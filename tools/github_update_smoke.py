"""Run release-response fixtures using the actual installed Debug APK on an emulator."""
import argparse
import os
from pathlib import Path
import shlex
import subprocess
import tempfile
import uuid

ROOT = Path(__file__).resolve().parents[1]
if __name__ == "__main__":
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--serial", required=True)
    ap.add_argument("--sdk", type=Path, required=True)
    args = ap.parse_args()
    adb = ["adb", "-s", args.serial]
    def run(*cmd):
        return subprocess.check_output([str(x) for x in cmd], timeout=60).decode("utf8", "replace").strip()
    assert run(*adb, "shell", "getprop ro.kernel.qemu") == "1"
    [apk] = [s.removeprefix("package:").strip() for s in
             run(*adb, "shell", "pm path com.alensw.PicFolder").splitlines() if s.endswith("base.apk")]
    android = args.sdk/"platforms/android-36/android.jar"
    # The probe uses class signatures for compilation, then loads those classes from the APK on device.
    classes = list((ROOT/"app/build/intermediates/javac/debug").rglob("com/alensw/updater/GitHubUpdates.class"))
    [compiled] = [p.parents[3] for p in classes]
    with tempfile.TemporaryDirectory() as tmp:
        run("javac", "--release", "8", "-classpath", os.pathsep.join(map(str,(android,compiled))), "-d", tmp,
            ROOT/"tools/version/ReleaseProbe.java")
        probe = Path(tmp)/"probe.jar"
        run("java", "-cp", args.sdk/"build-tools/36.0.0/lib/d8.jar", "com.android.tools.r8.D8",
            "--min-api", "23", "--lib", android, "--classpath", compiled, "--output", probe, *Path(tmp).rglob("*.class"))
        dest = "/data/local/tmp/openqpic-updates-"+uuid.uuid4().hex+".jar"
        try:
            run(*adb, "push", probe, dest)
            result = run(*adb, "shell", "CLASSPATH="+shlex.quote(dest+":"+apk)+" app_process / com.alensw.updater.ReleaseProbe")
            assert result.endswith(": OK"), result
            print(result)
        finally:
            run(*adb, "shell", "rm -f "+dest)
