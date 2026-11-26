"""Import the two JNI libraries, matching transport bridge and repository-only license.
The app remains buildable without the native source checkouts or an NDK."""
import argparse
import hashlib
import json
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def inputs(image, transport):
    for abi in ("armeabi", "x86", "arm64-v8a"):
        for build, lib in ((image/"native-src/build", "libqpicjni156.so"),
                           (transport/"build", "libqpiclocalsend.so")):
            yield build/"libs"/abi/lib, Path("app/src/main/jniLibs")/abi/lib
    for name in ("Wire.java", "LocalSend.java", "LoopbackHttp.java", "SystemTls.java", "HttpChannel.java"):
        path = Path("org/openqpic/localsend")/name
        yield transport/"java"/path, Path("app/src/main/java")/path
    yield transport/"third_party/picohttpparser/NOTICE", Path("licenses/picohttpparser.txt")


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--image", type=Path, default=ROOT.parent/"OpenQPicJni")
    p.add_argument("--transport", type=Path, default=ROOT.parent/"OpenQPicLocalSend")
    opt = p.parse_args()
    hashes = {}
    for src, rel in inputs(opt.image, opt.transport):
        dst = ROOT/rel
        dst.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(src, dst)
        hashes[rel.as_posix()] = hashlib.sha256(dst.read_bytes()).hexdigest()
    (ROOT/"app/native-inputs.json").write_text(json.dumps(hashes, indent=2) + "\n", "utf-8")


if __name__ == "__main__":
    main()
