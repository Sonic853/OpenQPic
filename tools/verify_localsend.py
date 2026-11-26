"""Check gallery code, external libraries, native libraries and LocalSend in both Android packages."""
import argparse
from datetime import date
import hashlib
from io import BytesIO
import json
import os
from pathlib import Path
import re
import struct
import subprocess
import tempfile
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
APP = ROOT/"app"
PREFIX = "Lcom/alensw/transfer/"
LICENSES_URL = "https://github.com/Sonic853/OpenQPic/tree/main/licenses"
DOCUMENT_FILE_SHA = "684e0ffcb21a660c939d1bb814521f42c4bd74816f4a117291fe6a101cf3a822"
EXCLUDED_LIBRARY_RESOURCES = {
    "META-INF/DEPENDENCIES", "META-INF/LICENSE", "META-INF/NOTICE",
    "META-INF/androidx/documentfile/documentfile/LICENSE.txt",
    "org/apache/http/version.properties", "META-INF/androidx.documentfile_documentfile.version",
}


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
    strings = [string(i) for i in range(u(56))]
    types = [strings[u(u(68)+i*4)] for i in range(u(64))]
    return set(types), {types[u(u(100)+i*32)] for i in range(u(96))}, strings


def library_classes(archive, digest):
    data = archive.read_bytes()
    assert sha(data) == digest, ("unexpected Maven artifact", archive, sha(data))
    if archive.suffix == ".aar":
        with ZipFile(BytesIO(data)) as aar:
            data = aar.read("classes.jar")
    with ZipFile(BytesIO(data)) as jar:
        return {"L"+name[:-6]+";" for name in jar.namelist() if name.endswith(".class")}


def verify(apk, aab, tools, documentfile):
    pinned = json.loads((APP/"native-inputs.json").read_text())
    assert {"app/src/main/java/org/openqpic/localsend/"+n for n in
            ("Wire.java", "LocalSend.java", "LoopbackHttp.java", "HttpChannel.java", "SystemTls.java")} <= pinned.keys()
    libs = {}
    for rel, digest in pinned.items():
        data = (ROOT/rel).read_bytes()
        # Java source may have CRLF after a Windows clone.
        assert digest == sha(data) or (not rel.endswith(".so") and digest == sha(data.replace(b"\r\n", b"\n"))), rel
        if rel.endswith(".so"):
            libs["lib/"+rel.split("/jniLibs/")[1]] = digest
    assert len(libs) == 6
    licenses = ROOT/"licenses"
    assert (licenses/"README.md").is_file()
    license_files = {p.name for p in licenses.rglob("*.txt")}
    def declared(path,prefix):
        return {prefix+n for n in re.findall(r"\bnative\s+\S+\s+(\w+)\(",path.read_text("utf-8"))}
    java=APP/"src/main/java"
    image_prefix="Java_com_alensw_jni_JniUtils_"
    image=declared(java/"com/alensw/jni/JniUtils.java",image_prefix) | {image_prefix+"fuCopyFile",image_prefix+"fuSocketPair"}
    wire=declared(java/"org/openqpic/localsend/Wire.java","Java_org_openqpic_localsend_Wire_")
    assert len(image)==34 and len(wire)>=1
    # Fixed names from the last mixed build, including the JNI bridge. Keep this
    # independent of current sources so deleting a Java file cannot shrink the gate.
    inventory=[s.split() for s in (APP/"recovered-types.txt").read_text().splitlines() if s.startswith("L")]
    old={r[0] for r in inventory}
    assert len(old)==len({r[-1] for r in inventory})==len(inventory)==1472
    assert all(len(r) in (1,2) for r in inventory)
    unused={s for s in (APP/"removed-unused-library-types.txt").read_text().splitlines() if s.startswith("L")}
    assert len(unused)==587 and unused <= old
    httpcore = {t for t in old-unused if t.startswith("Lorg/apache/http/")}
    adapters = {s for s in (APP/"replaced-video-adapters.txt").read_text().splitlines() if s.startswith("L")}
    assert len(httpcore)==254 and len(adapters)==5 and adapters <= old-unused-httpcore
    replaced_video = httpcore | adapters
    removed = unused | replaced_video
    # Reflected class names (and JNI-style names) need not appear in type_ids.
    removed_names = {name for t in removed for name in (t, t[1:-1], t[1:-1].replace("/", "."))}
    expected={r[-1] for r in inventory if r[0] not in removed}
    assert len(expected)==626 and not removed & expected
    external = library_classes(documentfile, DOCUMENT_FILE_SHA)
    assert len(external)==5 and external == {t for t in expected if t.startswith("Landroidx/documentfile/")}
    assert not next((APP/"src").rglob("*.smali"),None), "Smali in product sources"
    for suffix in ("*.class","*.dex","*.jar"):
        assert not next((APP/"src").rglob(suffix),None), "Precompiled Java input: "+suffix
    classdir=APP/"build/intermediates/javac/debug/compileDebugJavaWithJavac/classes"
    javac={"L"+f.relative_to(classdir).as_posix()[:-6]+";" for f in classdir.rglob("*.class")}
    expected.update(s for s in (APP/"product-java.txt").read_text().splitlines() if s.startswith("L"))
    assert expected-external <= javac, ("not compiled from Java", sorted(expected-external-javac))
    assert not external & javac, ("external classes also compiled from source", sorted(external & javac))
    assert not removed & javac, ("removed Java classes still compiled", sorted(removed & javac))
    retired={t for t in (APP/"removed-legacy-types.txt").read_text().splitlines() if t.startswith("L")}
    assert len(retired)==775 and len(old | retired)==2247 and not retired & expected
    with ZipFile(apk) as z, ZipFile(aab) as bundle:
        assert {p for p in z.namelist() if p.startswith("lib/")} == libs.keys()
        assert {p[5:] for p in bundle.namelist() if p.startswith("base/lib/")} == libs.keys()
        for name, digest in libs.items():
            elf=z.read(name)
            assert sha(elf) == sha(bundle.read("base/"+name)) == digest, name
            check_elf(elf,name.split("/")[1],image if name.endswith("/libqpicjni156.so") else wire)
        for package in (z, bundle):
            packaged = [n for n in package.namelist() if re.search(r"(^|/)assets/licenses(/|$)", n)
                        or n.rsplit("/", 1)[-1] in license_files]
            assert not packaged, ("Repository-only license files packaged", packaged)
            assert not any(n.lower().endswith((".pem",".jks",".p12",".keystore",".key")) for n in package.namelist()), "Packaged key material"
            resources = {n.removeprefix("base/root/") for n in package.namelist()}
            assert not resources & EXCLUDED_LIBRARY_RESOURCES, resources & EXCLUDED_LIBRARY_RESOURCES
            assert not any(n.startswith("META-INF/maven/org.apache.httpcomponents/httpcore/") for n in resources)
        dex_names = {name for name in z.namelist() if re.fullmatch(r"classes\d*\.dex", name)}
        assert {name for name in bundle.namelist() if name.startswith("base/dex/") and name.endswith(".dex")} == {
            "base/dex/"+name for name in dex_names
        }
        defs, refs = set(), set()
        for name in sorted(dex_names):
            data = z.read(name)
            assert data == bundle.read("base/dex/"+name)
            types, classes, strings = dex(data)
            assert not classes & defs
            defs |= classes
            refs |= {t.lstrip("[") for t in types}
            stale_names = removed_names.intersection(strings)
            assert not stale_names, ("removed class names in DEX", sorted(stale_names))
            for stale in (b"PeerHub", b"224.0.0.251", b"CloudProvider", b"ver.q-supreme.com", b"QP-Gallery-Releases",
                          b"last_check_update", b"quickpic.authorization", b"quickpic.account", b"failed_upload_tasks", b"umeng"):
                assert stale.lower() not in data.lower(), stale
        assert expected <= defs, ("missing from DEX", sorted(expected-defs))
        assert {"Ljavax/net/ssl/SSLSocket;","Ljavax/net/ssl/SSLServerSocket;",
                "Landroid/security/keystore/KeyGenParameterSpec$Builder;","Landroid/media/MediaDataSource;"} <= refs
        program = javac | external
        # AGP generates an empty R class for the resource-free DocumentFile AAR.
        unexpected={d for d in defs-program if d != "Landroidx/documentfile/R;"
                    and not re.fullmatch(r"Lcom/alensw/PicFolder/R(?:\$\w+)?;", d)
                    and not ("$$ExternalSynthetic" in d and d.split("$$ExternalSynthetic")[0]+";" in program)}
        assert not unexpected, ("DEX classes not traceable to source, Maven libraries, D8 or R", sorted(unexpected))
        assert not {t for t in refs if t.startswith(PREFIX)} - {d for d in defs if d.startswith(PREFIX)}
        forbidden=retired | removed | {r[0] for r in inventory if len(r)==2}
        assert not forbidden & (defs|refs), forbidden & (defs|refs)
        assert not any(t.startswith(("Lorg/apache/http/", "Landroidx/core/", "Lcom/alensw/cloud/",
                                      "Lb/a/", "Lcom/c/a/")) for t in refs)
        api=b"https://api.github.com/repos/Sonic853/OpenQPic/releases/latest"
        assert any(api in z.read(n) for n in z.namelist() if re.fullmatch(r"classes\d*\.dex",n))
    def run(*args):
        return subprocess.check_output([str(x) for x in args], text=True, encoding="utf-8", errors="replace")
    aapt = tools/("aapt2.exe" if os.name=="nt" else "aapt2")
    def check_sdk(package):
        manifest=run(aapt,"dump","xmltree",package,"--file","AndroidManifest.xml")
        sdk,=re.findall(r"(?m)^\s*E: uses-sdk\b[^\n]*\n((?:[ \t]*A: [^\n]*\n)+)",manifest)
        for key,value in (("minSdkVersion",23),("targetSdkVersion",32)):
            assert re.search(r"android:"+key+r"\(0x[0-9a-f]+\)="+str(value)+r"\b",sdk),(package,key,sdk)
        return manifest
    manifest = check_sdk(apk)
    def version(package):
        badging=run(aapt,"dump","badging",package).splitlines()[0]
        code, tag = re.search(r"\bversionCode='(\d+)' versionName='([^']+)'",badging).groups()
        day, _, patch = tag.partition("p")
        assert re.fullmatch(r"\d{4}\.\d{2}\.\d{2}(?:p[1-9]\d*)?",tag)
        expected_code=10000000+(date.fromisoformat(day.replace(".","-"))-date(2020,1,1)).days*65536+int(patch or 0)
        assert int(code)==expected_code and 9700000<int(code)<=2100000000
        return tag, int(code)
    tag,code=version(apk)
    assert re.search(r"(?m)^application-label-zh-CN:'快图'$",run(aapt,"dump","badging",apk))
    assert "android.permission.WRITE_SYNC_SETTINGS" not in manifest and "android.permission.REQUEST_INSTALL_PACKAGES" not in manifest
    for s in ["com.alensw.transfer.TransferActivity","com.alensw.transfer.TransferSessionService",
              "android.permission.FOREGROUND_SERVICE_DATA_SYNC","com.alensw.video.VideoProxyService","android.intent.action.SEND_MULTIPLE",".StreamingActivity"]:
        assert s in manifest, s
    assert "com.alensw.transfer.TransferService" not in manifest
    services=re.findall(r"(?m)^\s*E: service[^\n]*\n((?:[ \t]*A: [^\n]*\n)+)",manifest)
    transfer,=[block for block in services if '"com.alensw.transfer.TransferSessionService"' in block]
    for attribute,value in (("exported","false"),("stopWithTask","false"),("foregroundServiceType","0x00000001")):
        assert re.search(r"android:"+attribute+r"\(0x[0-9a-f]+\)="+value+r"\b",transfer),attribute
    resources=run(aapt,"dump","resources",apk)
    assert re.search(r'resource \S+ string/version\b[^\n]*\n[^\n]*"'+re.escape(tag)+r'"',resources)
    table = dict((name,n) for n,name in re.findall(r"^\s*resource (0x[0-9a-f]+) (\S+)",resources,re.M))
    stable = dict(x.split(":")[1].split(" = ") for x in (APP/"resource-ids.txt").read_text().splitlines())
    assert stable.items() <= table.items()
    update_labels={"string/update_"+s for s in ("no_release","check_failed","view_release","open_failed")}
    about_labels={"string/about_"+s for s in ("sonic853","sonic853_email","sonic853_card","sonic853_github",
                   "sonic853_bilibili","sonic853_x","v97_developer","contact","send_email","profile_card","follow",
                   "bilibili","open_source_licenses","open_source_licenses_url")}
    product_resources=update_labels | about_labels | {"drawable/ic_bilibili","string/share_other"}
    assert product_resources <= table.keys()
    assert all(re.fullmatch(r"(string|id|layout|drawable)/ls_\w+",k) or k in product_resources for k in table.keys()-stable.keys())
    symbols = (APP/"build/intermediates/runtime_symbol_list/debug/processDebugResources/R.txt").read_text()
    for k in table.keys()-stable.keys():
        assert "int "+k.replace("/"," ")+" "+table[k] in symbols, k
    def check_license_link(package, resource_dump):
        label = "string/about_open_source_licenses"
        url = label + "_url"
        assert re.search(fr'resource {table[url]} {url}[^\n]*\n[^\n]*"{re.escape(LICENSES_URL)}"', resource_dump)
        for variant in ("xml", "xml-v28", "xml-v33"):
            tree = run(aapt,"dump","xmltree",package,"--file",f"res/{variant}/main_pref.xml")
            about, = [node for node in re.split(r"(?m)^ {6}E: ", tree)
                      if f")=@{table['string/about']}\n" in node]
            entry, = [node for node in re.split(r"(?m)^ {10}E: ", about)
                      if f")=@{table[label]}\n" in node]
            assert entry.startswith("Preference ") and "E: intent " in entry, (package, variant)
            assert '"android.intent.action.VIEW"' in entry and f")=@{table[url]}\n" in entry, (package, variant)
    check_license_link(apk, resources)
    run(tools/("zipalign.exe" if os.name=="nt" else "zipalign"),"-c","4",apk)
    java = Path(os.environ["JAVA_HOME"])/"bin/java" if "JAVA_HOME" in os.environ else "java"
    javac_exe = Path(java).with_name("javac")
    with tempfile.TemporaryDirectory() as temp:
        run(javac_exe,"-d",temp,ROOT/"buildSrc/src/main/java/org/openqpic/version/GalleryVersion.java",
            ROOT/"tools/version/ReleaseVersion.java",ROOT/"tools/version/VersionTest.java")
        run(java,"-ea","-cp",temp,"VersionTest")
        with ZipFile(aab) as bundle, ZipFile(Path(temp)/"bundle.zip","w") as proto:
            for n in bundle.namelist():
                if n=="base/manifest/AndroidManifest.xml": proto.writestr("AndroidManifest.xml",bundle.read(n))
                if n=="base/resources.pb" or n.startswith("base/res/"): proto.writestr(n[5:],bundle.read(n))
        binary=Path(temp)/"bundle.apk"
        run(aapt,"convert","--output-format","binary","-o",binary,Path(temp)/"bundle.zip")
        assert version(binary)==(tag,code)
        check_sdk(binary)
        check_license_link(binary, run(aapt,"dump","resources",binary))
    run(java,"-Xmx128m","-XX:+UseSerialGC","-jar",tools/"lib/apksigner.jar","verify",apk)
    return {"apk_sha256":sha(apk.read_bytes()), "bytes":apk.stat().st_size, "dex_classes":len(defs),
            "java_compiled_classes":len(javac),"retained_gallery_types":len(old-removed),
            "external_library_types":len(external),
            "versionName":tag,"versionCode":code,"minSdk":23,"targetSdk":32,"retired_legacy_types":len(retired),
            "removed_unused_library_types":len(unused),
            "replaced_video_server_types":len(replaced_video),
            "original_resource_ids":len(stable),"native_sha256":libs,"licenses_url":LICENSES_URL,
            "packaged_license_files":0}


if __name__ == "__main__":
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--build-tools", type=Path, required=True)
    ap.add_argument("--documentfile", type=Path, required=True)
    args = ap.parse_args()
    data = verify(APP/"build/outputs/apk/debug/app-debug.apk",APP/"build/outputs/bundle/debug/app-debug.aab",
                  args.build_tools,args.documentfile)
    report = APP/"build/reports/localsend-debug.json"
    report.parent.mkdir(parents=True,exist_ok=True)
    report.write_text(json.dumps(data,indent=2)+"\n","utf-8")
    print(report, data["bytes"], data["dex_classes"])
