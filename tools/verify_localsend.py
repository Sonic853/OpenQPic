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
import xml.etree.ElementTree as ET
from zipfile import ZIP_DEFLATED, ZipFile

ROOT = Path(__file__).resolve().parents[1]
APP = ROOT/"app"
PREFIX = "Lcom/alensw/transfer/"
LICENSES_URL = "https://github.com/Sonic853/OpenQPic/tree/main/licenses"
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


def check_file_provider_api(data, tools):
    # Check the actual public method definitions; neither overload needs a call site in the app.
    dexdump = tools / ("dexdump.exe" if os.name == "nt" else "dexdump")
    with tempfile.TemporaryDirectory() as temp:
        dex_file = Path(temp) / "provider.dex"
        dex_file.write_bytes(data)
        api = ET.fromstring(subprocess.check_output([dexdump, "-e", "-l", "xml", dex_file]))
    methods = api.findall("./package[@name='androidx.core.content']/class[@name='FileProvider']"
                          "/method[@name='getUriForFile'][@visibility='public'][@static='true'][@return='android.net.Uri']")
    signatures = {tuple(p.get("type") for p in m.findall("parameter")) for m in methods}
    args = ("android.content.Context", "java.lang.String", "java.io.File")
    assert {args, (*args, "java.lang.String")} <= signatures, ("missing FileProvider.getUriForFile overload", signatures)


def library_inventory(archives):
    pinned = {coord: digest for digest, coord in
              (line.split() for line in (APP/"runtime-libraries.sha256").read_text().splitlines())}
    assert archives.keys() == pinned.keys(), ("runtime dependency change", archives.keys() ^ pinned.keys())
    classes, r_classes, resources = {}, set(), set()
    for coord, file in archives.items():
        archive = Path(file)
        data = archive.read_bytes()
        assert sha(data) == pinned[coord], ("unexpected Maven artifact", coord)
        if archive.suffix == ".aar":
            with ZipFile(BytesIO(data)) as aar:
                namespace = ET.fromstring(aar.read("AndroidManifest.xml")).get("package")
                r_classes.add("L" + namespace.replace(".", "/") + "/R")
                for line in aar.read("R.txt").decode().splitlines():
                    kind, resource_type, name, *_ = line.split()
                    if kind == "int" and resource_type != "styleable":
                        resources.add(resource_type + "/" + name)
                data = aar.read("classes.jar")
        with ZipFile(BytesIO(data)) as jar:
            classes[coord] = {"L"+n[:-6]+";" for n in jar.namelist()
                              if n.endswith(".class") and not n.startswith("META-INF/")}
    return classes, r_classes, resources


def verify(apk, aab, tools, archives, variant="debug"):
    release = variant == "release"
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
    assert all((licenses/name).is_file() for name in ("android-support-apache-2.0.txt", "android-support.txt", "jvm-libraries.txt"))
    license_files = {p.name for p in licenses.rglob("*.txt")}
    def declared(path,prefix):
        return {prefix+n for n in re.findall(r"\bnative\s+\S+\s+(\w+)\(",path.read_text("utf-8"))}
    java=APP/"src/main/java"
    image_prefix="Java_com_alensw_jni_JniUtils_"
    image=declared(java/"com/alensw/jni/JniUtils.java",image_prefix) | {image_prefix+"fuCopyFile",image_prefix+"fuSocketPair"}
    wire=declared(java/"org/openqpic/localsend/Wire.java","Java_org_openqpic_localsend_Wire_")
    assert len(image)==39 and len(wire)>=1
    # Fixed names from the last mixed build, including the JNI bridge. Keep this
    # independent of current sources so deleting a Java file cannot shrink the gate.
    inventory=[s.split() for s in (APP/"recovered-types.txt").read_text().splitlines() if s.startswith("L")]
    old={r[0] for r in inventory}
    assert len(old)==len({r[-1] for r in inventory})==len(inventory)==1472
    assert all(len(r) in (1,2) for r in inventory)
    unused={s for s in (APP/"removed-unused-library-types.txt").read_text().splitlines() if s.startswith("L")}
    assert len(unused)==587 and unused <= old
    libraries, library_r, library_resources = library_inventory(archives)
    external = set.union(*libraries.values())
    core = libraries["androidx.core:core:1.17.0"]
    support = {t for t in old if t.startswith("Landroid/support/")}
    assert len(support) == 60 and not support & external
    restored_by_core = unused & core
    assert all(t.startswith("Landroidx/core/") for t in restored_by_core)
    httpcore = {t for t in old-unused if t.startswith("Lorg/apache/http/")}
    adapters = {s for s in (APP/"replaced-video-adapters.txt").read_text().splitlines() if s.startswith("L")}
    assert len(httpcore)==254 and len(adapters)==5 and adapters <= old-unused-httpcore
    replaced_video = httpcore | adapters
    removed = (unused - restored_by_core) | replaced_video | support
    # Reflected class names (and JNI-style names) need not appear in type_ids.
    removed_names = {name for t in removed for name in (t, t[1:-1], t[1:-1].replace("/", "."))}
    replaced = unused | replaced_video | support
    gallery = {r[-1] for r in inventory if r[0] not in replaced}
    assert len(gallery)==566 and not removed & gallery
    assert gallery & external == libraries["androidx.documentfile:documentfile:1.1.0"]
    provider = "Landroidx/core/content/FileProvider;"
    if release:
        # Only Release shrinks Core and its transitive dependencies.
        expected = gallery | {provider, "Landroidx/core/app/CoreComponentFactory;",
                              "Landroidx/core/provider/DocumentsContractCompat;", "Landroidx/core/view/ViewCompat;"}
        expected.update(libraries["androidx.drawerlayout:drawerlayout:1.2.0"])
        expected.update(libraries["androidx.viewpager:viewpager:1.1.0"])
    else:
        expected = gallery | external
    assert not next((APP/"src").rglob("*.smali"),None), "Smali in product sources"
    for suffix in ("*.class","*.dex","*.jar"):
        assert not next((APP/"src").rglob(suffix),None), "Precompiled Java input: "+suffix
    classdir=APP/f"build/intermediates/javac/{variant}/compile{variant.capitalize()}JavaWithJavac/classes"
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
            if release:
                assert z.getinfo(name).compress_type == ZIP_DEFLATED, ("uncompressed release JNI", name)
            check_elf(elf,name.split("/")[1],image if name.endswith("/libqpicjni156.so") else wire)
        for package in (z, bundle):
            packaged = [n for n in package.namelist() if re.search(r"(^|/)(licenses?|notice|copying)([./_-]|$)", n, re.I)
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
            if release and provider in classes:
                check_file_provider_api(data, tools)
            assert not classes & defs
            defs |= classes
            refs |= {t.lstrip("[") for t in types}
            stale_names = removed_names.intersection(strings)
            assert not stale_names, ("removed class names in DEX", sorted(stale_names))
            for stale in (b"PeerHub", b"224.0.0.251", b"CloudProvider", b"ver.q-supreme.com", b"QP-Gallery-Releases",
                          b"last_check_update", b"quickpic.authorization", b"quickpic.account", b"failed_upload_tasks", b"umeng"):
                assert stale.lower() not in data.lower(), stale
        assert expected <= defs, ("missing from DEX", sorted(expected-defs))
        if release:
            assert core - defs, "Release packaged the entire Core library"
            assert (refs & external) <= defs, ("dangling library references", (refs & external) - defs)
        assert {"Ljavax/net/ssl/SSLSocket;","Ljavax/net/ssl/SSLServerSocket;",
                "Landroid/security/keystore/KeyGenParameterSpec$Builder;","Landroid/media/MediaDataSource;"} <= refs
        program = javac | external
        generated_r = library_r | {"Lcom/alensw/PicFolder/R"}
        unexpected={d for d in defs-program if d.rstrip(";").split("$")[0] not in generated_r
                    and not ("$$ExternalSynthetic" in d and d.split("$$ExternalSynthetic")[0]+";" in program)
                    and not (d.endswith("$-CC;") and d.removesuffix("$-CC;")+";" in program)}
        if release:
            # R8 supplies the API 28 superclass of CoreComponentFactory for older devices.
            unexpected.discard("Landroid/app/AppComponentFactory;")
        assert not unexpected, ("DEX classes not traceable to source, Maven libraries, D8 or R", sorted(unexpected))
        assert not {t for t in refs if t.startswith(PREFIX)} - {d for d in defs if d.startswith(PREFIX)}
        forbidden=retired | removed | {r[0] for r in inventory if len(r)==2}
        assert not forbidden & (defs|refs), forbidden & (defs|refs)
        # Include newer product helpers and anonymous classes moved with the recovered packages.
        old_packages = ("Lcom/alensw/a/", "Lcom/alensw/b/", "Lcom/b/a/",
                        "Lcom/alensw/ui/a/", "Lcom/alensw/ui/b/", "Lcom/alensw/ui/c/", "Lcom/alensw/ui/d/")
        stale_types = {t for t in defs | refs if t.startswith(old_packages)}
        assert not stale_types, ("obfuscated gallery package in DEX", sorted(stale_types))
        # AndroidX Core publishes a few legacy Binder/Parcelizer binary names.
        assert {t for t in refs if t.startswith("Landroid/support/")} == {t for t in core & defs if t.startswith("Landroid/support/")}
        assert not any(t.startswith(("Lorg/apache/http/", "Lcom/alensw/cloud/",
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
    for name in ("androidx.core.content.FileProvider", "android.support.FILE_PROVIDER_PATHS",
                 "com.alensw.PicFolder.provider", "com.alensw.PicFolder.FileProvider"):
        assert f'"{name}"' in manifest, name
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
    def resource_ids(dump):
        return {name: n for n, name in re.findall(r"^\s*resource (0x[0-9a-f]+) (\S+)", dump, re.M)}
    table = resource_ids(resources)
    stable = dict(x.split(":")[1].split(" = ") for x in (APP/"resource-ids.txt").read_text().splitlines())
    declared = {e.get("type")+"/"+e.get("name"): e.get("id")
                for e in ET.parse(APP/"src/main/res/values/public.xml").getroot().findall("public")}
    assert declared.items() <= stable.items() and len(stable) >= 995
    assert stable.items() <= table.items()
    update_labels={"string/update_"+s for s in ("no_release","check_failed","view_release","open_failed")}
    about_labels={"string/about_"+s for s in ("sonic853","sonic853_email","sonic853_card","sonic853_github",
                   "sonic853_bilibili","sonic853_x","v97_developer","contact","send_email","profile_card","follow",
                   "bilibili","open_source_licenses","open_source_licenses_url")}
    viewer_resources = {"id/viewer_details_overlay", "id/viewer_details_sheet",
                        "string/gif_system_decoder", "string/gif_system_decoder_summary",
                        "string/viewer_ignore_gif_loop_limit",
                        "array/viewer_swipe_actions", "array/viewer_swipe_values"} | {
                            "string/viewer_swipe_" + action for action in ("up", "down", "none", "details", "back")}
    scan_labels = {"string/enhanced_scan", "string/enhanced_scan_summary"}
    product_resources=update_labels | about_labels | viewer_resources | scan_labels | {"drawable/ic_bilibili","string/share_other","plurals/ls_cancel_sending_files"}
    original = declared.keys() - product_resources
    assert len(original) == 918
    assert product_resources <= table.keys()
    extras = {k.replace(".", "_") for k in table.keys()-stable.keys()} - library_resources - product_resources
    assert all(re.fullmatch(r"(string|id|layout|drawable)/ls_\w+", k) for k in extras), extras
    symbols = (APP/f"build/intermediates/runtime_symbol_list/{variant}/process{variant.capitalize()}Resources/R.txt").read_text()
    java_ids = {kind+"/"+name: value for kind, name, value in re.findall(r"^int (\w+) (\S+) (0x[0-9a-f]+)$", symbols, re.M)}
    aliases = {"string/"+word: "string/stable_keyword_"+word for word in ("default", "interface")}
    projected = {aliases.get(k, k.replace(".", "_")): value for k, value in table.items()}
    assert projected == java_ids, ("Java and APK resource IDs differ", projected.items() ^ java_ids.items())
    assert library_resources <= java_ids.keys(), ("missing AAR resources", library_resources - java_ids.keys())
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
        bundle_resources = run(aapt,"dump","resources",binary)
        assert resource_ids(bundle_resources) == table, "AAB and APK resource IDs differ"
        check_license_link(binary, bundle_resources)
    if not release:  # Release is signed after artifact verification.
        run(java,"-Xmx128m","-XX:+UseSerialGC","-jar",tools/"lib/apksigner.jar","verify",apk)
    return {"apk_sha256":sha(apk.read_bytes()), "bytes":apk.stat().st_size, "dex_classes":len(defs),
            "aab_bytes":aab.stat().st_size, "resource_ids":len(table),
            "java_compiled_classes":len(javac),"retained_gallery_types":len(gallery),
            "external_library_types":len(defs & external),
            "maven_artifacts":len(libraries), "core_types":len(defs & core), "replaced_support_types":len(support),
            "versionName":tag,"versionCode":code,"minSdk":23,"targetSdk":32,"retired_legacy_types":len(retired),
            "removed_unused_library_types":len(unused-defs),
            "replaced_video_server_types":len(replaced_video),
            "original_resource_ids":len(original), "pre_androidx_resource_ids":len(stable),
            "native_sha256":libs,"licenses_url":LICENSES_URL,
            "packaged_license_files":0}


if __name__ == "__main__":
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--variant", choices=("debug", "release"), default="debug")
    ap.add_argument("--build-tools", type=Path, required=True)
    ap.add_argument("--library", nargs=2, action="append", required=True, metavar=("COORDINATE", "ARCHIVE"))
    args = ap.parse_args()
    variant = args.variant
    apk_name = "app-release-unsigned.apk" if variant == "release" else "app-debug.apk"
    data = verify(APP/f"build/outputs/apk/{variant}"/apk_name, APP/f"build/outputs/bundle/{variant}/app-{variant}.aab",
                  args.build_tools, dict(args.library), variant)
    report = APP/f"build/reports/localsend-{variant}.json"
    report.parent.mkdir(parents=True,exist_ok=True)
    report.write_text(json.dumps(data,indent=2)+"\n","utf-8")
    print(report, data["bytes"], data["dex_classes"])
