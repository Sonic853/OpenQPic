"""Exercise the installed app on a dedicated English API 23 ARM test emulator.

Requires the official core peer built by OpenQPicLocalSend/tests/official_peer.py.
The emulator must route to its host alias 10.0.2.2 and the IPv4 multicast group.
The app must have storage permission. No auto-accept hooks or test routes in the APK.
"""
import argparse
from concurrent.futures import ThreadPoolExecutor
from contextlib import ExitStack
import hashlib
from http.client import HTTPConnection, HTTPSConnection, HTTPException, parse_headers
import json
import os
from pathlib import Path
import re
import shlex
import socket
import ssl
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
    p.add_argument("--tls",action="store_true",help="expect the app's HTTPS listener")
    p.add_argument("--mixed",action="store_true",help="use the opposite HTTP/HTTPS listener mode on the official peer")
    p.add_argument("--interop-only",action="store_true",help="run discovery and both official transfer directions without the extended session cases")
    p.add_argument("--openssl",default="openssl",help="OpenSSL CLI for disposable test client certificates")
    opt = p.parse_args()
    listener_protocol = "https" if opt.tls else "http"
    peer_tls = opt.tls != opt.mixed
    peer_protocol = "https" if peer_tls else "http"
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
        for _ in range(4):
            message=shell("uiautomator dump "+dump)
            if "dumped" in message.lower(): return ET.fromstring(run("exec-out","cat",dump))
        raise AssertionError(("Cannot read activity UI",message))
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
        tcp=shell("cat /proc/net/tcp /proc/net/tcp6"); udp=shell("cat /proc/net/udp"); igmp=shell("cat /proc/net/igmp")
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
    tls_context=None; server_pin=None
    tls_tmp=tempfile.TemporaryDirectory() if opt.tls else None
    test_env=dict(os.environ,TOKIO_WORKER_THREADS="2")
    test_env.pop("OPENQPIC_TEST_TLS",None)
    sender_env=dict(test_env,OPENQPIC_TEST_SENDER_PROTOCOL=peer_protocol)
    receiver_env=test_env.copy()
    if opt.tls: sender_env["OPENQPIC_TEST_TLS"]="1"
    if peer_tls: receiver_env["OPENQPIC_TEST_TLS"]="1"
    def connection(timeout,ctx=None):
        return HTTPSConnection("127.0.0.1",port,timeout=timeout,context=ctx or tls_context) if opt.tls else HTTPConnection("127.0.0.1",port,timeout=timeout)
    def http(route,body=None,method="POST",timeout=12):
        c=connection(timeout)
        try:
            data=json.dumps(body).encode() if isinstance(body,dict) else body
            c.request(method,API+route,data,{"Content-Type":"application/json" if isinstance(body,dict) else "application/octet-stream"})
            r=c.getresponse(); return r.status,r.read()
        finally: c.close()
    def state(expected,timeout=8):
        deadline=time.monotonic()+timeout
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
    peer={"alias":"Fixture","fingerprint":run_id,"port":27117,"version":"2.2","protocol":peer_protocol,"download":False}
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
        c=connection(5)
        try:
            c.request("POST",API+path,headers={"Content-Length":str(size),"Expect":"100-continue"})
            r=c.getresponse(); assert r.status==403,(r.status,r.read())
        finally: c.close()
    def partial(path,data):
        c=connection(12)
        c.putrequest("POST",API+path); c.putheader("Content-Length",str(len(data))); c.endheaders()
        c.send(data[:1024]); return c
    def kill(child):
        if child.poll() is None: child.kill()
        child.wait()
    def client_cert(label):
        td=Path(tls_tmp.name)
        cert,key=td/(label+".pem"),td/(label+".key")
        subprocess.run([opt.openssl,"req","-x509","-newkey","rsa:2048","-nodes","-sha256","-days","2",
                        "-subj","/CN=Test Peer","-keyout",str(key),"-out",str(cert)],check=True,stderr=subprocess.DEVNULL)
        return (cert,key,hashlib.sha256(ssl.PEM_cert_to_DER_cert(cert.read_text())).hexdigest().upper())
    children=[]; added=set(); source="/sdcard/Download/ls-"+run_id+".bin"; result={"tls":opt.tls}; source_uri=None
    if opt.mixed: result["peer_protocol"]=peer_protocol
    try:
        if opt.tls:
            # Create certificates before the UI setup so a small emulator clock lag
            # does not make a just-issued certificate appear to start in the future.
            cert,key,peer["fingerprint"]=client_cert("client")
            cert2,key2,pin2=client_cert("pin-check")
            if not opt.interop_only: other,other_key,_=client_cert("other")
        shell("am force-stop "+PKG); launch(PKG+"/.GalleryActivity"); hidden()
        capture=subprocess.Popen(adb+["shell","timeout 90 tcpdump -l -nn -A -s 2048 -c 1 -i eth0 'udp and dst host 224.0.0.167 and dst port 53317'"],
                                 stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
        children.append(capture)
        launch(SCREEN,"-a android.intent.action.VIEW")
        state([True]*3,75 if opt.tls else 8)
        if opt.tls:
            # Discover the installed test server's leaf via the private adb tunnel, then
            # use strict leaf trust (no DNS names in LocalSend identities) on all HTTP calls.
            bootstrap=ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT); bootstrap.check_hostname=False
            bootstrap.verify_mode=ssl.CERT_NONE; bootstrap.load_cert_chain(cert,key)
            with socket.create_connection(("127.0.0.1",port),timeout=12) as raw,bootstrap.wrap_socket(raw) as tls:
                der=tls.getpeercert(True); server_pin=hashlib.sha256(der).hexdigest().upper()
            def context():
                ctx=ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT); ctx.check_hostname=False
                ctx.load_verify_locations(cadata=ssl.DER_cert_to_PEM_cert(der)); return ctx
            tls_context=context(); tls_context.load_cert_chain(cert,key)
            for client in (HTTPConnection("127.0.0.1",port,timeout=5),connection(5,context())):
                try:
                    client.request("GET",API+"info"); r=client.getresponse()
                except (OSError,HTTPException): pass
                else: raise AssertionError(("plaintext or anonymous TLS accepted",r.status))
                finally: client.close()
            assert http("register",dict(peer,fingerprint="A"*64))[0]==403
            result["reject_plaintext_anonymous_and_wrong_identity"]=True
        status,raw=http("info",method="GET"); info=json.loads(raw)
        assert status==200 and info["version"]=="2.2" and info["protocol"]==listener_protocol and info["port"]==53317
        if opt.tls: assert info["fingerprint"]==server_pin
        assert sockets()==[True]*3
        packet=capture.communicate(timeout=20)[0]
        assert info["fingerprint"].encode() in packet and b'"announce":true' in packet
        result["gallery_hidden_and_visible_multicast"]=True
        if opt.tls:
            # From the same test server certificate, advertise a different fingerprint, then the real
            # one. Both must receive a TCP connection; only the matching one may see a registration.
            server=ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
            server.minimum_version=ssl.TLSVersion.TLSv1_2
            server.load_cert_chain(cert2,key2); server.verify_mode=ssl.CERT_REQUIRED
            server.load_verify_locations(cadata=ssl.DER_cert_to_PEM_cert(der))
            for advertised in (peer["fingerprint"],pin2):
                with socket.create_server(("127.0.0.1",0)) as listener, ExitStack() as cleanup:
                    n=listener.getsockname()[1]; tunnel=f"tcp:{n}"
                    run("reverse","--no-rebind",tunnel,tunnel); cleanup.callback(run,"reverse","--remove",tunnel)
                    rule=f"-d {local} -p tcp --dport {n} -j REDIRECT --to-ports {n}"
                    shell("iptables -t nat -I OUTPUT 1 "+rule); cleanup.callback(shell,"iptables -t nat -D OUTPUT "+rule)
                    announce=dict(peer,alias="Pin check",fingerprint=advertised,port=n,protocol="https",announce=True)
                    shell("printf '%s' "+shlex.quote(json.dumps(announce))+" | timeout 1 /system/xbin/nc -u 224.0.0.167 53317 || :")
                    listener.settimeout(12)
                    with listener.accept()[0] as client:
                        client.settimeout(10)
                        try: secure=server.wrap_socket(client,server_side=True)
                        except (ssl.SSLError,ConnectionResetError):
                            if advertised==pin2: raise
                        else:
                            with secure:
                                assert advertised==pin2,"Android completed TLS with a mismatched server certificate"
                                with secure.makefile("rb") as stream:
                                    assert stream.readline(256)==b"POST /api/localsend/v2/register HTTP/1.1\r\n"
                                    length=int(parse_headers(stream)["Content-Length"]); assert 0<length<4096
                                    assert json.loads(stream.read(length))["fingerprint"]==server_pin
                                body=json.dumps(dict(announce,fingerprint=pin2)).encode()
                                secure.sendall(f"HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: {len(body)}\r\n\r\n".encode()+body)
                    if opt.mixed and advertised==pin2:
                        # A newly advertised scheme cannot replace a verified HTTPS address.
                        forged=dict(announce,protocol="http",fingerprint="B"*64)
                        shell("printf '%s' "+shlex.quote(json.dumps(forged))+" | timeout 1 /system/xbin/nc -u 224.0.0.167 53317 || :")
                        listener.settimeout(2)
                        try: unexpected,_=listener.accept()
                        except TimeoutError: pass
                        else:
                            unexpected.close()
                            raise AssertionError("Probed plaintext at a verified HTTPS address")
                        result["verified_https_not_replaced_by_http"]=True
            result["wrong_server_pin_rejected_before_register"]=True
        if not opt.tls:
            for alias,fp,proto,n in [("Obsolete","dev-a","http",26001),("Zed","dev-z","http",26002),
                                     ("Alpha","dev-a","http",26001),("AAA encrypted","E"*64,"https",26003)]:
                assert http("register",dict(peer,alias=alias,fingerprint=fp,protocol=proto,port=n,deviceType="desktop"))[0]==200
            labels=[n.get("text") for n in screen().iter("node") if n.get("resource-id")==PKG+":id/title"]
            assert labels==["AAA encrypted","Alpha","Zed"],labels
            result["peers_deduplicated_and_sorted_by_name"]=True
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
        sender=subprocess.Popen([str(opt.peer),"send",str(port)]+([server_pin] if opt.tls else []),
                                stdout=subprocess.PIPE,stderr=subprocess.STDOUT,env=sender_env)
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
        def check_sessions(pool):
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
            if opt.tls:
                ctx=context(); ctx.load_cert_chain(other,other_key)
                for route,body in (("cancel?sessionId="+ticket["sessionId"],b""),(upload_path(ticket),b"x")):
                    c=connection(8,ctx)
                    try:
                        c.request("POST",API+route,body); assert c.getresponse().status==403
                    finally: c.close()
                result["session_bound_to_client_certificate"]=True
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
            req=offer(wrong,invalid,"image/jpeg")
            broken="header-"+run_id+".jpg"; header=data[:33]  # PNG signature and IHDR, no pixels
            req["files"]["broken"]=dict(offer(broken,header,"image/png")["files"]["one"],id="broken")
            # Same PNG bytes: one sender knows the content type, another trusts the .jpg suffix.
            photos={"by_content":("content-"+run_id+".jpg","image/png"),
                    "by_name":("name-"+run_id+".jpg","image/jpeg")}
            for key,(name,mime) in photos.items():
                req["files"][key]=dict(offer(name,data,mime)["files"]["one"],id=key)
            t=accept(req)
            assert set(t["files"])==set(req["files"]),t
            assert http(upload_path(t),invalid)[0]==415 and wrong not in files()
            assert http(upload_path(t,"broken"),header)[0]==415 and broken not in files()
            for key,(name,_) in photos.items():
                status,body=http(upload_path(t,key),data)
                assert status==200,(name,status,body)
                added.add(name); assert contents(name)==data
            result["disguised_media_rejected_before_publish"]=True
            result["mislabeled_png_received_under_original_name"]=True
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
        if not opt.interop_only:
            with ThreadPoolExecutor(2) as pool:
                check_sessions(pool)
        with tempfile.TemporaryDirectory() as temp:
            root=Path(temp); sample=root/"source.bin"; data=bytes(range(256))*1027; sample.write_bytes(data)
            run("push",str(sample),source)
            store="content://media/external/file"; path=source.replace("/sdcard/","/storage/emulated/0/")
            shell("content insert --uri "+store+" --bind _data:s:"+path+" --bind _display_name:s:source.bin --bind mime_type:s:application/octet-stream")
            row=shell("content query --uri "+store+" --projection _id --where "+shlex.quote("_data='"+path+"'"))
            source_uri=store+"/"+re.search(r"\b_id=(\d+)",row)[1]
            launch(SCREEN,"-f 0x10008000 -a android.intent.action.SEND -t application/octet-stream --grant-read-uri-permission --eu android.intent.extra.STREAM "+source_uri)
            receiver=subprocess.Popen([str(opt.peer),"receive-gated",str(root/"in"),str(root)],stdout=subprocess.PIPE,stderr=subprocess.STDOUT,env=receiver_env)
            children.append(receiver)
            receiver_fp=None
            for line in receiver.stdout:
                if line.startswith(b"FINGERPRINT="): receiver_fp=line.decode().strip().split("=")[1]
                if line.startswith(b"PORT="): break
            target=int(line.strip().split(b"=")[1]); announce=dict(peer,port=target,announce=True)
            if peer_tls:
                assert receiver_fp and len(receiver_fp)==64
                announce["fingerprint"]=receiver_fp
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
        state([True]*3,75 if opt.tls else 8)
        code,raw=http("info",method="GET")
        assert code==200 and json.loads(raw)["fingerprint"]==info["fingerprint"]
        shell("input keyevent 4"); hidden()
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
        if tls_tmp: tls_tmp.cleanup()


if __name__=="__main__":
    main()
