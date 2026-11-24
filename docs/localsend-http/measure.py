"""Size-only A/B builds in a disposable checkout. No app is installed or started."""
import argparse
import hashlib
import io
import json
import os
from pathlib import Path
import shutil
import struct
import subprocess
import tempfile
import urllib.request
from zipfile import ZipFile

APP = Path(__file__).resolve().parents[2]
PICO = "dd573d8fa9264c646a400e96ce9f07dea6f156e4"
PIN = {"picohttpparser.c":"8aacb701e60a3a5c600f51845d9554463773eafed88f9c4f47472ed5dac6a942",
       "picohttpparser.h":"1fc9074dd12418b2b91e55ef3a8279bccf8d3577b790dea6f2f908ab2a157f1c"}
ABIS = ("armeabi", "x86")
SHA = lambda b: hashlib.sha256(b).hexdigest()


def run(args, cwd, env=None):
    r = subprocess.run(list(map(str,args)),cwd=cwd,env=env,stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
    if r.returncode:
        raise RuntimeError(r.stdout.decode("utf8","replace")[-6500:])
    return r.stdout


def snapshot(apk):
    result = {"bytes":apk.stat().st_size,"sha256":SHA(apk.read_bytes()),"dex":{},"libs":{}}
    with ZipFile(apk) as z:
        for i in z.infolist():
            if not i.filename.endswith((".dex",".so")):
                continue
            data = z.read(i)
            r = {"raw":len(data),"zip":i.compress_size,"sha256":SHA(data)}
            if i.filename.endswith(".dex"):
                r["class_defs"] = struct.unpack_from("<I",data,96)[0]
                result["dex"][i.filename] = r
            else:
                result["libs"][i.filename] = r
    return result


def parser_fixtures(t, ndk16, ndk64, supplied):
    d=t/"parser"; d.mkdir()
    for name, digest in PIN.items():
        data = (supplied/name).read_bytes() if supplied else urllib.request.urlopen(
            f"https://raw.githubusercontent.com/h2o/picohttpparser/{PICO}/{name}",timeout=35).read()
        if SHA(data)!=digest: raise ValueError("parser source hash: "+name)
        (d/name).write_bytes(data)
    # Build the parser itself, not a transfer implementation. Public entry points retain its
    # real request, response, header and chunk decoder code in the stripped shared object.
    (d/"floor.c").write_text("int wire_size_floor(void) { return 1; }\n")
    (d/"Application.mk").write_text("APP_PLATFORM := android-23\nAPP_STL := none\nAPP_OPTIM := release\n")
    paths, sizes = {}, {}
    for abi in (*ABIS, *(("arm64-v8a",) if ndk64 else ())):
        ndk=ndk64 if abi=="arm64-v8a" else ndk16
        sizes[abi]={}
        for kind,src in (("elf_floor","floor.c"),("http_parser","picohttpparser.c")):
            (d/"Android.mk").write_text("LOCAL_PATH := $(call my-dir)\ninclude $(CLEAR_VARS)\n"
                "LOCAL_MODULE := qpicwirecost\nLOCAL_SRC_FILES := "+src+"\nLOCAL_CFLAGS := -Os -ffunction-sections -fdata-sections\n"
                "LOCAL_LDFLAGS := -Wl,--gc-sections"+(" -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384" if abi=="arm64-v8a" else "")+
                "\ninclude $(BUILD_SHARED_LIBRARY)\n")
            out=t/(kind+"-"+abi)
            run([ndk/("ndk-build.cmd" if os.name=="nt" else "ndk-build"),"NDK_PROJECT_PATH=.",
                 "APP_BUILD_SCRIPT=Android.mk","NDK_APPLICATION_MK=Application.mk","APP_ABI="+abi,
                 "NDK_OUT="+str(out/"obj"),"NDK_LIBS_OUT="+str(out/"libs")],d)
            elf=out/"libs"/abi/"libqpicwirecost.so"
            raw=elf.read_bytes()
            if kind=="http_parser":
                if not all((s+b"\x00") in raw for s in (b"phr_parse_request",b"phr_parse_response",b"phr_decode_chunked")):
                    raise ValueError("parser functions not present")
                paths[abi]=elf
            sizes[abi][kind]=len(raw)
    return paths,sizes


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--app",type=Path,default=APP)
    p.add_argument("--jni",type=Path,default=APP.parent/"OpenQPicJni")
    p.add_argument("--sdk",type=Path,required=True)
    p.add_argument("--gradle-cache",type=Path,required=True)
    p.add_argument("--ndk16",type=Path,required=True)
    p.add_argument("--ndk64",type=Path,help="also measure the arm64 parser and packaging of the new arm64 image-library port")
    p.add_argument("--pico-dir",type=Path,help="offline source directory for the pinned two parser files")
    p.add_argument("--out",type=Path,default=Path(__file__).with_name("size-study.json"))
    a=p.parse_args()
    rev=lambda r:run(["git","rev-parse","HEAD"],r).decode().strip()
    env=os.environ.copy()
    env.update(ANDROID_HOME=str(a.sdk.resolve()),ANDROID_SDK_ROOT=str(a.sdk.resolve()),
               GRADLE_USER_HOME=str(a.gradle_cache.resolve()))
    env.pop("OPENPIC_RECOVERY_ROOT",None)
    report={"app_commit":rev(a.app),"jni_commit":rev(a.jni),"units":"bytes","pico_commit":PICO,"pico_sources_sha256":PIN,
            "release_signing":"same throwaway RSA key and apksigner for all release rows of this run",
            "scope":"APK and stripped parser costs; deletion-only / parser fixtures are not runnable products",
            "abis":list(ABIS),"variants":{}}
    ndks=[a.ndk16]+([a.ndk64] if a.ndk64 else [])
    report["ndks"]=[next(l.split("=",1)[1].strip() for l in (d/"source.properties").read_text().splitlines()
                         if l.startswith("Pkg.Revision")) for d in ndks]
    parent=a.app/"build"; parent.mkdir(exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="wifi-size-",dir=parent) as tmp:
        t=Path(tmp).resolve(); app=t/"app-repo"; app.mkdir()
        data=run(["git","archive","--format=zip","HEAD"],a.app)
        with ZipFile(io.BytesIO(data)) as z:z.extractall(app)
        native=app/"app/src/main/jniLibs"; signer=a.sdk.resolve()/"build-tools/36.0.0/lib/apksigner.jar"
        key=t/"measure.jks"
        run(["keytool","-genkeypair","-noprompt","-alias","probe","-keystore",key,"-storepass","sizeonly",
             "-keypass","sizeonly","-dname","CN=Size Probe","-keyalg","RSA","-keysize","2048","-validity","30"],t)
        def build(label):
            wrap=["cmd.exe","/d","/c","gradlew.bat"] if os.name=="nt" else ["sh","gradlew"]
            run(wrap+[":app:assembleDebug",":app:assembleRelease","--offline","--no-build-cache","--no-daemon",
                      "--max-workers=2","-Dorg.gradle.jvmargs=-Xmx768m -XX:ActiveProcessorCount=2 -Dfile.encoding=UTF-8",
                      "--console=plain"],app,env)
            outputs=app/"app/build/outputs/apk"
            release=outputs/"release/app-release-unsigned.apk"; signed=t/(label+".apk")
            java=["java","-Xms16m","-Xmx128m","-XX:+UseSerialGC","-XX:ActiveProcessorCount=2","-jar",signer]
            run(java+["sign","--ks",key,"--ks-pass","pass:sizeonly","--ks-key-alias","probe",
                 "--v4-signing-enabled","false","--out",signed,release],app)
            run(java+["verify",signed],app)
            snap={"debug":snapshot(outputs/"debug/app-debug.apk"),"unsigned_release":snapshot(release),
                  "signed_release":snapshot(signed)}
            report["variants"][label]=snap
            print(label, {k:v["bytes"] for k,v in snap.items()},flush=True)
        build("original")
        prefix="com/alensw/transfer/"
        for folder,suffix in (("main/smali",".smali"),("recovered/java",".java")):
            d=app/"app/src"/folder/prefix
            report["removed_"+suffix[1:]+"_files"]=len(list(d.rglob("*"+suffix)))
            # Only this disposable git archive is changed; references from other classes intentionally remain.
            shutil.rmtree(d)
        for name,starts in (("sources.txt",prefix),("replace.txt","L"+prefix)):
            f=app/"app/recovered-java"/name
            f.write_bytes(b"".join(l for l in f.read_bytes().splitlines(keepends=True) if not l.startswith(starts.encode())))
        build("without_85_transfer_classes")
        ref=report["variants"]["original"]["debug"]["dex"]
        cut=report["variants"]["without_85_transfer_classes"]["debug"]["dex"]
        if sum(v["class_defs"] for v in ref.values())-sum(v["class_defs"] for v in cut.values())!=85:
            raise ValueError("not the expected transfer namespace")
        paths,report["stripped_elf_probes"]=parser_fixtures(t,a.ndk16.resolve(),a.ndk64 and a.ndk64.resolve(),a.pico_dir)
        for abi in ABIS: shutil.copyfile(paths[abi],native/abi/"libqpicwirecost.so")
        build("without_old_plus_parser_only_separate_so")
        for abi in ABIS:
            (native/abi/"libqpicwirecost.so").unlink()
            shutil.copyfile(a.jni/"native-src/build/libs"/abi/"libqpicjni156.so",native/abi/"libqpicjni156.so")
        build("without_old_with_reconstructed_image_libs")
        if a.ndk64:
            # Only use the independent source port in the JNI repo. The Android app's
            # ordinary ABI filters and the user's working tree are never modified.
            gradle=app/"app/build.gradle"; source=gradle.read_text("utf8")
            old="abiFilters 'armeabi', 'x86'"
            if source.count(old)!=1: raise ValueError("unexpected baseline ABI filters")
            for abi in ABIS: (native/abi/"libqpicjni156.so").unlink()
            (native/"arm64-v8a").mkdir(exist_ok=True)
            shutil.copyfile(a.jni/"native-src/build/libs/arm64-v8a/libqpicjni156.so",native/"arm64-v8a/libqpicjni156.so")
            gradle.write_text(source.replace(old,"abiFilters 'arm64-v8a'"),encoding="utf8")
            build("without_old_arm64_only_reconstructed")
            for abi in ABIS:
                shutil.copyfile(a.jni/"native-src/build/libs"/abi/"libqpicjni156.so",native/abi/"libqpicjni156.so")
            gradle.write_text(source.replace(old,old+", 'arm64-v8a'"),encoding="utf8")
            build("without_old_three_reconstructed")
    a.out.parent.mkdir(parents=True,exist_ok=True)
    a.out.write_bytes((json.dumps(report,indent=2)+"\n").encode())


if __name__=="__main__": main()
