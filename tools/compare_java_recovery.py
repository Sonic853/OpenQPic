"""Compare the pre-migration APK's runtime declarations with the rebuilt Java APK."""
import argparse
from pathlib import Path
import re
import struct
from zipfile import ZipFile

APP = Path(__file__).resolve().parents[1] / "app"


def declarations(data):
    """Read DEX class_data_items, including whether a method has executable instructions."""
    def u32(at):
        return struct.unpack_from("<I", data, at)[0]

    def table(header, size):
        count, offset = struct.unpack_from("<II", data, header)
        return range(offset, offset + count * size, size)

    def uleb(pos):
        value = 0
        for i in range(5):
            b = data[pos + i]
            value |= (b & 127) << (7 * i)
            if b < 128:
                return value, pos + i + 1
        raise ValueError("Invalid DEX uleb128")

    def string(offset):
        _, start = uleb(offset)
        return data[start:data.index(0, start)].replace(b"\xc0\x80", b"\0").decode("utf-8", "surrogatepass")

    strings = [string(u32(p)) for p in table(56, 4)]
    types = [strings[u32(p)] for p in table(64, 4)]

    def type_list(offset):
        if not offset:
            return ()
        return tuple(types[i] for i in struct.unpack_from("<" + str(u32(offset)) + "H", data, offset + 4))

    protos = ["(" + "".join(type_list(u32(p + 8))) + ")" + types[u32(p + 4)] for p in table(72, 12)]
    fields = [strings[n] + ":" + types[t] for _, t, n in
              (struct.unpack_from("<HHI", data, p) for p in table(80, 8))]
    methods = [strings[n] + protos[t] for _, t, n in
               (struct.unpack_from("<HHI", data, p) for p in table(88, 8))]
    result = {}
    for p in table(96, 32):
        owner, _, parent, interfaces, _, _, pos, _ = struct.unpack_from("<8I", data, p)
        members = set()
        result[types[owner]] = (types[parent] if parent != 0xffffffff else "", type_list(interfaces), members)
        if not pos:
            continue
        counts = []
        for _ in range(4):
            count, pos = uleb(pos)
            counts.append(count)
        for kind, count in enumerate(counts):
            index = 0
            for _ in range(count):
                delta, pos = uleb(pos)
                index += delta
                flags, pos = uleb(pos)
                if kind < 2:
                    modifiers = ("static " if kind == 0 else "") + ("final " if flags & 0x10 else "")
                    modifiers += ("volatile " if flags & 0x40 else "") + ("transient " if flags & 0x80 else "")
                    members.add((modifiers + "field", fields[index]))
                else:
                    code, pos = uleb(pos)
                    form = "code" if code and u32(code + 12) else ("native" if flags & 0x100 else "abstract")
                    modifiers = ("static " if flags & 8 else "") + ("synchronized " if flags & 0x20020 else "")
                    members.add((modifiers + form, methods[index]))
    return result


def apk_types(path):
    result = {}
    with ZipFile(path) as apk:
        for name in apk.namelist():
            if re.fullmatch(r"classes\d*\.dex", name):
                entries = declarations(apk.read(name))
                if entries.keys() & result.keys():
                    raise ValueError(f"Duplicate classes in {path}")
                result.update(entries)
    return result


def compare(baseline, rebuilt):
    old, new = apk_types(baseline), apk_types(rebuilt)
    aliases = dict((row[0], row[-1]) for row in (s.split() for s in
                   (APP/"recovered-types.txt").read_text().splitlines() if s.startswith("L")))

    def renamed(signature):
        return re.sub(r"L[^;(:]+;", lambda m: aliases.get(m[0], m[0]), signature)

    missing = []
    count = dict(types=0, fields=0, methods=0, concrete=0)
    for before, after in aliases.items():
        if before not in old or after not in new:
            missing.append(f"type: {before} → {after}")
            continue
        parent, interfaces, members = old[before]
        np, ni, nm = new[after]
        actual_interfaces = set(ni)
        # SDK 36 D8 backports ExecutorService.close to this ThreadPoolExecutor
        # subclass: it adds AutoCloseable and the concrete close() companion.
        if (after == "Lcom/alensw/b/k/a;" and np == "Ljava/util/concurrent/ThreadPoolExecutor;"
                and ("code", "close()V") in nm):
            actual_interfaces.discard("Ljava/lang/AutoCloseable;")
        if renamed(parent) != np or set(map(renamed, interfaces)) != actual_interfaces:
            missing.append(f"hierarchy: {after}: {parent} {interfaces} → {np} {ni}")
        count["types"] += 1
        for kind, name in members:
            sig = renamed(name)
            if (kind, sig) not in nm:
                missing.append(f"{after}->{sig} [{kind}]")
            if "field" in kind:
                count["fields"] += 1
            else:
                count["methods"] += 1
                count["concrete"] += "code" in kind
    if missing:
        raise SystemExit(f"{len(missing)} missing or changed declarations:\n" + "\n".join(sorted(missing)))
    return count


if __name__ == "__main__":
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("baseline", type=Path, help="APK built from aa0043e before the Java migration")
    p.add_argument("rebuilt", type=Path, nargs="?", default=APP/"build/outputs/apk/debug/app-debug.apk")
    args = p.parse_args()
    print(compare(args.baseline, args.rebuilt))
