"""Check the gallery's separate loopback video service against a real MediaStore FD."""
import argparse
from http.client import HTTPConnection
from pathlib import Path
import re
import subprocess
import time
import uuid


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--serial",required=True)
    p.add_argument("--mp4",required=True,type=Path)
    args=p.parse_args()
    adb=["adb","-s",args.serial]
    def run(*cmd):
        return subprocess.check_output([*adb,*cmd],timeout=40).decode("utf-8","replace").strip()
    assert run("shell","getprop ro.kernel.qemu")=="1"
    data=args.mp4.read_bytes(); assert len(data)>1024
    name="oqp-"+uuid.uuid4().hex[:12]+".mp4"; path="/sdcard/Movies/"+name
    media="content://media/external/video/media"; uri=None; forward=None
    try:
        run("shell","mkdir -p /sdcard/Movies"); run("push",str(args.mp4),path)
        run("shell",f"am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///storage/emulated/0/Movies/{name}")
        for _ in range(25):
            listing=run("shell","content query --uri "+media+" --projection _id:_display_name")
            match=re.search(r"_id=(\d+), _display_name="+re.escape(name),listing)
            if match: break
            time.sleep(.3)
        assert match, listing
        uri=media+"/"+match[1]; run("shell","am force-stop com.alensw.PicFolder")
        run("shell","am startservice -n com.alensw.PicFolder/com.alensw.video.VideoProxyService -a android.intent.action.VIEW -d "+uri+" -t video/mp4")
        for _ in range(12):
            log=run("shell","dumpsys activity activities")
            target=re.search(r"http://127\.0\.0\.1:(\d+)(/video/[a-f0-9-]+/"+re.escape(name)+")",log)
            if target: break
            time.sleep(.25)
        assert target, log[-3000:]
        sockets=run("shell","cat /proc/net/tcp /proc/net/tcp6")
        listening=[line.split()[1] for line in sockets.splitlines()
                   if len(line.split())>3 and line.split()[3]=="0A" and line.split()[1].endswith(f":{int(target[1]):04X}")]
        assert listening and all(x.startswith(("0100007F:","0000000000000000FFFF00000100007F:")) for x in listening), listening
        # Closed transfers can legitimately leave TCP TIME_WAIT entries.
        assert not re.search(r":D045\s+\S+\s+0A\b",sockets)
        assert not re.search(r":D045\s+\S+\s+07\b",run("shell","cat /proc/net/udp"))
        forward=int(run("forward","tcp:0","tcp:"+target[1]))
        for bounds,status,expected in [(None,200,data),("bytes=128-511",206,data[128:512]),
                                       ("bytes=-257",206,data[-257:]),(f"bytes={len(data)}-",416,b"")]:
            c=HTTPConnection("127.0.0.1",forward,timeout=8)
            c.request("GET",target[2],headers={"Range":bounds} if bounds else {})
            res=c.getresponse(); body=res.read(); c.close()
            assert (res.status,body)==(status,expected),(bounds,res.status,len(body))
        launched=run("shell",f"am start -W -n com.alensw.PicFolder/.StreamingActivity -a android.intent.action.VIEW "
                     f"-t video/mp4 -d http://127.0.0.1:{target[1]}{target[2]}")
        assert "Status: ok" in launched, launched
        print("Loopback-only video handoff: GET 200, bounded/suffix 206, unsatisfiable 416; gallery player opened; LocalSend absent.")
    finally:
        if forward: run("forward","--remove",f"tcp:{forward}")
        run("shell","am force-stop com.alensw.PicFolder")
        if uri: run("shell","content delete --uri "+uri)
        run("shell","rm -f "+path)


if __name__=="__main__":
    main()
