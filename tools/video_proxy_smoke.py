"""Check the loopback video service with a real MediaStore FD and optional provider fixtures."""
import argparse
from contextlib import ExitStack
from http.client import HTTPConnection, HTTPSConnection
import io
from pathlib import Path
import os
import re
import shlex
import shutil
import socket
import ssl
import subprocess
import tempfile
import time
from urllib.parse import quote
import uuid
from zipfile import ZipFile


APP = "com.alensw.PicFolder"
PROBE = "org.openqpic.videoprobe"
SUBTITLE = b"1\n00:00:00,000 --> 00:00:01,000\nLoopback fixture\n"


def build_probe(sdk, work, mp4, external=False):
    bt = sdk/"build-tools/36.0.0"
    android = sdk/"platforms/android-36/android.jar"
    def tool(name):
        return bt/(name+(".exe" if os.name=="nt" else ""))
    def run(*args):
        subprocess.run([str(x) for x in args], check=True, timeout=60)
    manifest = work/"AndroidManifest.xml"
    player = '''<activity android:name=".ProbePlayer" android:exported="true"><intent-filter>
        <action android:name="android.intent.action.VIEW"/><category android:name="android.intent.category.DEFAULT"/>
        <data android:scheme="http" android:mimeType="video/mp4"/>
    </intent-filter></activity>''' if external else ""
    manifest.write_text(f"""<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="{PROBE}">
    <uses-permission android:name="android.permission.INTERNET"/>
    <application android:label="Video fixture" android:debuggable="true" android:usesCleartextTraffic="true">
        <provider android:name=".ProbeProvider" android:authorities="{PROBE}" android:exported="true" />
        {player}
    </application>
</manifest>""", "utf-8")
    assets = work/"assets"; assets.mkdir()
    shutil.copyfile(mp4, assets/"clip.mp4")
    classes = work/"classes"; classes.mkdir()
    apk = work/"probe.apk"
    run(tool("aapt2"), "link", "-I", android, "--manifest", manifest, "--min-sdk-version", "23",
        "--target-sdk-version", "32", "-A", assets, "-o", apk)
    run("javac", "--release", "8", "-Xlint:-options", "-classpath", android, "-d", classes,
        *sorted((Path(__file__).parent/"video").glob("*.java")))
    run("java", "-Xmx160m", "-cp", bt/"lib/d8.jar", "com.android.tools.r8.D8",
        "--min-api", "23", "--lib", android, "--output", work, *sorted(classes.rglob("*.class")))
    with ZipFile(apk,"a") as z:
        z.write(work/"classes.dex","classes.dex")
    aligned = work/"aligned.apk"
    run(tool("zipalign"),"4",apk,aligned)
    key = work/"probe.p12"
    run("keytool","-genkeypair","-keystore",key,"-storepass","probe123","-alias","probe",
        "-keyalg","RSA","-keysize","2048","-validity","7","-dname","CN=Video Fixture")
    run("java","-Xmx96m","-jar",bt/"lib/apksigner.jar","sign","--ks",key,"--ks-pass","pass:probe123",aligned)
    return aligned


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--serial",required=True)
    p.add_argument("--mp4",required=True,type=Path)
    p.add_argument("--sdk",type=Path,help="also verify a content provider's asset slice, subtitle and pipe")
    p.add_argument("--tls",action="store_true",help="exercise the built-in player and its separate pinned HTTPS listener")
    p.add_argument("--blue-frame",action="store_true",help="assert that the supplied solid-blue H.264 fixture is rendered")
    p.add_argument("--openssl",help="also verify that an unrelated HTTPS player URL uses normal system trust")
    args=p.parse_args()
    adb_exe = "adb.exe" if os.name=="nt" else "adb"
    adb=[str(args.sdk/"platform-tools"/adb_exe) if args.sdk else "adb","-s",args.serial]
    def run(*cmd):
        return subprocess.check_output([*adb,*cmd],timeout=40).decode("utf-8","replace").strip()
    assert run("shell","getprop ro.kernel.qemu")=="1"
    assert run("shell","id").startswith("uid=0("), "Run on a dedicated rootable emulator"
    data=args.mp4.read_bytes(); assert len(data)>1024
    name="oqp-"+uuid.uuid4().hex[:12]+".mp4"; path="/sdcard/Movies/"+name
    media="content://media/external/video/media"
    def start(uri, filename):
        extra="--ez com.alensw.video.INTERNAL_PLAYER true " if args.tls else ""
        run("shell", f"am startservice -n {APP}/com.alensw.video.VideoProxyService -a android.intent.action.VIEW "+extra+
            f"-d {shlex.quote(uri)} -t video/mp4")
        for _ in range(12):
            log=run("shell","dumpsys activity activities")
            scheme="https" if args.tls else "https?"
            target=re.search(scheme+r"://127\.0\.0\.1:(\d+)(/video/[a-f0-9-]+/"+re.escape(quote(filename,safe=""))+r")",log)
            if target and not args.tls and target[0].startswith("https:"):
                raise AssertionError("The default player is internal; use --tls or select an external HTTP handler.")
            if target: return target.groups()
            time.sleep(.25)
        raise AssertionError(log[-3000:])

    def request(path, method="GET", bounds=None):
        c=HTTPSConnection("127.0.0.1",forward,context=trust,timeout=10) if args.tls else HTTPConnection("127.0.0.1",forward,timeout=8)
        try:
            c.request(method,path,headers={"rAnGe":bounds} if bounds is not None else {})
            res=c.getresponse()
            assert res.getheader("Connection")=="close"
            return res.status,res.getheader("Content-Range"),int(res.getheader("Content-Length")),res.read()
        finally:
            c.close()

    def check_seekable(path):
        size=len(data)
        assert request(path)==(200,None,size,data)
        assert request(path,method="HEAD",bounds="bytes=128-511")==(200,None,size,b"")
        for bounds,begin,end in (("bytes=128-511",128,511), ("bytes=601-",601,size-1),
                                 ("bytes=-257",size-257,size-1), (f"bytes={size-4}-{size+99}",size-4,size-1)):
            assert request(path,bounds=bounds)==(206,f"bytes {begin}-{end}/{size}",end-begin+1,data[begin:end+1]),bounds
        for bounds in (f"bytes={size}-","bytes=0-1,2-3",""):
            assert request(path,bounds=bounds)==(416,f"bytes */{size}",0,b""),bounds

    with ExitStack() as clean:
        def tunnel(port):
            local=int(run("forward","tcp:0","tcp:"+port))
            clean.callback(run,"forward","--remove",f"tcp:{local}")
            return local

        clean.callback(run,"shell","rm -f "+path)
        if args.sdk:
            assert PROBE not in run("shell","pm list packages "+PROBE), "Use a clean fixture package"
            work=Path(clean.enter_context(tempfile.TemporaryDirectory()))
            assert "Success" in run("install",build_probe(args.sdk,work,args.mp4,external=not args.tls))
            clean.callback(run,"uninstall",PROBE)
        run("shell","mkdir -p /sdcard/Movies"); run("push",str(args.mp4),path)
        run("shell",f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///storage/emulated/0/Movies/{name}")
        for _ in range(25):
            listing=run("shell","content query --uri "+media+" --projection _id:_display_name")
            match=re.search(r"_id=(\d+), _display_name="+re.escape(name),listing)
            if match: break
            time.sleep(.3)
        assert match, listing
        uri=media+"/"+match[1]
        clean.callback(run,"shell","content delete --uri "+uri)
        run("shell","am force-stop "+APP)
        clean.callback(run,"shell","am force-stop "+APP)
        port, target=start(uri,name)
        sockets=run("shell","cat /proc/net/tcp /proc/net/tcp6")
        listening=[line.split()[1] for line in sockets.splitlines()
                   if len(line.split())>3 and line.split()[3]=="0A" and line.split()[1].endswith(f":{int(port):04X}")]
        assert listening and all(x.startswith(("0100007F:","0000000000000000FFFF00000100007F:")) for x in listening), listening
        # Closed transfers can legitimately leave TCP TIME_WAIT entries.
        assert not re.search(r":D045\s+\S+\s+0A\b",sockets)
        assert not re.search(r":D045\s+\S+\s+07\b",run("shell","cat /proc/net/udp"))
        forward=tunnel(port)
        if args.tls:
            # Bootstrap the fixture's trust from its private emulator tunnel; disable DNS name
            # matching here only. Every subsequent request verifies against this exact leaf.
            leaf=ssl.get_server_certificate(("127.0.0.1",forward),timeout=10)
            trust=ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT); trust.check_hostname=False
            trust.load_verify_locations(cadata=leaf)
        check_seekable(target)
        assert request(target,method="POST")==(405,None,0,b"")
        assert request(target.replace(target.split("/")[2],str(uuid.uuid4())))==(404,None,0,b"")
        if args.sdk and not args.tls:
            run("shell","run-as "+PROBE+" rm -f files/prefix")
            run("shell",f"am start -W -n {PROBE}/.ProbePlayer -a android.intent.action.VIEW -t video/mp4 -d http://127.0.0.1:{port}{target}")
            for _ in range(25):
                if run("shell","run-as "+PROBE+" ls files/prefix 2>/dev/null")=="files/prefix": break
                time.sleep(.2)
            prefix=subprocess.check_output([*adb,"exec-out","run-as",PROBE,"cat","files/prefix"],timeout=8)
            assert prefix==data[:256],"Third-party app could not read the HTTP playback grant"
            print("A separate player read the HTTP range with no custom certificates.",flush=True)
        scheme="https" if args.tls else "http"
        launched=run("shell",f"am start -W -n {APP}/.StreamingActivity -a android.intent.action.VIEW "
                     f"-t video/mp4 -d {scheme}://127.0.0.1:{port}{target} --ez android.intent.extra.finishOnCompletion false")
        assert "Status: ok" in launched, launched
        if args.blue_frame:
            from PIL import Image
            end=time.monotonic()+20
            while time.monotonic()<end:
                im=Image.open(io.BytesIO(subprocess.check_output([*adb,"exec-out","screencap","-p"],timeout=8))).convert("RGB")
                w,h=im.size; center=im.crop((w//4,h//3,3*w//4,2*h//3))
                if sum(r<70 and g<70 and b>170 for r,g,b in center.getdata())>center.width*center.height*.8: break
            else: raise AssertionError("Player did not render the supplied blue video")
            print("Verified decoded blue H.264 frame.",flush=True)
        print("Loopback-only MediaStore video: GET/HEAD, bounded/open/suffix ranges, 416, grant check and player opening passed.")
        if args.sdk:
            def probe(name):
                nonlocal port,forward
                if args.tls:
                    # A singleTask ActivityRecord may keep its original intent in dumpsys.
                    # Finish the old record so the next grant URL can be read from a new one.
                    active=run("shell","dumpsys activity activities | grep mResumedActivity")
                    if APP in active: run("shell","input keyevent 4")
                chosen="content://"+PROBE+"/document/"+quote("/fixture/"+name,safe="")
                probe_port, destination=start(chosen,name)
                if probe_port!=port:  # The first listener may retire while building the fixture.
                    port,forward=probe_port,tunnel(probe_port)
                return destination
            sliced=probe("clip + demo.mp4")
            check_seekable(sliced)
            sibling=sliced.rsplit("/",1)[0]+"/"+quote("clip + demo.srt",safe="")
            assert request(sibling)==(200,None,len(SUBTITLE),SUBTITLE)
            pipe=probe("pipe.mp4")
            assert request(pipe)==(200,None,len(data),data)
            assert request(pipe,bounds="bytes=0-31")==(206,f"bytes 0-31/{len(data)}",32,data[:32])
            assert request(pipe,bounds="bytes=1-")==(404,None,0,b"")
            print("Provider FD: byte-accurate declared slice, encoded name, sibling subtitle and sequential pipe passed.")
        if args.openssl:
            temp=Path(clean.enter_context(tempfile.TemporaryDirectory()))
            key,cert=temp/"foreign.key",temp/"foreign.pem"
            subprocess.run([args.openssl,"req","-x509","-newkey","rsa:2048","-nodes","-days","2","-sha256",
                            "-subj","/CN=127.0.0.1","-addext","subjectAltName=IP:127.0.0.1","-keyout",key,"-out",cert],
                           check=True,stderr=subprocess.DEVNULL)
            ctx=ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER); ctx.load_cert_chain(cert,key)
            with socket.create_server(("127.0.0.1",0)) as listener:
                foreign=listener.getsockname()[1]; reverse=f"tcp:{foreign}"
                run("reverse","--no-rebind",reverse,reverse); clean.callback(run,"reverse","--remove",reverse)
                run("shell",f"am start -n {APP}/.StreamingActivity -a android.intent.action.VIEW "
                            f"-t video/mp4 -d https://127.0.0.1:{foreign}/video/{uuid.uuid4()}/foreign.mp4")
                listener.settimeout(15)
                with listener.accept()[0] as client:
                    client.settimeout(12); assert client.recv(1,socket.MSG_PEEK)==b"\x16"
                    try: secure=ctx.wrap_socket(client,server_side=True)
                    except (ssl.SSLError,ConnectionResetError): pass
                    else: secure.close(); raise AssertionError("Player accepted an unrelated self-signed HTTPS certificate")
                run("shell","am force-stop "+APP)  # stop platform retries before removing the adb reverse listener
            print("An unrelated HTTPS playback certificate was rejected before any HTTP request.")


if __name__=="__main__":
    main()
