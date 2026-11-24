"""Compile this package in isolation and verify its Java and DEX ownership."""

import argparse
import csv
import hashlib
import os
from pathlib import Path
import struct
import subprocess
import tempfile
import zipfile


HERE = Path(__file__).resolve().parent
JAVA_ROOT = HERE.parents[2]
SWITCH_MAPS = {
    "Lorg/apache/http/impl/auth/GGSSchemeBase$1;",
    "Lorg/apache/http/impl/cookie/RFC6265CookieSpecProvider$2;",
}
CAS_BACKPORT = "Lorg/apache/http/impl/bootstrap/HttpServer$$ExternalSyntheticBackportWithForwarding0;"


def dex_ownership(d, allow_compiler_extras=False):
    """Per-class fingerprint of hierarchy, declared member descriptors and code presence."""
    if d[:8] != b"dex\n035\0":
        raise ValueError("Expected DEX 035 (min API 23)")

    def u4(p):
        return struct.unpack_from("<I", d, p)[0]

    def uleb(p):
        result = 0
        for shift in range(0, 35, 7):
            b = d[p]
            result |= (b & 127) << shift
            p += 1
            if b < 128:
                return result, p
        raise ValueError("Malformed DEX unsigned LEB128")

    def table(offset, size):
        count, start = struct.unpack_from("<II", d, offset)
        return range(start, start + count * size, size)

    def string(idx):
        _, start = uleb(u4(u4(60) + 4 * idx))
        # Only type, field and method identifiers in this package are used as keys.
        return d[start:d.index(b"\0", start)].decode("utf8", "surrogateescape")

    types = [string(u4(p)) for p in table(64, 4)]

    def type_list(offset):
        if not offset:
            return []
        return [types[i] for i in struct.unpack_from(f"<{u4(offset)}H", d, offset + 4)]

    proto = ["(" + "".join(type_list(u4(p + 8))) + ")" + types[u4(p + 4)] for p in table(72, 12)]
    fields, methods = [], []
    for offset, parts, pool, sep in ((80, fields, types, ":"), (88, methods, proto, "")):
        for p in table(offset, 8):
            owner, typ, name = struct.unpack_from("<HHI", d, p)
            parts.append((types[owner], string(name) + sep + pool[typ]))
    inventory, extra_methods = {}, []
    for p in table(96, 32):
        owner = types[u4(p)]
        if not owner.startswith("Lorg/apache/"):
            continue
        is_enum = bool(u4(p + 4) & 0x4000)
        shape = ["class " + owner, "super " + types[u4(p + 8)]]
        shape.extend("interface " + t for t in type_list(u4(p + 12)))
        pos = u4(p + 24)
        counts = []
        if pos:
            for _ in range(4):
                n, pos = uleb(pos)
                counts.append(n)
        field_count = method_count = body_count = 0
        for section, count in zip(("static", "instance", "direct", "virtual"), counts):
            idx = 0
            members = []
            for _ in range(count):
                inc, pos = uleb(pos)
                flags, pos = uleb(pos)
                idx += inc
                if section in ("static", "instance"):
                    decl, name = fields[idx]
                    field_count += 1
                else:
                    code, pos = uleb(pos)
                    decl, name = methods[idx]
                    # Modern javac enum-array factories and the implicit constructor
                    # of a Java-written switch table have no original member.
                    if allow_compiler_extras and (
                        (is_enum and (flags & 0x100a) == 0x100a and name == "$values()[" + owner)
                        or (owner in SWITCH_MAPS and flags & 2 and name == "<init>()V")
                    ):
                        extra_methods.append(owner + "->" + name)
                        continue
                    method_count += 1
                    body_count += bool(code)
                    name += " code" if code else " declaration"
                if owner != decl:
                    raise ValueError(f"Unexpected declaring class: {owner}, {decl}")
                members.append(section + " " + name)
            shape.extend(sorted(members))
        sha = hashlib.sha256(("\n".join(shape) + "\n").encode()).hexdigest()
        inventory[owner] = (field_count, method_count, body_count, sha)
    return inventory, extra_methods


def verify(opts, work):
    with (HERE / "ownership.tsv").open(encoding="utf8") as f:
        rows = list(csv.DictReader(f, delimiter="\t"))
    sources = {r["java"] for r in rows}
    expected = {r["descriptor"]: (int(r["fields"]), int(r["methods"]), int(r["bodies"]), r["declarations_sha256"])
                for r in rows}
    if len(expected) != 828 or len(rows) != 828 or len(sources) != 707:
        raise ValueError("Unexpected ownership list")
    actual = {p.relative_to(JAVA_ROOT).as_posix() for p in (JAVA_ROOT / "org/apache").rglob("*.java")}
    if actual != sources:
        raise ValueError(f"Java source set changed: {sorted(actual ^ sources)}")
    classes = work / "classes"
    empty = work / "empty-source-path"
    classes.mkdir()
    empty.mkdir()
    files = work / "sources.args"
    files.write_text("\n".join('"' + (JAVA_ROOT / n).as_posix() + '"' for n in sorted(sources)), "utf8")
    subprocess.run([opts.jdk / "bin/javac", "-source", "8", "-target", "8", "-Xlint:-options",
                    "-g:source,lines", "-encoding", "UTF-8", "-proc:none", "-implicit:none",
                    "-sourcepath", empty, "-bootclasspath", opts.android_jar, "-classpath",
                    opts.compile_api, "-d", classes, "@" + str(files)], check=True)
    classfiles = sorted(classes.rglob("*.class"))
    compiled = {"L" + p.relative_to(classes).as_posix()[:-6] + ";" for p in classfiles}
    if compiled != expected.keys():
        raise ValueError(f"javac identity difference: {sorted(compiled ^ expected.keys())}")
    program = work / "javac-only.jar"
    with zipfile.ZipFile(program, "w") as jar:
        for p in classfiles:
            jar.write(p, p.relative_to(classes).as_posix())
    dest = work / "dex.zip"
    subprocess.run([opts.jdk / "bin/java", "-cp", opts.d8, "com.android.tools.r8.D8", "--min-api",
                    "23", "--debug", "--lib", opts.android_jar, "--classpath", opts.compile_api,
                    "--output", dest, program], check=True)
    with zipfile.ZipFile(dest) as z:
        inv, extras = dex_ownership(z.read("classes.dex"), allow_compiler_extras=True)
    generated = inv.keys() - expected.keys()
    if generated - {CAS_BACKPORT}:
        raise ValueError(f"Unexpected D8 classes: {sorted(generated)}")
    changed = [n for n, shape in expected.items() if inv.get(n) != shape]
    if changed:
        raise ValueError(f"Missing/changed DEX declarations: {changed}")
    print(f"{len(sources)} Java files; {len(compiled)} class files; {len(expected)} DEX identities; "
          f"{sum(v[1] for v in expected.values())} original methods, "
          f"{sum(v[2] for v in expected.values())} original code items present.")
    print(f"Compiler-only additions: {len(extras)} methods in original types, {sorted(generated)}")


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--jdk", type=Path, default=os.environ.get("JAVA_HOME"))
    cli.add_argument("--android-jar", type=Path, required=True)
    cli.add_argument("--compile-api", type=Path, required=True, help="output of prepare_compileonly.py")
    cli.add_argument("--d8", type=Path, required=True, help="SDK build-tools/36.0.0/lib/d8.jar")
    cli.add_argument("--work", type=Path, required=True, help="parent for isolated temporary outputs")
    opts = cli.parse_args()
    if opts.jdk is None:
        cli.error("supply --jdk or set JAVA_HOME")
    opts.work.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(dir=opts.work) as directory:
        verify(opts, Path(directory))
