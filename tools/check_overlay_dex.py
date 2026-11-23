#!/usr/bin/env python3
"""Before packaging an opt-in overlay, compare its actual D8 code to stable Smali."""
import argparse
import json
import os
from pathlib import Path
import shutil
import tempfile
from zipfile import ZipFile

from recovery_paths import RECOVERY_ROOT, ROOT
from verify_apk import TOOL_SHA, JNI, dex_inventory, digest, java_content, run


def main():
    p = argparse.ArgumentParser(description=__doc__)
    for name in ("overlay", "resources", "dex-dir", "report"):
        p.add_argument("--" + name, type=Path, required=True)
    a = p.parse_args()
    wanted = {s.strip() for s in (a.overlay / "replace.txt").read_text("utf-8").splitlines()
              if s.strip() and not s.lstrip().startswith("#")}
    originals = {}
    for f in (ROOT / "app/src/main/smali").rglob("*.smali"):
        with f.open(encoding="utf-8") as stream:
            desc = stream.readline().strip().split()[-1]
        if desc in wanted:
            originals[desc] = f.read_text("utf-8")
    apktool = RECOVERY_ROOT / "tools/apktool-2.12.1.jar"
    if digest(apktool.read_bytes()) != TOOL_SHA:
        raise SystemExit("Expected the pinned APKTool 2.12.1 for overlay verification.")
    java = Path(os.environ["JAVA_HOME"]) / "bin/java" if "JAVA_HOME" in os.environ else shutil.which("java")
    a.report.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(dir=a.report.parent) as tmp:
        t = Path(tmp)
        # This compiler probe contains only D8's output. It's neither installed
        # nor signed, and is not an input to the production APK packager.
        probe = t / "probe.apk"
        shutil.copyfile(a.resources, probe)
        dex = sorted(a.dex_dir.rglob("*.dex"))
        with ZipFile(probe, "a") as z:
            for n, f in enumerate(dex, 1):
                z.write(f, "classes.dex" if n == 1 else f"classes{n}.dex")
        run(java, "-Xmx1g", "-jar", apktool, "d", "-r", "-p", t/"fw", "-o", t/"decoded", probe)
        compiled, _ = dex_inventory(t / "decoded")
    changed = sorted(k for k in wanted if k not in originals or k not in compiled or
                     java_content(originals[k]) != java_content(compiled[k][2]))
    unexpected = sorted(compiled.keys() - (wanted | {JNI}))
    passed = not changed and not unexpected and JNI in compiled
    a.report.write_text(json.dumps({"passed": passed, "checked_types": sorted(wanted),
        "missing_or_changed_code": changed, "unexpected_d8_types": unexpected}, indent=2) + "\n", encoding="utf-8")
    if not passed:
        raise SystemExit(f"Overlay differs from DEX: changed={changed}; unexpected={unexpected}. See {a.report}")
    print(f"Matched declarations and instructions for {len(wanted)} additional Java-owned types.")


if __name__ == "__main__":
    main()
