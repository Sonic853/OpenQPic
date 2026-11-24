"""Exercise the installed app on a dedicated English API 23 ARM test emulator.

Requires the official core peer built by OpenQPicLocalSend/tests/official_peer.py.
The emulator must route to its host alias 10.0.2.2 and the IPv4 multicast group.
The app must have storage permission. No auto-accept hooks or test routes in the APK.
"""
import argparse
from concurrent.futures import ThreadPoolExecutor
import hashlib
from http.client import HTTPConnection, HTTPException
import json
from pathlib import Path
import re
import shlex
import struct
import subprocess
import tempfile
import time
import urllib.parse
import uuid
import zlib
import xml.etree.ElementTree as ET

PKG = "com.alensw.PicFolder"
SCREEN = PKG+"/com.alensw.transfer.TransferActivity"
API = "/api/localsend/v2/"
FOLDER = "/sdcard/Download/QP Transfer"


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--serial", required=True)
    p.add_argument("--peer", required=True, type=Path)
    p.add_argument("--report", type=Path, required=True)
    p.add_argument("--mp4",type=Path,help="Also verify a real video from this small fixture")
    opt = p.parse_args()
    adb = ["adb","-s",opt.serial]
    run = lambda *args: subprocess.check_output([*adb,*args], timeout=40)
    def shell(command):
        return run("shell",command).decode("utf-8","replace").replace("\r","").strip()
    def launch(component, extras=""):
        assert "Status: ok" in shell("am start -W -n "+component+" "+extras)
    assert shell("getprop ro.kernel.qemu") == "1" and shell("getprop ro.build.version.sdk") == "23"
    assert not shell("pm path "+PKG).startswith("Error")
    run_id=uuid.uuid4().hex[:10]
    dump="/data/local/tmp/ls-"+run_id+".xml"
    def screen():
        shell("uiautomator dump "+dump)
        return ET.fromstring(run("exec-out","cat",dump))
    def center(node):
        x,y,r,b=map(int,re.findall(r"\d+",node.get("bounds")))
        return (x+r)//2,(y+b)//2
    def tap(*,rid=None,text=None,prefix=None):
        for _ in range(3):
            tree=screen()
            for node in tree.iter("node"):
                if ((rid is not None and node.get("resource-id")==rid) or
                        (text is not None and node.get("text")==text) or
                        (prefix is not None and node.get("text","").startswith(prefix))):
                    x,y=center(node); shell(f"input tap {x} {y}")
                    return
        raise AssertionError([(n.get("text"),n.get("resource-id")) for n in tree.iter("node")])
    def sockets():
        tcp=shell("cat /proc/net/tcp"); udp=shell("cat /proc/net/udp"); igmp=shell("cat /proc/net/igmp")
        return [bool(re.search(r":D045\s+\S+\s+0A\b",tcp)),
                bool(re.search(r":D045\s+\S+\s+07\b",udp)), "A70000E0" in igmp]
    # adbd forwards to loopback, while the app deliberately binds only the LAN
    # address. Route just our port on this isolated emulator; never bind the host LAN.
    local=re.search(r"inet ([0-9.]+)/",shell("ip -4 addr show eth0"))[1]
    tcp=shell("cat /proc/net/tcp /proc/net/tcp6"); rules=shell("iptables -t nat -S OUTPUT")
    transit=next(p for p in range(30117,30217) if f":{p:04X} " not in tcp and f"--dport {p} " not in rules)
    route=f"-d 127.0.0.1 -p tcp --dport {transit} -j DNAT --to-destination {local}:53317"
    shell("iptables -t nat -I OUTPUT 1 "+route)
    port=int(run("forward","tcp:0",f"tcp:{transit}").strip())
    reverse=None; backroute=None
    def http(route,body=None,method="POST",timeout=12):
        c=HTTPConnection("127.0.0.1",port,timeout=timeout)
        try:
            data=json.dumps(body).encode() if isinstance(body,dict) else body
            c.request(method,API+route,data,{"Content-Type":"application/json" if isinstance(body,dict) else "application/octet-stream"})
            r=c.getresponse(); return r.status,r.read()
        finally: c.close()
    def state(expected):
        deadline=time.monotonic()+8
        while sockets()!=expected:
            assert time.monotonic()<deadline, sockets()
            time.sleep(.15)
    def hidden(active=False):
        state([active,False,False])
        for path,method,body in [("info","GET",None),("register","POST",peer),
                                 ("prepare-upload","POST",offer("photo.png",b"png"))]:
            try: status,_=http(path,body,method,timeout=2)
            except (OSError,HTTPException): continue
            raise AssertionError((path,status))
        deadline=time.monotonic()+5
        while ("isForeground=true" in shell("dumpsys activity services "+PKG))!=active:
            assert time.monotonic()<deadline,"foreground service state"
            time.sleep(.15)
    def files():
        return set(shell("ls "+shlex.quote(FOLDER)+" 2>/dev/null").splitlines())
    def contents(name):
        return run("exec-out","cat "+shlex.quote(FOLDER+"/"+name))
    peer={"alias":"Fixture","fingerprint":run_id,"port":27117,"version":"2.2","protocol":"http","download":False}
    def offer(name,data,mime="application/octet-stream"):
        return {"info":peer,"files":{"one":{"id":"one","fileName":name,"size":len(data),"fileType":mime,
                                            "sha256":hashlib.sha256(data).hexdigest()}}}
    def png():
        def chunk(tag,data):
            return struct.pack(">I",len(data))+tag+data+struct.pack(">I",zlib.crc32(tag+data))
        return (b"\x89PNG\r\n\x1a\n"+chunk(b"IHDR",struct.pack(">2I5B",2,2,8,2,0,0,0))+
                chunk(b"tEXt",b"Comment\0"+b"P"*180000)+chunk(b"IDAT",zlib.compress((b"\0"+b"\x31\x22\x13"*2)*2))+chunk(b"IEND",b""))
    def upload_path(ticket,key="one"):
        return "upload?"+urllib.parse.urlencode({"sessionId":ticket["sessionId"],"fileId":key,"token":ticket["files"][key]})
    def forbidden_upload(path,size):
        # Check authorization before sending the body. Pushing the complete rejected body through
        # adbd can reset the connection on close (unread bytes) before the 403 reaches the client.
        c=HTTPConnection("127.0.0.1",port,timeout=5)
        try:
            c.request("POST",API+path,headers={"Content-Length":str(size),"Expect":"100-continue"})
            r=c.getresponse(); assert r.status==403,(r.status,r.read())
        finally: c.close()
    def partial(path,data):
        c=HTTPConnection("127.0.0.1",port,timeout=12)
        c.putrequest("POST",API+path); c.putheader("Content-Length",str(len(data))); c.endheaders()
        c.send(data[:1024]); return c
    def kill(child):
        if child.poll() is None: child.kill()
        child.wait()
    children=[]; added=set(); source="/sdcard/Download/ls-"+run_id+".bin"; result={}; source_uri=None
    try:
        shell("am force-stop "+PKG); launch(PKG+"/.GalleryActivity"); hidden()
        capture=subprocess.Popen(adb+["shell","timeout 18 tcpdump -l -nn -A -s 2048 -c 1 -i eth0 'udp and dst host 224.0.0.167 and dst port 53317'"],
                                 stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
        children.append(capture)
        launch(SCREEN,"-a android.intent.action.VIEW")
        state([True]*3)
        status,raw=http("info",method="GET"); info=json.loads(raw)
        assert status==200 and info["version"]=="2.2" and info["protocol"]=="http" and info["port"]==53317
        assert sockets()==[True]*3
        packet=capture.communicate(timeout=20)[0]
        assert info["fingerprint"].encode() in packet and b'"announce":true' in packet
        result["gallery_hidden_and_visible_multicast"]=True
        for alias,fp,proto,n in [("Obsolete","dev-a","http",26001),("Zed","dev-z","http",26002),
                                 ("Alpha","dev-a","http",26001),("AAA encrypted","dev-e","https",26003)]:
            assert http("register",dict(peer,alias=alias,fingerprint=fp,protocol=proto,port=n,deviceType="desktop"))[0]==200
        labels=[n.get("text") for n in screen().iter("node") if n.get("resource-id")==PKG+":id/title"]
        assert labels==["Alpha","Zed","AAA encrypted"],labels
        result["peers_deduplicated_sorted_http_first"]=True
        tap(rid=PKG+":id/add"); hidden()
        # Wait for the picker window to finish its enter animation before Back.
        shell("uiautomator dump "+dump); shell("input keyevent 4")
        deadline=time.monotonic()+10
        while sockets()!=[True]*3:
            assert time.monotonic()<deadline, shell("dumpsys window windows | grep mCurrentFocus=")
            time.sleep(.2)
        assert http("info",method="GET")[0]==200
        shell("input keyevent 3"); hidden()
        result["picker_and_home_hidden"]=True
        launch(SCREEN)
        state([True]*3)
        assert http("prepare-upload",offer("document.pdf",b"%PDF","application/pdf"))[0]==415
        assert http("prepare-upload",offer("app.apk",b"APK","image/png"))[0]==415
        assert http("prepare-upload",offer("empty.jpg",b"","image/jpeg"))[0]==415
        before=files()
        sender=subprocess.Popen([str(opt.peer),"send",str(port)],stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
        children.append(sender)
        for _ in range(3):
            tree=screen()
            if any(n.get("resource-id")==PKG+":id/ls_accept" for n in tree.iter("node")): break
        text=" ".join(n.get("text","") for n in tree.iter("node"))
        assert "official" in text.lower() or "官方" in text,text
        assert "Skipped 1" in text and "notes.txt" not in text and "sound.wav" in text,text
        tap(rid=PKG+":id/ls_accept")
        output=sender.communicate(timeout=35)[0].decode(); assert sender.returncode==0,output
        hashes=re.search(r"PNG=(\w+) WAV=(\w+)",output).groups()
        new=files()-before; added.update(new)
        assert len(new)==2
        assert any(n.startswith("官方-📷") and hashlib.sha256(contents(n)).hexdigest()==hashes[0] for n in new)
        assert any(n.startswith("sound") and hashlib.sha256(contents(n)).hexdigest()==hashes[1] for n in new)
        result["official_to_android"]={"png_wav_sha256":hashes,"parallel":True,"text_skipped":True}
        with ThreadPoolExecutor(2) as pool:
            name="ls-"+run_id+".png"; data=png()
            req=offer("nested/"+name,data)
            waiter=pool.submit(http,"prepare-upload",req,"POST",90)
            # Wait until the pending decision has reached the Android UI.
            for _ in range(3):
                shell("uiautomator dump "+dump)
                if b"Receive from Fixture" in run("exec-out","cat",dump): break
            assert http("prepare-upload",req)[0]==409
            assert http("cancel")[0]==200 and waiter.result(timeout=5)[0]==403
            waiter=pool.submit(http,"prepare-upload",req,"POST",90)
            tap(rid=PKG+":id/ls_accept")
            status,raw=waiter.result(); assert status==200
            ticket=json.loads(raw); args={"sessionId":ticket["sessionId"],"fileId":"one","token":"invalid"}
            path=lambda:"upload?"+urllib.parse.urlencode(args)
            forbidden_upload(path(),len(data)); args["token"]=ticket["files"]["one"]
            assert http(path(),b"X"*len(data))[0]==422
            assert name not in files() and http(path(),data)[0]==200
            added.add(name); assert contents(name)==data; forbidden_upload(path(),len(data))
            result["busy_cancel_token_hash_retry_replay"]=True
            def accept(req):
                waiter=pool.submit(http,"prepare-upload",req,"POST",75)
                tap(rid=PKG+":id/ls_accept")
                code,body=waiter.result(timeout=7); assert code==200,(code,body)
                return json.loads(body)
            wrong="fake-"+run_id+".jpg"; invalid=b"plain text masquerading as an image"
            companion="companion-"+run_id+".png"
            req=offer(wrong,invalid,"image/jpeg"); req["files"]["good"]=dict(offer(companion,data)["files"]["one"],id="good")
            t=accept(req)
            assert http(upload_path(t),invalid)[0]==415 and wrong not in files()
            assert http(upload_path(t,"good"),data)[0]==200; added.add(companion); assert contents(companion)==data
            result["disguised_media_rejected_before_publish"]=True
            keep="choose-"+run_id+".png"; skip="skip-"+run_id+".png"
            req=offer(keep,data); req["files"]["two"]=dict(req["files"]["one"],id="two",fileName=skip)
            waiter=pool.submit(http,"prepare-upload",req,"POST",75); tap(prefix=skip)
            shell("input keyevent 3"); hidden(True); launch(SCREEN); state([True]*3)
            tree=screen()
            assert any(n.get("text","").startswith(skip) and n.get("checked")=="false" for n in tree.iter("node"))
            tap(rid=PKG+":id/ls_accept")
            code,body=waiter.result(); t=json.loads(body); assert code==200 and list(t["files"])==["one"]
            assert http(upload_path(t),data)[0]==200; added.add(keep); assert skip not in files()
            result["confirmation_selection_survives_home"]=True
            if opt.mp4:
                movie="ls-"+run_id+".mp4"; clip=opt.mp4.read_bytes()
                t=accept(offer(movie,clip,"video/mp4"))
                assert http(upload_path(t),clip)[0]==200; added.add(movie); assert contents(movie)==clip
                result["real_video_received"]=True
            a="partial-"+run_id+".png"; b="next-"+run_id+".png"
            req=offer(a,data); req["files"]["two"]=dict(req["files"]["one"],id="two",fileName=b)
            t=accept(req); c=partial(upload_path(t),data)
            try:
                shell("input keyevent 3"); hidden(True)
                # No discovery or new offer while hidden; another authorized
                # connection for the same multi-file session must still work.
                assert http(upload_path(t,"two"),data)[0]==200; added.add(b)
                c.send(data[1024:]); r=c.getresponse(); assert r.status==200; r.read(); added.add(a)
            finally: c.close()
            hidden(); assert contents(a)==contents(b)==data
            result["incoming_stream_and_next_file_survive_home"]=True
            launch(SCREEN); state([True]*3)
            name="cancel-"+run_id+".png"; t=accept(offer(name,data)); c=partial(upload_path(t),data)
            try:
                shell("input keyevent 3"); hidden(True)
                launch(SCREEN,"-a com.alensw.transfer.VIEW_SESSION"); state([True]*3)
                tap(rid=PKG+":id/cancel"); forbidden_upload(upload_path(t),len(data))
            finally: c.close()
            assert name not in files()
            result["manual_cancel_from_restored_history"]=True
            name="notify-"+run_id+".png"; t=accept(offer(name,data)); c=partial(upload_path(t),data)
            try:
                shell("input keyevent 3"); hidden(True); shell("service call statusbar 1")
                for _ in range(3):
                    tree=screen(); nodes=list(tree.iter("node"))
                    title=next((n for n in nodes if n.get("text")=="WiFi transfer"),None)
                    action=next((n for n in nodes if n.get("resource-id")=="android:id/action0" and
                                 (n.get("text","")+n.get("content-desc","")).lower().startswith("cancel")),None)
                    if title is not None and action is not None:
                        x,y=center(action); shell(f"input tap {x} {y}"); break
                    if title is not None:
                        x,y=center(title); shell(f"input swipe {x} {y} {x} {min(y+115,468)} 350")
                else: raise AssertionError([(n.get("text"),n.get("resource-id")) for n in nodes])
                hidden()
            finally: c.close(); shell("service call statusbar 2")
            assert name not in files()
            assert ".ls-" not in shell("ls -a "+shlex.quote(FOLDER))
            result["manual_cancel_from_background_notification"]=True
        with tempfile.TemporaryDirectory() as temp:
            root=Path(temp); sample=root/"source.bin"; data=bytes(range(256))*1027; sample.write_bytes(data)
            run("push",str(sample),source)
            store="content://media/external/file"; path=source.replace("/sdcard/","/storage/emulated/0/")
            shell("content insert --uri "+store+" --bind _data:s:"+path+" --bind _display_name:s:source.bin --bind mime_type:s:application/octet-stream")
            row=shell("content query --uri "+store+" --projection _id --where "+shlex.quote("_data='"+path+"'"))
            source_uri=store+"/"+re.search(r"\b_id=(\d+)",row)[1]
            launch(SCREEN,"-f 0x10008000 -a android.intent.action.SEND -t application/octet-stream --grant-read-uri-permission --eu android.intent.extra.STREAM "+source_uri)
            receiver=subprocess.Popen([str(opt.peer),"receive-gated",str(root/"in"),str(root)],stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
            children.append(receiver)
            for line in receiver.stdout:
                if line.startswith(b"PORT="): break
            target=int(line.strip().split(b"=")[1]); announce=dict(peer,port=target,announce=True)
            reverse=f"tcp:{target}"; run("reverse",reverse,reverse)
            backroute=f"-d {local} -p tcp --dport {target} -j REDIRECT --to-ports {target}"
            shell("iptables -t nat -I OUTPUT 1 "+backroute)
            # Emit an independent real multicast datagram on eth0. Its declared
            # port reaches the official server via adbd; only HTTP registration
            # can return that server's alias ("Test Server" is not in the beacon).
            shell("printf '%s' "+shlex.quote(json.dumps(announce))+" | timeout 2 /system/xbin/nc -u 224.0.0.167 53317 || :")
            tap(text="Test Server")
            deadline=time.monotonic()+8
            while not (root/"waiting").exists():
                assert time.monotonic()<deadline; time.sleep(.1)
            shell("input keyevent 4"); hidden(True)
            # An id-less cancel is for a pending incoming offer; it cannot kill this send.
            try: code,_=http("cancel",timeout=2)
            except (OSError,HTTPException): pass
            else: raise AssertionError(code)
            (root/"resume").touch()
            assert receiver.wait(timeout=25)==0, receiver.stdout.read()
            received,=(root/"in").iterdir(); assert received.read_bytes()==data
            result["android_to_official"]={"bytes":len(data),"sha256":hashlib.sha256(data).hexdigest(),
                                           "peer_from_udp_http_register":True,"entry":"ACTION_SEND content://","completed_after_back":True}
        shell("input keyevent 3"); hidden()
        shell("am force-stop "+PKG); launch(PKG+"/.GalleryActivity"); launch(SCREEN)
        assert http("info",method="GET")[0]==200; shell("input keyevent 4"); hidden()
        result["back_and_reopen"]=True
        base=shell("pm path "+PKG).removeprefix("package:")
        result["apk_sha256"]=hashlib.sha256(run("exec-out","cat",base)).hexdigest()
        result["device"]=shell("getprop ro.build.fingerprint")
        opt.report.parent.mkdir(parents=True,exist_ok=True)
        opt.report.write_text(json.dumps(result,indent=2)+"\n","utf-8")
        print(json.dumps(result,indent=2))
    finally:
        for child in children: kill(child)
        shell("am force-stop "+PKG)
        if source_uri: shell("content delete --uri "+source_uri)
        for name in added:
            path="/storage/emulated/0/Download/QP Transfer/"+name
            shell("content delete --uri content://media/external/file --where "+shlex.quote("_data='"+path.replace("'","''")+"'"))
        for path in [dump,source,*(FOLDER+"/"+n for n in added)]: shell("rm -f "+shlex.quote(path))
        if reverse: run("reverse","--remove",reverse)
        if backroute: shell("iptables -t nat -D OUTPUT "+backroute)
        run("forward","--remove",f"tcp:{port}"); shell("iptables -t nat -D OUTPUT "+route)


if __name__=="__main__":
    main()
