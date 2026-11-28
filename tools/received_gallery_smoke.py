"""Verify real album/grid updates without reopening GalleryActivity (isolated API 23 emulator)."""
import argparse
from concurrent.futures import ThreadPoolExecutor
import hashlib
from http.client import HTTPConnection
import json
from pathlib import Path
import re
import shlex
import struct
import subprocess
import tempfile
import time
import uuid
import xml.etree.ElementTree as ET
import zlib

PKG="com.alensw.PicFolder"
DIR="/storage/emulated/0/Download/QP Transfer"
BASE="/api/localsend/v2/"


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--serial",required=True)
    p.add_argument("--folder-only",action="store_true",help="seed the folder instead of verifying album discovery")
    p.add_argument("--mp4",type=Path,help="Also receive a real video while the gallery is paused, then return")
    p.add_argument("--report",type=Path)
    opt=p.parse_args()
    adb=["adb","-s",opt.serial]
    def run(*args): return subprocess.check_output([*adb,*args],timeout=35)
    def sh(command): return run("shell",command).decode("utf8","replace").replace("\r","").strip()
    assert sh("getprop ro.kernel.qemu")=="1" and sh("getprop ro.build.version.sdk")=="23"
    tag=uuid.uuid4().hex[:10]; dump="/data/local/tmp/oqp-refresh-"+tag+".xml"
    def tree():
        sh("uiautomator dump "+dump)
        return ET.fromstring(run("exec-out","cat",dump))
    def focused():
        text=sh("dumpsys activity activities | grep mResumedActivity")
        m=re.search(r"ActivityRecord\{(\w+) u0 "+re.escape(PKG)+r"/\.GalleryActivity ",text)
        return m[1] if m else None
    def title(wanted):
        names=[]
        for _ in range(5):
            names=[n.get("text") for n in tree().iter("node") if n.get("text")]
            if wanted in names: return
        raise AssertionError(("visible gallery title",wanted,names))
    def tap_node(key,value):
        for _ in range(3):
            n=next((n for n in tree().iter("node") if n.get(key)==value),None)
            if n is not None:
                x,y,r,b=map(int,re.findall(r"\d+",n.get("bounds")));sh(f"input tap {(x+r)//2} {(y+b)//2}"); return
        raise AssertionError(("missing UI control",key,value))
    def switch_root(label):
        tap_node("resource-id","android:id/up"); tap_node("text",label); title(label)
    local=re.search(r"inet ([0-9.]+)/",sh("ip -4 addr show eth0"))[1]
    sockets=sh("cat /proc/net/tcp /proc/net/tcp6"); rules=sh("iptables -t nat -S OUTPUT")
    transit=next(p for p in range(30220,30320) if f":{p:04X} " not in sockets and f"--dport {p} " not in rules)
    route=f"-d 127.0.0.1 -p tcp --dport {transit} -j DNAT --to-destination {local}:53317"
    names=[]
    def chunk(t,b): return struct.pack(">I",len(b))+t+b+struct.pack(">I",zlib.crc32(t+b))
    pink=(228,25,150); green=(22,225,136)
    def png(rgb):
        return (b"\x89PNG\r\n\x1a\n"+chunk(b"IHDR",struct.pack(">2I5B",96,96,8,2,0,0,0))+
                chunk(b"tEXt",b"Comment\0"+b"Q"*8000)+chunk(b"IDAT",zlib.compress((b"\0"+bytes(rgb)*96)*96))+chunk(b"IEND",b""))
    def indexed(name,mime,size):
        table="content://media/external/"+("video" if mime=="video/mp4" else "images")+"/media"
        where=shlex.quote("_data='"+DIR+"/"+name+"'")
        for _ in range(24):
            match=re.search(r"_id=(\d+), _size=(\d+), mime_type=([^\s,]+)",
                            sh(f"content query --uri {table} --projection _id:_size:mime_type --where {where}"))
            if match and int(match[2])==size and match[3]==mime: return
            time.sleep(.15)
        raise AssertionError("Media not indexed: "+name)
    def marker(color):
        # The original ImageGridView paints on a Canvas rather than exposing accessibility children.
        # Find the fixture's unmistakable thumbnail in the *rendered* grid, below the action bar.
        raw=run("exec-out","screencap"); w,h,fmt=struct.unpack_from("<III",raw)
        bpp={1:4,2:4,4:2}[fmt]; assert len(raw)==12+bpp*w*h
        coords=[]
        for y in range(h//5,h-12,3):
            for x in range(0,w,3):
                pos=12+bpp*(y*w+x)
                if fmt==4:  # HAL_PIXEL_FORMAT_RGB_565 on the ARM emulator
                    v=struct.unpack_from("<H",raw,pos)[0]; r,g,b=(v>>11)*255//31,((v>>5)&63)*255//63,(v&31)*255//31
                else: r,g,b=raw[pos:pos+3]
                if abs(r-color[0])<18 and abs(g-color[1])<18 and abs(b-color[2])<18: coords.append((x,y))
        if len(coords)>20: return coords[len(coords)//2]
    def visible(color):
        end=time.monotonic()+12
        while not (point:=marker(color)):
            assert time.monotonic()<end,"Published thumbnail did not appear in the open gallery"
            time.sleep(.2)
        return point
    def deliver(name,expected_title,data,mime,color=None,finish_before_back=False):
        current=focused(); assert current
        title(expected_title)
        sh("am start -W -n "+PKG+"/com.alensw.transfer.TransferActivity")
        end=time.monotonic()+8
        while ":D045 " not in sh("cat /proc/net/udp"):
            assert time.monotonic()<end; time.sleep(.1)
        req={"info":{"alias":"Refresh fixture","version":"2.2","fingerprint":tag,"port":27117,"protocol":"http"},
             "files":{"photo":{"id":"photo","fileName":name,"fileType":mime,"size":len(data)}}}
        def prepare():
            c=HTTPConnection("127.0.0.1",port,timeout=45)
            try:
                c.request("POST",BASE+"prepare-upload",json.dumps(req),{"Content-Type":"application/json"})
                r=c.getresponse(); raw=r.read(); assert r.status==200,(r.status,raw); return json.loads(raw)
            finally: c.close()
        with ThreadPoolExecutor(1) as pool:
            future=pool.submit(prepare)
            tap_node("resource-id",PKG+":id/ls_accept")
            ticket=future.result()
        c=HTTPConnection("127.0.0.1",port,timeout=12)
        try:
            c.putrequest("POST",BASE+"upload?sessionId="+ticket["sessionId"]+"&fileId=photo&token="+ticket["files"]["photo"])
            c.putheader("Content-Length",str(len(data)));c.endheaders();c.send(data[:256])
            if not finish_before_back:
                sh("input keyevent 4");title(expected_title);assert focused()==current
                if color: assert not marker(color),"partial file already visible"
            c.send(data[256:]);r=c.getresponse();body=r.read();assert r.status==200,(r.status,body)
        finally: c.close()
        indexed(name,mime,len(data))
        if finish_before_back:
            sh("input keyevent 4")
            end=time.monotonic()+8
            while focused()!=current:
                assert time.monotonic()<end;time.sleep(.15)
        assert focused()==current
    sh("mkdir -p "+shlex.quote(DIR))
    assert not sh("ls -a "+shlex.quote(DIR)).strip(".\n"),"Use an empty test receive folder"
    sh("iptables -t nat -I OUTPUT 1 "+route);port=int(run("forward","tcp:0","tcp:"+str(transit)))
    try:
        sh("am force-stop "+PKG);seed="refresh-"+tag+"-one.png";names.append(seed)
        if opt.folder_only:
            with tempfile.TemporaryDirectory() as td:
                f=Path(td)/seed;f.write_bytes(png(pink));run("push",str(f),DIR+"/"+seed)
            sh("am start -W -n "+PKG+"/.GalleryActivity -d file:///storage/emulated/0/Download/QP%20Transfer")
        else:
            sh("am start -W -n "+PKG+"/.GalleryActivity")
            if any(n.get("resource-id")==PKG+":id/drawer_list" for n in tree().iter("node")):
                sh("input keyevent 4")  # The first launch of each version demonstrates the drawer.
            title("Albums")
            record=focused(); deliver(seed,"Albums",png(pink),"image/png",pink)
            point=visible(pink)
            assert focused()==record;sh(f"input tap {point[0]} {point[1]}")
        title("QP Transfer (1)"); record=focused()
        visible(pink)
        second="refresh-"+tag+"-two.jpg";names.append(second);deliver(second,"QP Transfer (1)",png(green),"image/png",green)
        title("QP Transfer (2)"); visible(green); visible(pink); assert focused()==record
        counts=[1,2]
        if opt.mp4:
            video="refresh-"+tag+".mp4"; names.append(video)
            deliver(video,"QP Transfer (2)",opt.mp4.read_bytes(),"video/mp4",finish_before_back=True)
            title("QP Transfer (3)"); assert focused()==record; counts.append(3)
        if not opt.folder_only:
            sh("input keyevent 4"); title("Albums"); switch_root("Moments")
            purple=(158,34,234); moment="refresh-"+tag+"-moment.png";names.append(moment)
            try:
                record=focused(); deliver(moment,"Moments",png(purple),"image/png",purple)
                visible(purple); assert focused()==record
            finally: switch_root("Albums")
        base=sh("pm path "+PKG).removeprefix("package:")
        result={"visible_album_created":not opt.folder_only,"open_folder_count":counts,"same_gallery_activity":True,
                "partial_hidden_and_new_thumbnail_drawn":True,"video_received_while_paused":bool(opt.mp4),
                "moments_thumbnail_updated":not opt.folder_only,"media_store_image_indexed":True,
                "device":sh("getprop ro.build.fingerprint"),"apk_sha256":hashlib.sha256(run("exec-out","cat",base)).hexdigest()}
        if opt.report:
            opt.report.parent.mkdir(parents=True,exist_ok=True)
            opt.report.write_text(json.dumps(result,indent=2)+"\n","utf8")
        print(json.dumps(result))
    finally:
        sh("am force-stop "+PKG);run("forward","--remove",f"tcp:{port}");sh("iptables -t nat -D OUTPUT "+route)
        for name in names:
            path=DIR+"/"+name
            sh("content delete --uri content://media/external/file --where "+shlex.quote("_data='"+path+"'"))
            sh("rm -f "+shlex.quote(path))
        sh("rm -f "+dump)


if __name__=="__main__":
    main()
