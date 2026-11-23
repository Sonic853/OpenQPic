#!/usr/bin/env python3
"""Request Android Studio's Gradle V2 models using the already-cached wrapper and AGP."""
import argparse
import json
import os
from pathlib import Path
import shutil
import subprocess

from recovery_paths import RECOVERY_ROOT, ROOT


def one(paths, label):
    matches = list(paths)
    if len(matches) != 1:
        raise SystemExit(f"Expected one {label}, found {matches}. Run the project wrapper first.")
    return matches[0]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--gradle-user-home", type=Path,
                        default=Path(os.environ.get("GRADLE_USER_HOME", Path.home() / ".gradle")))
    parser.add_argument("--gradle-home", type=Path, help="optional unpacked Gradle 9.2.1")
    args = parser.parse_args()
    cache = args.gradle_user_home.resolve()
    gradle = args.gradle_home or one((cache / "wrapper/dists/gradle-9.2.1-bin").glob("*/gradle-9.2.1"), "Gradle")
    builder = one((cache / "caches/modules-2/files-2.1/com.android.tools.build/builder-model/9.0.1")
                  .glob("*/builder-model-9.0.1.jar"), "builder-model")
    out = ROOT / "app/build/tmp/studio-model-probe"
    out.mkdir(parents=True, exist_ok=True)
    cp = os.pathsep.join(map(str, [builder, gradle / "lib/*", out]))
    jdk = Path(os.environ["JAVA_HOME"]) / "bin" if "JAVA_HOME" in os.environ else None
    command = lambda exe: str(jdk / exe) if jdk else shutil.which(exe)
    subprocess.run([command("javac"), "-encoding", "UTF-8", "-cp", cp, "-d", out,
                    ROOT / "tools/StudioModelProbe.java"], check=True)
    lines = subprocess.check_output([command("java"), "-Dfile.encoding=UTF-8", "-cp", cp, "StudioModelProbe",
                                     ROOT, gradle, cache], encoding="utf-8").splitlines()
    data = {}
    for line in lines:
        if "=" in line:
            k, v = line.split("=", 1)
            data.setdefault(k, []).append(v)
    for key, path in {"manifest": "app/src/main/AndroidManifest.xml",
                      "res": "app/src/main/res", "jniLibs": "app/src/main/jniLibs", "smali": "app/src/main/smali"}.items():
        if [Path(v).resolve() for v in data.get(key, [])] != [(ROOT / path).resolve()]:
            raise SystemExit(f"Unexpected IDE model {key}: {data.get(key)}")
    reading = RECOVERY_ROOT / "reference/java"
    if [Path(v).resolve() for v in data.get("reading", [])] != ([reading.resolve()] if reading.is_dir() else []):
        raise SystemExit(f"Unexpected IDE model reading: {data.get('reading')}")
    if {Path(p).resolve() for p in data.get("java", [])} != {
        (ROOT/"app/src/main/java").resolve(), (ROOT/"app/src/recovered/java").resolve()}:
        raise SystemExit(f"Unexpected IDE Java roots: {data.get('java')}")
    if (data.get("module") != [":app type=APPLICATION"] or data.get("namespace") != ["com.alensw.PicFolder"]
            or set(data.get("variant", [])) != {"debug", "release"}):
        raise SystemExit(f"Unexpected Android module: {data}")
    report = ROOT / "app/build/reports/stable-debug/studio-model.json"
    report.parent.mkdir(parents=True, exist_ok=True)
    report.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("\n".join(lines))
    print(f"PASS: {report}")


if __name__ == "__main__":
    main()
