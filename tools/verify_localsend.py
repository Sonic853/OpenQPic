"""Verify the shipped LocalSend product (distinct from the original stable equivalence gate)."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import struct
import subprocess
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
APP = ROOT/"app"
PREFIX = "Lcom/alensw/transfer/"


def sha(data):
    return hashlib.sha256(data).hexdigest()


def check_elf(data, abi, methods):
    wide = data[4] == 2
    assert data[:4] == b"\x7fELF" and data[5] == 1
    assert (data[4],struct.unpack_from("<H",data,18)[0]) == {
        "armeabi":(1,40),"x86":(1,3),"arm64-v8a":(2,183)}[abi]
    shoff, = struct.unpack_from("<Q" if wide else "<I", data, 40 if wide else 32)
    ent,n = struct.unpack_from("<HH", data, 58 if wide else 46)
    sections = [struct.unpack_from("<IIQQQQIIQQ" if wide else "<10I",data,shoff+i*ent) for i in range(n)]
    exports=set()
    for sec in sections:
        if sec[1]!=11: continue  # SHT_DYNSYM, not arbitrary strings in the file
        st=sections[sec[6]]; strings=data[st[4]:st[4]+st[5]]
        for pos in range(sec[4],sec[4]+sec[5],sec[9]):
            if wide: index,info,other,owner,_,_=struct.unpack_from("<IBBHQQ",data,pos)
            else: index,_,_,info,other,owner=struct.unpack_from("<IIIBBH",data,pos)
            if owner and info>>4 in (1,2) and info&15==2 and other&3 in (0,3):
                name=strings[index:strings.index(b"\0",index)].decode("ascii")
                if name.startswith("Java_"): exports.add(name)
    assert exports == methods, (abi, exports ^ methods)
    if wide:
        phoff,=struct.unpack_from("<Q",data,32); ent,n=struct.unpack_from("<HH",data,54)
        loads=[p for i in range(n) if (p:=struct.unpack_from("<II6Q",data,phoff+i*ent))[0]==1]
        assert loads and all(p[7]>=16384 and (p[2]-p[3])%16384==0 for p in loads)


def dex(data):
    def u(pos):
        return struct.unpack_from("<I", data, pos)[0]
    def string(n):
        p = u(u(60)+n*4)
        while data[p] & 128:
            p += 1
        p += 1
        return data[p:data.index(b"\0", p)].decode("utf-8", "replace")
    types = [string(u(u(68)+i*4)) for i in range(u(64))]
    return set(types), {types[u(u(100)+i*32)] for i in range(u(96))}


def verify(apk, aab, tools):
    pinned = json.loads((APP/"native-inputs.json").read_text())
    libs = {}
    for rel, digest in pinned.items():
        data = (ROOT/rel).read_bytes()
        # Java source may have CRLF after a Windows clone.
        assert digest == sha(data) or (not rel.endswith(".so") and digest == sha(data.replace(b"\r\n", b"\n"))), rel
        if rel.endswith(".so"):
            libs["lib/"+rel.split("/jniLibs/")[1]] = digest
    assert len(libs) == 6
    def declared(path,prefix):
        return {prefix+n for n in re.findall(r"\bnative\s+\S+\s+(\w+)\(",path.read_text("utf-8"))}
    java=APP/"src/main/java"
    image_prefix="Java_com_alensw_jni_JniUtils_"
    image=declared(java/"com/alensw/jni/JniUtils.java",image_prefix) | {image_prefix+"fuCopyFile",image_prefix+"fuSocketPair"}
    wire=declared(java/"org/openqpic/localsend/Wire.java","Java_org_openqpic_localsend_Wire_")
    assert len(image)==34 and len(wire)>=1
    expected = {f.read_text("utf-8").splitlines()[0].split()[-1] for f in (APP/"src/main/smali").rglob("*.smali")}
    expected.add("Lcom/alensw/jni/JniUtils;")
    expected.update(s for s in (APP/"product-java.txt").read_text().splitlines() if s.startswith("L"))
    # An accidentally deleted unrelated gallery class must fail even if it is
    # missing from both the edited source tree and the resulting APK.
    abi_jar=APP/"compileOnly/stable-dex-abi.jar"
    assert sha(abi_jar.read_bytes())=="c81c7e8312e73aa8abd6f7fadd12efed2875d71d6fc6ebd5eb5e61b828e6957f"
    with ZipFile(abi_jar) as headers:
        old={"L"+n[:-6]+";" for n in headers.namelist() if n.endswith(".class")}
    preserved={t for t in old if not t.startswith(PREFIX)}
    assert len(preserved)==2247 and preserved <= expected
    with ZipFile(apk) as z, ZipFile(aab) as bundle:
        assert {p for p in z.namelist() if p.startswith("lib/")} == libs.keys()
        assert {p[5:] for p in bundle.namelist() if p.startswith("base/lib/")} == libs.keys()
        for name, digest in libs.items():
            elf=z.read(name)
            assert sha(elf) == sha(bundle.read("base/"+name)) == digest, name
            check_elf(elf,name.split("/")[1],image if name.endswith("/libqpicjni156.so") else wire)
        notice="assets/licenses/picohttpparser.txt"
        assert b"Copyright" in z.read(notice) and z.read(notice)==bundle.read("base/"+notice)
        defs, refs = set(), set()
        for name in z.namelist():
            if not re.fullmatch(r"classes\d*\.dex", name):
                continue
            data = z.read(name)
            assert data == bundle.read("base/dex/"+name)
            types, classes = dex(data)
            assert not classes & defs
            defs |= classes
            refs |= {t.lstrip("[") for t in types}
            assert b"PeerHub" not in data and b"224.0.0.251" not in data
        generated = defs - expected
        assert not expected - defs, expected - defs
        assert all("$$ExternalSynthetic" in d and d.startswith((PREFIX, "Lcom/alensw/ui/c/ReceivedMediaUpdates$$ExternalSynthetic",
                   "Lcom/alensw/video/", "Lorg/openqpic/localsend/"))
                   for d in generated), generated
        assert not {t for t in refs if t.startswith(PREFIX)} - {d for d in defs if d.startswith(PREFIX)}
        removed={t for t in old if t.startswith(PREFIX) and t!=PREFIX+"TransferActivity;"}
        assert not removed & (defs|refs), removed & (defs|refs)
    def run(*args):
        return subprocess.check_output([str(x) for x in args], text=True, encoding="utf-8", errors="replace")
    aapt = tools/("aapt2.exe" if os.name=="nt" else "aapt2")
    manifest = run(aapt,"dump","xmltree",apk,"--file","AndroidManifest.xml")
    for s in ["com.alensw.transfer.TransferActivity","com.alensw.transfer.TransferSessionService",
              "android.permission.FOREGROUND_SERVICE_DATA_SYNC","com.alensw.video.VideoProxyService","android.intent.action.SEND_MULTIPLE",".StreamingActivity"]:
        assert s in manifest, s
    assert "com.alensw.transfer.TransferService" not in manifest
    services=re.findall(r"(?m)^\s*E: service[^\n]*\n((?:[ \t]*A: [^\n]*\n)+)",manifest)
    transfer,=[block for block in services if '"com.alensw.transfer.TransferSessionService"' in block]
    for attribute,value in (("exported","false"),("stopWithTask","false"),("foregroundServiceType","0x00000001")):
        assert re.search(r"android:"+attribute+r"\(0x[0-9a-f]+\)="+value+r"\b",transfer),attribute
    table = dict((name,n) for n,name in re.findall(r"^\s*resource (0x[0-9a-f]+) (\S+)",run(aapt,"dump","resources",apk),re.M))
    stable = dict(x.split(":")[1].split(" = ") for x in (APP/"resource-ids.txt").read_text().splitlines())
    assert stable.items() <= table.items()
    assert all(re.fullmatch(r"(string|id|layout|drawable)/ls_\w+",k) for k in table.keys()-stable.keys())
    symbols = (APP/"build/intermediates/runtime_symbol_list/debug/processDebugResources/R.txt").read_text()
    for k in table.keys()-stable.keys():
        assert "int "+k.replace("/"," ")+" "+table[k] in symbols, k
    run(tools/("zipalign.exe" if os.name=="nt" else "zipalign"),"-c","4",apk)
    java = Path(os.environ["JAVA_HOME"])/"bin/java" if "JAVA_HOME" in os.environ else "java"
    run(java,"-Xmx128m","-XX:+UseSerialGC","-jar",tools/"lib/apksigner.jar","verify",apk)
    return {"apk_sha256":sha(apk.read_bytes()), "bytes":apk.stat().st_size, "dex_classes":len(defs),
            "original_resource_ids":len(stable),"native_sha256":libs}


if __name__ == "__main__":
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--build-tools", type=Path, required=True)
    args = ap.parse_args()
    data = verify(APP/"build/outputs/apk/debug/app-debug.apk",APP/"build/outputs/bundle/debug/app-debug.aab",args.build_tools)
    report = APP/"build/reports/localsend-debug.json"
    report.parent.mkdir(parents=True,exist_ok=True)
    report.write_text(json.dumps(data,indent=2)+"\n","utf-8")
    print(report, data["bytes"], data["dex_classes"])
