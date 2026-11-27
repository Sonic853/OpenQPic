#!/usr/bin/env python3
"""Cross-check the signed AGP APK against the selected stable APK, not a staging folder."""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import tempfile
from zipfile import ZipFile

from recovery_paths import RECOVERY_ROOT, ROOT

BUILD = ROOT / "app/build"
APK_SHA = "23ed7071608dc05465c6885a98c5884478defddc9731580929533bd3d9a6b107"
TOOL_SHA = "66cf4524a4a45a7f56567d08b2c9b6ec237bcdd78cee69fd4a59c8a0243aeafa"
JNI = "Lcom/alensw/jni/JniUtils;"
JAVA_PATH = "com/alensw/jni/JniUtils.java"
LIBS = {f"lib/{abi}/libqpicjni156.so" for abi in ("armeabi", "x86")}
METADATA = "META-INF/com/android/build/gradle/app-metadata.properties"
DEX = re.compile(r"classes(?:[2-9]|[1-9][0-9]+)?\.dex")
SIGNATURE = re.compile(r"META-INF/(?:MANIFEST\.MF|[^/]+\.(?:SF|RSA|DSA|EC))", re.I)


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def run(*args) -> str:
    result = subprocess.run([str(a) for a in args], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode:
        raise RuntimeError((result.stdout + result.stderr).decode("utf-8", "replace")[-6000:])
    return result.stdout.decode("utf-8", "replace")


def diff(left, right):
    return {"missing": sorted(left.keys() - right.keys()), "added": sorted(right.keys() - left.keys()),
            "changed": sorted(k for k in left.keys() & right.keys() if left[k] != right[k])}


def dex_inventory(decoded: Path):
    inventory, counts = {}, {}
    for folder in sorted(decoded.glob("smali*")):
        if not folder.is_dir():
            continue
        name = ("classes" if folder.name == "smali" else folder.name.removeprefix("smali_")) + ".dex"
        members = []
        method_count = 0
        for path in folder.rglob("*.smali"):
            text = path.read_text(encoding="utf-8")  # normalize only CRLF
            header = text.partition("\n")[0]
            desc = header.rsplit(" ", 1)[-1]
            if not header.startswith(".class ") or not re.fullmatch(r"L[^;\n]+;", desc) or desc in inventory:
                raise ValueError(f"Missing or repeated DEX descriptor at {path}")
            methods = sum(line.startswith(".method ") for line in text.splitlines())
            method_count += methods
            members.append(desc)
            inventory[desc] = (name, methods, text)
        counts[name] = {"class_defs": len(members), "method_defs": method_count}
    return inventory, counts


def java_content(text: str):
    # Only source file/line metadata may differ in the selected Java classes.
    # Never ignore declarations, annotations, exception handlers or opcodes.
    return [line for line in (v.strip() for v in text.splitlines()) if line and
            not (line.startswith('.source "') or re.fullmatch(r"\.line [0-9]+", line))]


def decoded_resources(decoded):
    res = decoded / "res"
    return {p.relative_to(res).as_posix(): digest(p.read_bytes().replace(b"\r\n", b"\n")
            if p.suffix == ".xml" else p.read_bytes()) for p in res.rglob("*") if p.is_file()}


def normalized_mipmap_path(s):
    # AAPT2 names 20 backing files *-v4; the linked density/configs and
    # image bytes remain identical. Don't normalize any other qualifiers.
    return re.sub(r"res/(mipmap-(?:mdpi|hdpi|xhdpi|xxhdpi|xxxhdpi))-v4/", r"res/\1/", s)


def is_debuggable(line):
    return line.strip() == "A: http://schemas.android.com/apk/res/android:debuggable(0x0101000f)=true"


def zip_digest_map(z, predicate):
    names = z.namelist()
    if len(names) != len(set(names)):
        raise ValueError("Duplicate ZIP member")
    return {n: digest(z.read(n)) for n in names if not n.endswith("/") and predicate(n)}


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--build-dir", type=Path, default=BUILD)
    ap.add_argument("--apk", type=Path)
    ap.add_argument("--baseline", type=Path, default=RECOVERY_ROOT / "reference/OpenPic_stable.apk")
    ap.add_argument("--apktool", type=Path, default=RECOVERY_ROOT / "tools/apktool-2.12.1.jar")
    ap.add_argument("--build-tools", required=True, type=Path)
    ap.add_argument("--report", type=Path)
    ap.add_argument("--bundle", type=Path, help="also verify the packaged .aab")
    ap.add_argument("--overlay", type=Path)
    ap.add_argument("--built-in", action="store_true", help="verify both physical Java roots of the default app")
    a = ap.parse_args()
    if a.built_in:
        if a.overlay:
            ap.error("custom and built-in overlay are mutually exclusive")
        a.overlay = ROOT / "app/recovered-java"
    build = a.build_dir.resolve()
    a.apk = a.apk or build / "outputs/apk/debug/app-debug.apk"
    a.report = a.report or build / "reports/stable-debug/verification.json"
    owned = {JNI}
    if a.overlay:
        owned.update(s.strip() for s in (a.overlay / "replace.txt").read_text("utf-8").splitlines()
                     if s.strip() and not s.lstrip().startswith("#"))
    baseline, apk, tools = a.baseline.resolve(), a.apk.resolve(), a.build_tools.resolve()
    if digest(baseline.read_bytes()) != APK_SHA or digest(a.apktool.read_bytes()) != TOOL_SHA:
        raise SystemExit("Unexpected stable input or verifier APKTool SHA-256")
    java = Path(os.environ["JAVA_HOME"]) / "bin/java" if "JAVA_HOME" in os.environ else shutil.which("java")
    suffix = ".exe" if os.name == "nt" else ""
    aapt = tools / f"aapt2{suffix}"
    report = {"baseline_sha256": APK_SHA, "apk_sha256": digest(apk.read_bytes()), "apk": apk.name,
              "target_executed": False, "checks": {}}
    checks = report["checks"]

    def check(key, passed, **facts):
        checks[key] = {"passed": bool(passed), **facts}

    with ZipFile(baseline) as old, ZipFile(apk) as new:
        native = [zip_digest_map(z, lambda n: n.startswith("lib/")) for z in (old, new)]
        check("native", native[0] == native[1] and native[1].keys() == LIBS,
              sha256=native[1], stored=all(new.getinfo(k).compress_type == 0 for k in native[1]))
        assets = [zip_digest_map(z, lambda n: n.startswith(("assets/", "res/raw/"))) for z in (old, new)]
        check("assets_raw", assets[0] == assets[1], members=len(assets[1]))
        members = [{normalized_mipmap_path(n) for n in z.namelist()
                    if not n.endswith("/") and not SIGNATURE.fullmatch(n)} for z in (old, new)]
        dex_hashes = zip_digest_map(new, DEX.fullmatch)
        expected_additions = (set(dex_hashes) - {"classes.dex"} if a.overlay else {"classes2.dex"}) | {METADATA}
        check("payload_inventory", (members[0] | expected_additions) == members[1],
              original_count=len(members[0]), packaged_count=len(members[1]),
              added=sorted(members[1] - members[0]), missing=sorted(members[0] - members[1]))

    manifest = [[re.sub(r" \(line=\d+\)", "", l) for l in run(
        aapt, "dump", "xmltree", p, "--file", "AndroidManifest.xml").splitlines()] for p in (baseline, apk)]
    actual = [l for l in manifest[1] if not is_debuggable(l)]
    tags = Counter(m[1] for line in actual if (m := re.search(r"^\s*E: ([\w-]+)$", line)))
    check("binary_manifest", actual == manifest[0] and sum(map(is_debuggable, manifest[1])) == 1,
          ignored=["XML source line numbers", "debug variant android:debuggable=true"], tags=dict(tags))

    tables = [run(aapt, "dump", "resources", p).splitlines() for p in (baseline, apk)]
    ids = [{m[2]: m[1] for l in table if (m := re.match(r"^\s*resource (0x[0-9a-f]+) (\S+)", l))}
           for table in tables]
    pinned = dict(line.removeprefix("com.alensw.PicFolder:").split(" = ") for line in
                  (ROOT / "app/resource-ids.txt").read_text("utf-8").splitlines())
    pinned = {k: v for k, v in pinned.items() if k in ids[0]}  # The original APK predates OpenQPic's product IDs.
    check("resource_ids", ids[0] == ids[1] == pinned and len(pinned) == 918, names_and_ids=len(ids[1]),
          differences=diff(ids[0], ids[1]))
    check("complete_resource_table_dump", tables[0] == [normalized_mipmap_path(l) for l in tables[1]],
          lines=len(tables[1]), renamed_backing_paths=sum(l != normalized_mipmap_path(l) for l in tables[1]))
    symbols = build / "intermediates/runtime_symbol_list/debug/processDebugResources/R.txt"
    projected = {k.replace(".", "_"): v for k, v in pinned.items()}
    for keyword in ("default", "interface"):
        projected["string/stable_keyword_" + keyword] = projected.pop("string/" + keyword)
    agp_ids = {f"{t}/{n}": v for kind, t, n, v in (s.split() for s in symbols.read_text("utf-8").splitlines())
               if kind == "int"}
    check("compile_time_R", projected == agp_ids, fields=len(agp_ids),
          java_only_aliases=["string/stable_keyword_default", "string/stable_keyword_interface"])

    a.report.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(dir=a.report.parent) as tmp:
        temp = Path(tmp)
        views = [temp / "stable", temp / "agp"]
        for view, p in zip(views, (baseline, apk)):
            run(java, "-Xmx1g", "-jar", a.apktool.resolve(), "d", "-p", temp / "framework", "-o", view, p)
        (old, old_counts), (new, new_counts) = map(dex_inventory, views)
        old_text = {k: v[2] for k, v in old.items()}
        new_text = {k: v[2] for k, v in new.items()}
        text_diff = diff(old_text, new_text)
        failed_java = sorted(k for k in owned if k not in old or k not in new or
                             java_content(old_text[k]) != java_content(new_text[k]))
        check("all_dex_definitions_and_bodies", not text_diff["missing"] and not text_diff["added"] and
              not (set(text_diff["changed"]) - owned) and not failed_java and len(new) == 2332,
              original=old_counts, packaged=new_counts, exact_smali_classes=len(new) - len(text_diff["changed"]),
              text_differences=text_diff, java_not_equal_except_source_and_line_markers=failed_java,
              classes=len(new), methods=sum(c["method_defs"] for c in new_counts.values()))
        res_a, res_b = map(decoded_resources, views)
        check("decoded_resources", res_a == res_b, paths=len(res_b), differences=diff(res_a, res_b))
        jni_dex = new[JNI][0]
        java_members = {v[0] for k, v in new.items() if k in owned}
        smali_members = {v[0] for k, v in new.items() if k not in owned}
        if a.bundle:
            with ZipFile(a.bundle) as bundle:
                library_files = {n.removeprefix("base/"): h for n, h in
                                 zip_digest_map(bundle, lambda n: n.startswith("base/lib/")).items()}
                bundled_dex = {n.removeprefix("base/dex/"): h for n, h in
                               zip_digest_map(bundle, lambda n: n.startswith("base/dex/")).items()}
                # AAPT2 reads an ordinary proto resource archive. Reconstitute
                # that view of the FINAL bundle; don't inspect the prebundle.
                with ZipFile(temp / "proto.zip", "w") as proto:
                    for name in bundle.namelist():
                        if name == "base/manifest/AndroidManifest.xml":
                            proto.writestr("AndroidManifest.xml", bundle.read(name))
                        elif name == "base/resources.pb" or (name.startswith("base/res/") and not name.endswith("/")):
                            proto.writestr(name.removeprefix("base/"), bundle.read(name))
            run(aapt, "convert", "--output-format", "binary", "-o", temp / "from-bundle.ap_", temp / "proto.zip")
            bundle_table = run(aapt, "dump", "resources", temp / "from-bundle.ap_").splitlines()
            bundle_manifest = [re.sub(r" \(line=\d+\)", "", l) for l in run(aapt, "dump", "xmltree",
                temp / "from-bundle.ap_", "--file", "AndroidManifest.xml").splitlines()]
            check("bundle_payload", library_files == native[1] and dex_hashes == bundled_dex and
                  bundle_table == tables[1] and bundle_manifest == manifest[1],
                  abi_members=sorted(library_files), dex_members=sorted(bundled_dex),
                  same_resource_table_as_apk=bundle_table == tables[1], same_manifest_as_apk=bundle_manifest == manifest[1])

    inter = build / "intermediates"
    smali_dex = inter / "dex/debug/assembleDebugSmali/classes.dex"
    java_dex = list((inter / "dex/debug/mergeProjectDexDebug").rglob("*.dex"))
    javac = inter / "javac/debug/compileDebugJavaWithJavac/classes"
    runtime_javas = sorted(p.relative_to(javac).as_posix() for p in javac.rglob("*.class"))
    reading = RECOVERY_ROOT / "reference/java"
    java_src = ROOT / "app/src/main/java" / JAVA_PATH
    smali_sources = list((ROOT / "app/src/main/smali").rglob("*.smali"))
    original_java = (reading / JAVA_PATH).read_bytes()
    if a.built_in:
        staged = ROOT / "app/src/recovered/java"
        index = (a.overlay / "sources.txt").read_text("utf-8").splitlines()
        files = {f.relative_to(staged).as_posix() for f in staged.rglob("*.java")}
        changed = sorted(f for f in files if not (reading/f).is_file() or (reading/f).read_bytes() != (staged/f).read_bytes())
        check("built_in_java_sources", set(index) == files and len(index) == len(files) and not changed
              and java_src.read_bytes() == original_java, canonical_files=len(files)+1,
              java_class_owners=len(owned), changed=changed)
    d8_matches = Counter(digest(p.read_bytes()) for p in java_dex) == Counter(dex_hashes[k] for k in java_members)
    smali_matches = (len(smali_members) == 1 and not (smali_members & java_members) and
                     digest(smali_dex.read_bytes()) == dex_hashes[next(iter(smali_members))])
    check("runtime_input_provenance", d8_matches and smali_matches and len(smali_sources) == 2331 and
          java_src.read_bytes() == original_java and runtime_javas == sorted(k[1:-1] + ".class" for k in owned),
          javac_outputs=runtime_javas, jni_dex=jni_dex, javac_source_sha256=digest(original_java),
          matches_d8_output=d8_matches, matches_smali_output=smali_matches, smali_inputs=2332-len(owned),
          packaged_dex_sha256=dex_hashes)
    reading_count = sum(1 for _ in reading.rglob("*.java"))
    check("reading_root", reading_count == 2163, files=reading_count, entire_root_passed_to_javac=False)
    signature = run(java, "-jar", tools / "lib/apksigner.jar", "verify", "--verbose", "--print-certs", apk)
    check("apk_signature", "Verified using v2 scheme (APK Signature Scheme v2): true" in signature,
          schemes=[l for l in signature.splitlines() if l.startswith("Verified using") and l.endswith("true")],
          signer_certificate_sha256=next(l.rsplit(": ", 1)[1] for l in signature.splitlines()
                                         if "certificate SHA-256 digest:" in l))
    run(tools / f"zipalign{suffix}", "-c", "4", apk)
    check("zip_alignment", True)
    report["passed"] = all(c["passed"] for c in checks.values())
    a.report.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    for name, result in checks.items():
        print(f"{'PASS' if result['passed'] else 'FAIL'} {name}")
    print(a.report)
    return 0 if report["passed"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
