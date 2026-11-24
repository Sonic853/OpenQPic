"""Exercise the gallery toolbar, Android Sharesheet and direct targets on an already-running
AOSP emulator. Install the gallery first, grant its storage access and enable videos. Requires
Pillow, FFmpeg (libx264), a JDK and the project Android SDK. Use an English or zh-CN test image
with at most six direct handlers for at least one media selection; the probe adds seven more
itself to exercise the overflow menu. Does not start an emulator or build/install the gallery.
"""
import argparse
from contextlib import ExitStack
import hashlib
import io
import json
import os
from pathlib import Path
import re
import shlex
import subprocess
import tempfile
import time
import uuid
from xml.etree import ElementTree as ET
from zipfile import ZipFile

from PIL import Image, PngImagePlugin

APP = "com.alensw.PicFolder"
PEER = "org.openqpic.shareprobe"
LABEL = "OpenQPic share probe"
CHOOSER = "android.intent.action.CHOOSER"
MORE = ("More…", "更多…", "更多选项…")
COLORS = {"png": (225, 30, 149), "jpg": (24, 222, 133), "mp4": (44, 103, 230)}
MIMES = {"png": "image/png", "jpg": "image/jpeg", "mp4": "video/mp4"}


def run(*args):
    return subprocess.check_output([str(a) for a in args], timeout=75)


def exe(name):
    return name + (".exe" if os.name == "nt" else "")


def build_probe(sdk, tmp):
    jar = sdk / "platforms/android-36/android.jar"
    bt = sdk / "build-tools/36.0.0"
    manifest = tmp / "AndroidManifest.xml"
    share_filter = """<intent-filter><action android:name="android.intent.action.SEND"/>
        <action android:name="android.intent.action.SEND_MULTIPLE"/>
        <category android:name="android.intent.category.DEFAULT"/><data android:mimeType="*/*"/></intent-filter>"""
    aliases = "".join(f'<activity-alias android:name=".Overflow{i}" android:label="OQP extra {i}" '
                      f'android:targetActivity=".ShareProbe" android:enabled="false" android:exported="true">'
                      f'{share_filter}</activity-alias>' for i in range(1, 8))
    queries = "".join(f'<intent><action android:name="android.intent.action.{action}"/>'
                      '<data android:mimeType="*/*"/></intent>' for action in ("SEND", "SEND_MULTIPLE", "SEND_MSG"))
    manifest.write_text(f"""<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="{PEER}">
        <queries><package android:name="{APP}"/>{queries}</queries>
        <application android:debuggable="true" android:label="{LABEL}">
        <activity android:name=".ShareProbe" android:exported="true">{share_filter}</activity>
        {aliases}</application></manifest>""", encoding="utf8")
    apk = tmp / "probe.apk"
    run(bt / exe("aapt2"), "link", "-I", jar, "--manifest", manifest, "--min-sdk-version", "23",
        "--target-sdk-version", "32", "-o", apk)
    run("javac", "--release", "8", "-Xlint:-options", "-classpath", jar, "-d", tmp,
        Path(__file__).parent / "share/ShareProbe.java")
    run("java", "-Xmx128m", "-cp", bt / "lib/d8.jar", "com.android.tools.r8.D8", "--min-api", "23",
        "--lib", jar, "--output", tmp, tmp / "org/openqpic/shareprobe/ShareProbe.class")
    with ZipFile(apk, "a") as z:
        z.write(tmp / "classes.dex", "classes.dex")
    aligned = tmp / "aligned.apk"
    run(bt / exe("zipalign"), "4", apk, aligned)
    # No storage permissions; never reuse the gallery's signing key.
    key = tmp / "probe.p12"
    run("keytool", "-genkeypair", "-keystore", key, "-storepass", "probe123", "-alias", "probe",
        "-keyalg", "RSA", "-keysize", "2048", "-validity", "7", "-dname", "CN=Share Probe")
    run("java", "-Xmx96m", "-jar", bt / "lib/apksigner.jar", "sign", "--ks", key,
        "--ks-pass", "pass:probe123", aligned)
    return aligned


def fixtures(tmp, ffmpeg):
    files = {kind: tmp / ("fixture." + kind) for kind in COLORS}
    meta = PngImagePlugin.PngInfo()
    meta.add_text("Comment", "Q" * 8192)
    Image.new("RGB", (160, 160), COLORS["png"]).save(files["png"], pnginfo=meta)
    exif = Image.Exif()
    exif[270] = "Q" * 8192
    Image.new("RGB", (160, 160), COLORS["jpg"]).save(files["jpg"], quality=95, exif=exif)
    color = "%02x%02x%02x" % COLORS["mp4"]
    run(ffmpeg, "-loglevel", "error", "-f", "lavfi", "-i", f"color=c=0x{color}:s=160x160:r=10:d=1",
        "-c:v", "libx264", "-profile:v", "baseline", "-pix_fmt", "yuv420p", "-movflags", "+faststart", files["mp4"])
    # A valid ISO BMFF free box keeps the solid-color fixture above the gallery's small-file filter.
    with files["mp4"].open("ab") as out:
        out.write((8 + 8192).to_bytes(4, "big") + b"free" + bytes(8192))
    return files


class Device:
    def __init__(self, adb, serial, dump):
        self.command = [adb, "-s", serial]
        self.dump = dump

    def adb(self, *args):
        return run(*self.command, *args)

    def sh(self, command):
        return self.adb("shell", command).decode("utf8", "replace").replace("\r", "").strip()

    def nodes(self):
        msg = self.sh("uiautomator dump " + self.dump)
        if "dumped" not in msg.lower():
            return []
        return list(ET.fromstring(self.adb("exec-out", "cat", self.dump)).iter("node"))

    @staticmethod
    def match(nodes, labels):
        for n in nodes:
            if n.get("resource-id") in labels or " ".join(n.get("text", "").split()) in labels:
                return n
            desc = " ".join(n.get("content-desc", "").split())
            if any(desc == label or desc.startswith(label + ",") for label in labels):
                return n
        return None

    def find(self, *labels, scroll=False):
        for attempt in range(6 if scroll else 3):
            nodes = self.nodes()
            match = self.match(nodes, labels)
            if match is not None:
                return match
            if scroll and attempt:
                w, h = self.screen().size
                self.sh(f"input swipe {w//2} {h*4//5} {w//2} {h//4} 350")
        raise AssertionError(("missing UI control", labels, [n.get("text") for n in nodes if n.get("text")]))

    def tap(self, node):
        left, top, right, bottom = map(int, re.findall(r"-?\d+", node.get("bounds")))
        assert right > left and bottom > top, node.attrib
        self.sh(f"input tap {(left + right)//2} {(top + bottom)//2}")

    def screen(self):
        return Image.open(io.BytesIO(self.adb("exec-out", "screencap", "-p"))).convert("RGB")

    def thumb(self, kind, long=False):
        # ImageGridView paints its children on a Canvas, without accessibility nodes.
        color = COLORS[kind]
        end = time.monotonic() + 18
        while time.monotonic() < end:
            img = self.screen()
            points = [(x, y) for y in range(img.height // 7, img.height - 14, 4)
                      for x in range(5, img.width - 5, 4)
                      if all(abs(a - b) < 18 for a, b in zip(img.getpixel((x, y)), color))]
            if len(points) > 12:
                x, y = (sum(axis) // len(points) for axis in zip(*points))
                self.sh(f"input swipe {x} {y} {x} {y} 1200" if long else f"input tap {x} {y}")
                return
        raise AssertionError(("No gallery thumbnail", kind))

    def foreground(self, *, chooser=False, activity=None):
        """Check the resumed *activity's* record, not a chooser left elsewhere in the back stack."""
        end = time.monotonic() + 12
        while time.monotonic() < end:
            state = self.sh("dumpsys activity -a activities")
            match = re.search(r"(?:topResumedActivity|mResumedActivity|ResumedActivity)\s*[:=]\s*"
                              r"ActivityRecord\{(\w+) u\d+ ([^\s}]+)", state)
            if match:
                token, component = match.groups()
                if activity and component == activity:
                    return component
                if chooser and component.split("/")[0] not in (APP, PEER):
                    records = re.split(r"(?m)^\s*(?:\*\s*)?Hist\s*#\d+:\s*ActivityRecord\{", state)[1:]
                    record = next((r for r in records if r.startswith(token + " ")), "")
                    outer = re.search(r"(?im)^\s*intent\s*[:=]?\s*\{[^\n}]*\bact=" + re.escape(CHOOSER)
                                      + r"\b[^\n]*", record)
                    if outer:
                        flags = re.search(r"\bflg=0x([0-9a-f]+)", outer[0], re.I)
                        assert flags and int(flags[1], 16) & 1, ("Chooser lost its read grant", outer[0])
                        package = component.split("/")[0]
                        assert "package:" + package in self.sh("pm list packages -s " + package).splitlines(), component
                        return component
            time.sleep(.2)
        raise AssertionError(("Expected foreground", CHOOSER if chooser else activity, match.groups() if match else None))

    def clear_result(self):
        self.sh(f"run-as {PEER} rm -f files/result")

    def probe_result(self):
        for _ in range(40):
            raw = self.sh(f"run-as {PEER} cat files/result 2>/dev/null || true")
            if raw:
                report = json.loads(raw)
                assert "error" not in report, report
                return report
            time.sleep(.2)
        raise AssertionError("The selected probe did not publish a fresh result")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--sdk", required=True, type=Path)
    parser.add_argument("--serial", required=True)
    parser.add_argument("--ffmpeg", default="ffmpeg", help="FFmpeg executable with libx264")
    parser.add_argument("--other-label", help="Require this exact root-menu label, e.g. 其它… on a zh-CN emulator")
    parser.add_argument("--report", type=Path, help="Optional JSON report for this APK and device")
    opt = parser.parse_args()
    tag = "OqpShare-" + uuid.uuid4().hex[:12]
    folder = "/storage/emulated/0/Pictures/" + tag
    d = Device(opt.sdk / "platform-tools" / exe("adb"), opt.serial, "/data/local/tmp/" + tag + ".xml")
    assert "1" in (d.sh("getprop ro.kernel.qemu"), d.sh("getprop ro.boot.qemu")), "Use a dedicated emulator"
    assert "targetSdk=32" in d.sh("dumpsys package " + APP), "Install the gallery test APK first"
    assert PEER not in d.sh("pm list packages " + PEER), "Test receiver is already installed"
    other = (opt.other_label,) if opt.other_label else ("Other…", "其它…")
    cases = []
    compact = []

    with tempfile.TemporaryDirectory() as td, ExitStack() as clean:
        tmp = Path(td)
        files = fixtures(tmp, opt.ffmpeg)
        hashes = {k: hashlib.sha256(f.read_bytes()).hexdigest() for k, f in files.items()}
        assert "Success" in d.adb("install", build_probe(opt.sdk, tmp)).decode()
        clean.callback(d.adb, "uninstall", PEER)
        clean.callback(d.sh, "rm -f " + d.dump)
        d.sh("mkdir -p " + folder)
        clean.callback(d.sh, "rmdir " + folder)
        for file in files.values():
            path = folder + "/" + file.name
            where = shlex.quote("_data='" + path + "'")
            table = "content://media/external/file"
            d.adb("push", file, path)
            clean.callback(d.sh, "rm -f " + path)
            clean.callback(d.sh, f"content delete --uri {table} --where {where}")
            d.sh("am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file://" + path)
            for _ in range(40):
                row = d.sh(f"content query --uri {table} --projection _id:_size --where {where}")
                if re.search(rf"\b_size={file.stat().st_size}\b", row):
                    break
                time.sleep(.25)
            else:
                raise AssertionError(("Media not indexed", path, row))
        clean.callback(d.sh, "am force-stop " + APP)

        def menu(keys, viewer=False):
            d.sh("am force-stop " + APP)
            d.sh(f"am start -W -n {APP}/.GalleryActivity -d file://{folder}")
            # GalleryActivity demonstrates the navigation drawer once per installed version.
            if d.match(d.nodes(), (APP + ":id/drawer_list",)):
                d.sh("input keyevent 4")
            d.find(tag + " (3)")
            d.thumb(keys[0], long=not viewer)
            if viewer and d.match(d.nodes(), (APP + ":id/share",)) is None:
                w, h = d.screen().size
                d.sh(f"input tap {w//2} {h//2}")  # expose the viewer toolbar
            for key in keys[1:]:
                d.thumb(key)
            d.tap(d.find(APP + ":id/share"))
            d.find(*other)
            nodes = d.nodes()
            labels = [n.get("text") for n in nodes if n.get("resource-id") == APP + ":id/title" and n.get("text")]
            assert any(label in labels for label in other), ("Other isn't in the gallery root menu", labels)
            return nodes, labels

        def check_payload(keys, mime):
            got = d.probe_result()
            action = "android.intent.action." + ("SEND" if len(keys) == 1 else "SEND_MULTIPLE")
            assert got["action"] == action and got["type"] == mime, got
            assert got["uid"] != got["galleryUid"] and got["flags"] & 1 and not got["flags"] & 2, got
            assert got["zhCN"] == ["快图", "快图", "其它…"] and not got["privatePaths"], got
            assert sorted(got["clipTypes"]) == sorted({MIMES[k] for k in keys}), got
            assert sorted(got["sha256"]) == sorted(hashes[k] for k in keys), got
            return got

        def receive(route, keys, mime, viewer=False):
            d.clear_result()
            nodes, labels = menu(keys, viewer)
            system = None
            if route == "chooser":
                d.tap(d.match(nodes, other))
                system = d.foreground(chooser=True)
                d.tap(d.find(LABEL, scroll=True))
            else:
                target = d.match(nodes, (LABEL,))
                if target is None:
                    d.tap(d.find(*MORE))
                    target = d.find(LABEL, scroll=True)
                d.tap(target)
            got = check_payload(keys, mime)
            if route == "chooser" and got["directTargets"] <= 6:
                assert d.match(nodes, MORE) is None, labels
                compact.append({"selection": keys, "direct_targets": got["directTargets"], "root_menu": labels})
            cases.append({"route": route, "selection": keys, "chooser": system, **got})
            print(f"{route}: {got['action']} {mime} ({', '.join(keys)}) OK")

        receive("chooser", ["png"], "image/png", viewer=True)
        receive("chooser", ["png", "jpg"], "image/*")
        receive("chooser", ["mp4"], "video/mp4")
        receive("chooser", ["jpg", "mp4"], "*/*")
        receive("direct", ["png"], "image/png")
        receive("direct", ["png", "jpg"], "image/*")
        assert compact, "No compact gallery menu was exercised; use an AOSP emulator with fewer share handlers"

        nodes, _ = menu(["png", "mp4"])
        transfer = ("WiFi transfer", "WiFi 快传")
        target = d.match(nodes, transfer)
        if target is None:
            d.tap(d.find(*MORE))
            target = d.find(*transfer, scroll=True)
        d.tap(target)
        d.foreground(activity=APP + "/com.alensw.transfer.TransferActivity")
        assert d.find(APP + ":id/files_to_send").get("text", "").endswith("(2)")

        # Exercise the original More submenu and the separate, still top-level Sharesheet row.
        d.clear_result()
        d.sh(f"am start -W -n {PEER}/.ShareProbe -a org.openqpic.shareprobe.ENABLE_OVERFLOW")
        assert d.probe_result() == {"overflow": True}
        nodes, dense = menu(["png"])
        assert d.match(nodes, MORE) is not None, ("Expected the direct-target overflow", dense)
        d.tap(d.match(nodes, other))
        overflow_chooser = d.foreground(chooser=True)
        d.sh("input keyevent 4")
        d.clear_result()
        menu(["png"])
        d.tap(d.find(*MORE))
        d.tap(d.find(*(f"OQP extra {i}" for i in range(1, 8)), scroll=True))
        overflow_direct = check_payload(["png"], "image/png")
        assert overflow_direct["directTargets"] > 6, overflow_direct
        cases.append({"route": "direct-submenu", "selection": ["png"], **overflow_direct})

        # The editor first renders a new image. A chooser opened on the original template
        # would succeed at granting a URI but would deliver the wrong bytes and dimensions.
        name = tag + "-crop.jpg"
        source = folder + "/" + name
        rendered = "/sdcard/Android/data/" + APP + "/cache/.temp/" + name
        d.adb("push", files["jpg"], source)
        clean.callback(d.sh, "rm -f " + source + " " + rendered)
        d.clear_result()
        d.sh("am force-stop " + APP)
        d.sh(f"am start -W -n {APP}/.CropActivity -a android.intent.action.EDIT -d file://{source} "
             "-t image/jpeg --ei aspectX 3 --ei aspectY 2 --ei outputX 72 --ei outputY 48 --ez noFaceDetection true")
        d.tap(d.find(APP + ":id/share"))
        d.tap(d.find(*other))
        crop_chooser = d.foreground(chooser=True)
        d.tap(d.find(LABEL, scroll=True))
        crop = d.probe_result()
        result = d.adb("exec-out", "cat", rendered)
        with Image.open(io.BytesIO(result)) as bitmap:
            assert bitmap.size == (72, 48) and bitmap.format == "JPEG", bitmap
        digest = hashlib.sha256(result).hexdigest()
        assert crop["type"] == "image/jpeg" and crop["action"] == "android.intent.action.SEND", crop
        assert crop["sha256"] == [digest] and digest != hashes["jpg"], crop
        cases.append({"route": "crop-chooser", "selection": ["rendered-jpeg"], "width": 72, "height": 48,
                      "chooser": crop_chooser, **crop})
        apk = d.sh("pm path " + APP).splitlines()[0].removeprefix("package:")
        report = {"device": d.sh("getprop ro.build.fingerprint"), "apk_sha256": hashlib.sha256(
            d.adb("exec-out", "cat", apk)).hexdigest(), "cases": cases, "compact": compact,
            "overflow_root": dense, "overflow_chooser": overflow_chooser, "localsend_direct_count": 2}
    if opt.report:
        opt.report.parent.mkdir(parents=True, exist_ok=True)
        opt.report.write_text(json.dumps(report, indent=2, ensure_ascii=False) + "\n", encoding="utf8")
    print("Sharesheet, rendered crop, compact/overflow menus, direct receiver, LocalSend and zh-CN labels passed.")


if __name__ == "__main__":
    main()
