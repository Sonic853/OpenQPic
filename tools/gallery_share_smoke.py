"""Share one and two images from the real gallery UI to a different UID with no storage permission."""
import argparse
from contextlib import ExitStack
import hashlib
import io
import os
from pathlib import Path
import re
import shlex
import subprocess
import tempfile
import time
import uuid
from xml.etree import ElementTree as ET
from zipfile import ZipFile
from PIL import Image, PngImagePlugin

APP="com.alensw.PicFolder"
PEER="org.openqpic.shareprobe"
LABEL="OpenQPic share probe"

def run(*args):
    return subprocess.check_output([str(a) for a in args], timeout=75)

def build_probe(sdk, tmp):
    jar=sdk/"platforms/android-36/android.jar"; bt=sdk/"build-tools/36.0.0"
    manifest=tmp/"AndroidManifest.xml"
    manifest.write_text(f"""<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="{PEER}">
    <application android:debuggable="true" android:label="{LABEL}">
    <activity android:name=".ShareProbe" android:exported="true"><intent-filter>
    <action android:name="android.intent.action.SEND"/><action android:name="android.intent.action.SEND_MULTIPLE"/>
    <category android:name="android.intent.category.DEFAULT"/><data android:mimeType="image/*"/>
    </intent-filter></activity></application></manifest>""",encoding="utf8")
    apk=tmp/"probe.apk"
    run(bt/("aapt2.exe" if os.name=="nt" else "aapt2"),"link","-I",jar,"--manifest",manifest,
        "--min-sdk-version","23","--target-sdk-version","32","-o",apk)
    run("javac","--release","8","-classpath",jar,"-d",tmp,Path(__file__).parent/"share/ShareProbe.java")
    run("java","-Xmx128m","-cp",bt/"lib/d8.jar","com.android.tools.r8.D8","--min-api","23","--lib",jar,
        "--output",tmp,tmp/"org/openqpic/shareprobe/ShareProbe.class")
    with ZipFile(apk,"a") as z: z.write(tmp/"classes.dex","classes.dex")
    aligned=tmp/"aligned.apk"
    run(bt/("zipalign.exe" if os.name=="nt" else "zipalign"),"4",apk,aligned)
    # Ephemeral test key: do not use the application's signing identity.
    key=tmp/"probe.p12"
    run("keytool","-genkeypair","-keystore",key,"-storepass","probe123","-alias","probe","-keyalg","RSA","-dname","CN=Share Probe")
    run("java","-Xmx96m","-jar",bt/"lib/apksigner.jar","sign","--ks",key,"--ks-pass","pass:probe123",aligned)
    return aligned

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--sdk",required=True,type=Path)
    p.add_argument("--serial",required=True)
    opt=p.parse_args()
    def adb(*args): return run("adb","-s",opt.serial,*args)
    def shell(command): return adb("shell",command).decode("utf8","replace").replace("\r","").strip()
    assert "1" in (shell("getprop ro.kernel.qemu"),shell("getprop ro.boot.qemu"))
    assert "targetSdk=32" in shell("dumpsys package "+APP)
    assert PEER not in shell("pm list packages "+PEER), "Use an emulator without the test receiver installed"
    tag=uuid.uuid4().hex[:12]; folder="/storage/emulated/0/Pictures/OqpShare-"+tag
    dump="/data/local/tmp/oqp-share-"+tag+".xml"
    def node(label):
        for _ in range(3):
            shell("uiautomator dump "+dump)
            nodes=list(ET.fromstring(adb("exec-out","cat",dump)).iter("node"))
            for n in nodes:
                if label in (n.get("text"),n.get("content-desc"),n.get("resource-id")): return n
        raise AssertionError((label,[(n.get("text"),n.get("content-desc"),n.get("resource-id")) for n in nodes]))
    def tap_label(label):
        x,y,r,b=map(int,re.findall(r"\d+",node(label).get("bounds")))
        shell(f"input tap {(x+r)//2} {(y+b)//2}")
    def thumb(color, long=False):
        end=time.monotonic()+15
        while time.monotonic()<end:
            img=Image.open(io.BytesIO(adb("exec-out","screencap","-p"))).convert("RGB")
            pts=[(x,y) for y in range(img.height//7,img.height-14,4) for x in range(5,img.width-5,4)
                 if all(abs(a-b)<18 for a,b in zip(img.getpixel((x,y)),color))]
            if len(pts)>12:
                x,y=(sum(axis)//len(pts) for axis in zip(*pts))
                shell(f"input swipe {x} {y} {x} {y} 1200" if long else f"input tap {x} {y}");return
        raise AssertionError(("No thumbnail",color))
    colors=[(225,30,149),(24,222,133)]; digests=[]
    with tempfile.TemporaryDirectory() as td, ExitStack() as clean:
        tmp=Path(td)
        adb("install",build_probe(opt.sdk,tmp)); clean.callback(adb,"uninstall",PEER)
        clean.callback(shell,"rm -f "+dump)
        shell("mkdir -p "+folder); clean.callback(shell,"rmdir "+folder)
        for i,color in enumerate(colors):
            file=tmp/f"{i}.png"; meta=PngImagePlugin.PngInfo(); meta.add_text("Comment","Q"*8192)
            Image.new("RGB",(128,128),color).save(file,pnginfo=meta)
            digests.append(hashlib.sha256(file.read_bytes()).hexdigest())
            path=folder+"/"+file.name; adb("push",file,path)
            clean.callback(shell,"rm -f "+path)
            clean.callback(shell,"content delete --uri content://media/external/images/media --where "+shlex.quote("_data='"+path+"'"))
            shell("am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file://"+path)
        clean.callback(shell,"am force-stop "+APP)
        for count in (1,2):
            shell("am force-stop "+APP)
            shell(f"am start -W -n {APP}/.GalleryActivity -d file://{folder}")
            node("OqpShare-"+tag+" (2)")
            thumb(colors[0],long=True)
            if count==2: thumb(colors[1])
            tap_label(APP+":id/share"); tap_label(LABEL)
            expected=sorted(digests[:count])
            for _ in range(15):
                result=shell(f"run-as {PEER} cat files/result 2>/dev/null || true").splitlines()
                if result and sorted(result[1:])==expected: break
                time.sleep(.2)
            action="android.intent.action."+("SEND" if count==1 else "SEND_MULTIPLE")
            assert result and result[0]==action and sorted(result[1:])==expected,(action,result)
    print("Gallery chooser: ACTION_SEND and ACTION_SEND_MULTIPLE visible; all three content URI reads match SHA-256.")

if __name__=="__main__": main()
