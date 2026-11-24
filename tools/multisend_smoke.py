"""Exercise recipient selection, independent jobs and background queueing with official HTTP receivers."""
import argparse
from contextlib import ExitStack
import hashlib
import json
import os
from pathlib import Path
import random
import re
import shlex
import subprocess
import tempfile
import time
import uuid
import xml.etree.ElementTree as ET

PKG="com.alensw.PicFolder"
SCREEN=PKG+"/com.alensw.transfer.TransferActivity"


def main():
    ap=argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--serial",required=True)
    ap.add_argument("--peer",type=Path,required=True)
    ap.add_argument("--report",type=Path,required=True)
    opt=ap.parse_args()
    def adb(*args): return subprocess.check_output(["adb","-s",opt.serial,*args],timeout=40)
    def sh(cmd): return adb("shell",cmd).decode("utf8","replace").replace("\r","").strip()
    assert sh("getprop ro.kernel.qemu")=="1" and sh("getprop ro.build.version.sdk")=="23"
    tag=uuid.uuid4().hex[:10]; dump="/data/local/tmp/multisend-"+tag+".xml"
    def tree():
        sh("uiautomator dump "+dump); return ET.fromstring(adb("exec-out","cat",dump))
    def click(n):
        x,y,r,b=map(int,re.findall(r"\d+",n.get("bounds"))); sh(f"input tap {(x+r)//2} {(y+b)//2}")
    def node(attr,value):
        for _ in range(4):
            nodes=list(tree().iter("node"))
            n=next((n for n in nodes if n.get(attr)==value),None)
            if n is not None: return n
        raise AssertionError((value,[(n.get("text"),n.get("resource-id")) for n in nodes]))
    def find_row(label,cancel=False):
        for attempt in range(6):
            ui=tree(); parents={n:p for p in ui.iter() for n in p}
            titles=[n for n in ui.iter("node") if n.get("resource-id")==PKG+":id/sender"]
            n=next((n for n in titles if n.get("text","").startswith(label)),None)
            if n is not None:
                if cancel:
                    row=parents[parents[n]]
                    click(next(x for x in row.iter("node") if x.get("resource-id")==PKG+":id/cancel"))
                return
            sh("input swipe 155 425 155 190 220" if attempt%3==0 else "input swipe 155 190 155 425 220")
        raise AssertionError((label,[n.get("text") for n in titles]))
    def until(fn,reason):
        end=time.monotonic()+12
        while not fn():
            assert time.monotonic()<end,reason; time.sleep(.12)
    def visible(): return ":D045 " in sh("cat /proc/net/udp")
    def stop(child):
        if child.poll() is None: child.kill()
        child.wait()
    with tempfile.TemporaryDirectory() as tmp, ExitStack() as cleanup:
        root=Path(tmp); cleanup.callback(sh,"rm -f "+dump)
        sh("am force-stop "+PKG); cleanup.callback(sh,"am force-stop "+PKG)
        store="content://media/external/file"; expected=[]
        rng=random.Random(3701)
        for i,size in enumerate((262912,196611)):
            data=rng.randbytes(size);expected.append(hashlib.sha256(data).hexdigest())
            name=f"multi-{tag}-{i}.bin"; p=root/name; p.write_bytes(data)
            path="/storage/emulated/0/Download/"+name
            adb("push",str(p),path); cleanup.callback(sh,"rm -f "+shlex.quote(path))
            sh(f"content insert --uri {store} --bind _data:s:{path} --bind mime_type:s:application/octet-stream")
            row=sh(f"content query --uri {store} --projection _id --where "+shlex.quote("_data='"+path+"'"))
            uri=store+"/"+re.search(r"\b_id=(\d+)",row)[1]; cleanup.callback(sh,"content delete --uri "+uri)
            sh("am start -W -n "+SCREEN+" -f 0x30000000 -a android.intent.action.SEND -t application/octet-stream "
               "--grant-read-uri-permission --eu android.intent.extra.STREAM "+uri)
        assert "(2)" in node("resource-id",PKG+":id/files_to_send").get("text")
        until(visible,"discovery"); tip=node("resource-id",PKG+":id/empty_tip").get("text")
        assert tip.startswith("Looking for nearby devices\n"),tip
        click(node("resource-id",PKG+":id/ls_multi"))
        assert node("resource-id",PKG+":id/ls_send_selected").get("enabled")=="false"
        local=re.search(r"inet ([0-9.]+)/",sh("ip -4 addr show eth0"))[1]
        names=["A cancel","B keep","C decline","D keep","E queued"]
        children={}
        # Adding each peer at the top exercises selection by identity instead of list position.
        for count,name in enumerate(reversed(names),1):
            gate=root/name; gate.mkdir()
            child=subprocess.Popen([str(opt.peer),"receive-gated",str(gate/"in"),str(gate),name,"2",
                                    "decline" if name=="C decline" else "accept"],
                                   stdout=subprocess.PIPE,stderr=subprocess.STDOUT,env=dict(os.environ,TOKIO_WORKER_THREADS="2"))
            children[name]=child; cleanup.callback(stop,child)
            for line in child.stdout:
                if line.startswith(b"PORT="): break
            port=int(line.split(b"=")[1]); reverse=f"tcp:{port}"
            adb("reverse","--no-rebind",reverse,reverse); cleanup.callback(adb,"reverse","--remove",reverse)
            rule=f"-d {local} -p tcp --dport {port} -j REDIRECT --to-ports {port}"
            sh("iptables -t nat -I OUTPUT 1 "+rule); cleanup.callback(sh,"iptables -t nat -D OUTPUT "+rule)
            info={"alias":"beacon","fingerprint":name,"deviceType":"desktop","version":"2.2","protocol":"http","port":port,"announce":True}
            sh("printf '%s' "+shlex.quote(json.dumps(info))+" | timeout 1 /system/xbin/nc -u 224.0.0.167 53317 || :")
            if count==1: click(node("text",name)); click(node("text",name))  # uncheck and reselect
            click(node("text",name)); node("text",f"Send ({count})")
        click(node("resource-id",PKG+":id/ls_send_selected"))
        until(lambda:all((root/n/"waiting").exists() for n in names[:3]),"three independent offers before any answer")
        assert not any((root/n/"waiting").exists() for n in names[3:])
        find_row("E queued · Queued",cancel=True); find_row("E queued · Cancelled")
        sh("input keyevent 4"); until(lambda:not visible(),"sharing activity finished")
        (root/"C decline"/"resume").touch()
        until((root/"D keep"/"waiting").exists,"next recipient starts while hidden, despite a decline")
        sh("am start -W -n "+SCREEN+" -a com.alensw.transfer.VIEW_SESSION")
        find_row("C decline · HTTP 403"); find_row("A cancel · ",cancel=True); find_row("A cancel · Cancelled")
        sh("input keyevent 4"); until(lambda:not visible(),"hidden again")
        for n in ("D keep","B keep"): (root/n/"resume").touch()
        for n in ("D keep","B keep"):
            child=children[n]; assert child.wait(timeout=25)==0,child.stdout.read()
            hashes=sorted(hashlib.sha256(p.read_bytes()).hexdigest() for p in (root/n/"in").iterdir())
            assert hashes==sorted(expected),(n,hashes,expected)
        assert not (root/"E queued"/"waiting").exists()
        assert all(not list((root/n/"in").iterdir()) for n in ("A cancel","C decline","E queued"))
        until(lambda:not re.search(r":D045\s+\S+\s+0A\b",sh("cat /proc/net/tcp")),"last hidden endpoint retires")
        sh("am start -W -n "+SCREEN+" -a com.alensw.transfer.VIEW_SESSION")
        for n in ("D keep","B keep"): find_row(n+" · Sent")
        base=sh("pm path "+PKG).removeprefix("package:")
        result={"selected_devices":5,"concurrent_pending_offers":3,"independent_completed_recipients":2,
                "file_sha256_at_each_receiver":expected,"running_cancelled":True,"queued_cancelled_before_offer":True,
                "peer_declined_and_queue_advanced_while_hidden":True,"completed_after_activity_closed":True,
                "apk_sha256":hashlib.sha256(adb("exec-out","cat",base)).hexdigest(),"device":sh("getprop ro.build.fingerprint")}
        opt.report.parent.mkdir(parents=True,exist_ok=True)
        opt.report.write_text(json.dumps(result,indent=2)+"\n","utf8");print(json.dumps(result))


if __name__=="__main__":
    main()
