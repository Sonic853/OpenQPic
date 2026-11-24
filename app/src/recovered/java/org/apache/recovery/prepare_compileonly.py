"""Build javac-only declarations for optional APIs used by the four Apache releases.

The output does not contain an implementation of any OpenQPic type. Use it only on
the compiler classpath. Inputs are the JDK's Java 8 signatures and the versions
declared as optional/provided by the Commons Logging 1.2 POM.
"""

import argparse
import hashlib
import io
import os
from pathlib import Path
import struct
import urllib.request
import zipfile


MAVEN = "https://repo.maven.apache.org/maven2/"
ARTIFACTS = {
    "avalon-framework/avalon-framework/4.1.5/avalon-framework-4.1.5.jar":
        "833f1e50dfa628f13d4a4206b3ec6d8f42e96284a35d76c072d880804b747a04",
    "javax/servlet/servlet-api/2.3/servlet-api-2.3.jar":
        "8478b902d0815ed066db860fb14cc5d404548d4b6348ab930b46270fcddeba68",
    "log4j/log4j/1.2.17/log4j-1.2.17.jar":
        "1d31696445697720527091754369082a6651bd49781b6005deb94e56753406f9",
    "logkit/logkit/1.0.1/logkit-1.0.1.jar":
        "7ea93b4fc21f3d05ed224b168a025f864db75ddfddc2343e1ec29a386d7501e0",
}

# Directly named by the original DEX. Add external superclasses/interfaces below.
REQUIRED = """
javax/naming/InvalidNameException javax/naming/NamingException
javax/naming/directory/Attribute javax/naming/directory/Attributes
javax/naming/ldap/LdapName javax/naming/ldap/Rdn
javax/servlet/ServletContextEvent javax/servlet/ServletContextListener
org/apache/avalon/framework/logger/Logger org/apache/log/Hierarchy org/apache/log/Logger
org/apache/log4j/Level org/apache/log4j/Logger org/apache/log4j/Priority
org/ietf/jgss/GSSContext org/ietf/jgss/GSSCredential org/ietf/jgss/GSSException
org/ietf/jgss/GSSManager org/ietf/jgss/GSSName org/ietf/jgss/Oid
""".split()


def header(b):
    """Keep declarations, including constants and checked exceptions; remove all Code attributes."""
    if b[:4] != b"\xca\xfe\xba\xbe":
        raise ValueError("Not a class file")

    def u2(pos):
        return struct.unpack_from(">H", b, pos)[0]

    cp = [None] * u2(8)
    i, p = 1, 10
    while i < len(cp):
        tag = b[p]
        p += 1
        if tag == 1:
            n = u2(p)
            cp[i] = b[p + 2:p + 2 + n]
            p += 2 + n
        elif tag == 7:
            cp[i] = u2(p)
            p += 2
        elif tag in (5, 6):
            p += 8
            i += 1
        else:
            p += {3: 4, 4: 4, 8: 2, 9: 4, 10: 4, 11: 4, 12: 4, 15: 3,
                  16: 2, 17: 4, 18: 4, 19: 2, 20: 2}[tag]
        i += 1

    def class_name(idx):
        return cp[cp[idx]].decode("ascii")

    parents = [class_name(u2(p + 4))] if u2(p + 4) else []
    count = u2(p + 6)
    parents.extend(class_name(u2(p + 8 + 2 * n)) for n in range(count))
    p += 8 + 2 * count
    out = bytearray(b[:p])
    for _ in ("fields", "methods"):
        count = u2(p)
        out.extend(b[p:p + 2])
        p += 2
        for _ in range(count):
            out.extend(b[p:p + 6])
            nattrs = u2(p + 6)
            p += 8
            attrs = []
            for _ in range(nattrs):
                end = p + 6 + struct.unpack_from(">I", b, p + 2)[0]
                if cp[u2(p)] != b"Code":
                    attrs.append(b[p:end])
                p = end
            out.extend(struct.pack(">H", len(attrs)))
            out.extend(b"".join(attrs))
    out.extend(b[p:])  # class-level attributes
    return bytes(out), parents


def make(jdk, cache, output):
    sources = {}
    with zipfile.ZipFile(jdk / "lib/ct.sym") as ct:
        for entry in ct.namelist():
            parts = entry.split("/", 2)
            if (len(parts) == 3 and "8" in parts[0] and entry.endswith(".sig")
                    and parts[1] in ("java.naming", "java.security.jgss")):
                # ct.sym stamps these *Java 8 API signatures* with the host JDK
                # classfile version. SDK 36 D8 cannot read a JDK 25 stamp (69).
                # Emit the Java 8 header for these declaration-only inputs.
                sig = ct.read(entry)
                sources[parts[2][:-4]] = sig[:4] + struct.pack(">HH", 0, 52) + sig[8:]
    cache.mkdir(parents=True, exist_ok=True)
    for artifact, expected in ARTIFACTS.items():
        cached = cache / artifact.rsplit("/", 1)[1]
        if cached.is_file():
            data = cached.read_bytes()
        else:
            with urllib.request.urlopen(MAVEN + artifact, timeout=60) as response:
                data = response.read()
        if hashlib.sha256(data).hexdigest() != expected:
            raise ValueError(f"Unexpected SHA-256 for {cached.name}")
        if not cached.exists():
            cached.write_bytes(data)
        with zipfile.ZipFile(io.BytesIO(data)) as jar:
            for entry in jar.namelist():
                if entry.endswith(".class"):
                    sources[entry[:-6]] = jar.read(entry)

    selected, pending = {}, list(REQUIRED)
    while pending:
        name = pending.pop()
        if name not in selected:
            selected[name], parents = header(sources[name])
            pending.extend(p for p in parents if p in sources and p not in selected)
    output.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(output, "w") as jar:
        for name, data in sorted(selected.items()):
            jar.writestr(zipfile.ZipInfo(name + ".class"), data)
    print(f"{output}: {len(selected)} external API declarations (no method bodies)")


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--jdk", type=Path, default=os.environ.get("JAVA_HOME"),
                     help="JDK 21 or 25; defaults to JAVA_HOME")
    cli.add_argument("--cache", type=Path, required=True, help="cache for the four published JARs")
    cli.add_argument("--out", type=Path, required=True, help="destination compileOnly JAR")
    opts = cli.parse_args()
    if opts.jdk is None:
        cli.error("supply --jdk or set JAVA_HOME")
    make(opts.jdk, opts.cache, opts.out)
