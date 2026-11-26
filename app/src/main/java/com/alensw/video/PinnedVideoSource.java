package com.alensw.video;

import android.media.MediaDataSource;
import android.net.Uri;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import javax.net.ssl.SSLSocket;
import org.openqpic.localsend.SystemTls;

/** Seekable MediaPlayer input; only created for a live grant owned by VideoProxyService. */
final class PinnedVideoSource extends MediaDataSource {
    private static final int WINDOW=256*1024;
    private final Uri url;
    private final AutoCloseable lease;
    private long size=-1, start=-1, next;
    private boolean seekable;
    private final Object life=new Object();
    private SSLSocket active;
    private InputStream stream;
    private byte[] cache=new byte[0];
    private volatile boolean closed;

    PinnedVideoSource(Uri url) throws IOException {
        this.url=url;
        lease=VideoProxyService.retain(url);
    }
    private static String line(InputStream in,int max) throws IOException {
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        while(b.size()<max) {
            int n=in.read();
            if(n=='\r' && in.read()=='\n') return b.toString("US-ASCII");
            if(n<0x20 && n!='\t') throw new IOException("Invalid video HTTP header");
            b.write(n);
        }
        throw new IOException("Video HTTP header too large");
    }
    private void open(long at) throws IOException {
        SSLSocket old;
        synchronized(life) {
            if(closed) throw new IOException("Video source closed");
            old=active; active=null;
        }
        if(old!=null) old.close();
        stream=null;
        boolean head=at<0, partial=!head && seekable, keep=false;
        SSLSocket socket=SystemTls.video().openVideo(url.getPort());
        try {
            synchronized(life) { if(closed) throw new IOException("Video source closed"); active=socket; }
            String path=url.getEncodedPath();
            if(path==null || !path.matches("/video/[a-fA-F0-9-]+/[^\\s\\r\\n]+")) throw new IOException("Invalid video URL");
            String request=(head?"HEAD ":"GET ")+path+" HTTP/1.1\r\nHost: 127.0.0.1:"+url.getPort()+
                    "\r\nConnection: close\r\n"+(partial?"Range: bytes="+at+"-\r\n":"")+"\r\n";
            socket.getOutputStream().write(request.getBytes(StandardCharsets.US_ASCII));
            InputStream in=new BufferedInputStream(socket.getInputStream(),16384);
            String status=line(in,256);
            if(!status.startsWith(partial?"HTTP/1.1 206 ":"HTTP/1.1 200 ")) throw new IOException("Video request failed: "+status);
            HashMap<String,String> headers=new HashMap<>();
            for(int budget=16384;;) {
                String s=line(in,budget); if(s.isEmpty()) break; budget-=s.length()+2;
                int colon=s.indexOf(':'); if(colon<=0) throw new IOException("Invalid video response");
                headers.put(s.substring(0,colon).toLowerCase(java.util.Locale.ROOT),s.substring(colon+1).trim());
            }
            long n;
            try { n=Long.parseLong(headers.get("content-length")); } catch(RuntimeException e) { throw new IOException("Invalid video length",e); }
            if(head) {
                if(n<0) throw new IOException("Unknown video length");
                size=n; seekable="bytes".equals(headers.get("accept-ranges")); return;
            }
            if(n!=(partial?size-at:size)) throw new IOException("Video source length changed");
            if(partial && !("bytes "+at+"-"+(size-1)+"/"+size).equals(headers.get("content-range")))
                throw new IOException("Incorrect video range");
            stream=in; next=partial?at:0; keep=true;
        } finally {
            if(!keep) {
                synchronized(life) { if(active==socket) active=null; }
                socket.close();
            }
        }
    }
    @Override public synchronized long getSize() throws IOException {
        if(size<0) open(-1); return size;
    }
    @Override public synchronized int readAt(long at,byte[] buffer,int offset,int n) throws IOException {
        if(closed || at<0) throw new IOException("Invalid video read");
        if(n==0) return 0;
        if(at>=getSize()) return -1;
        if(at<start || at-start>=cache.length) {
            int chunk=(int)Math.min(WINDOW,size-at);
            // Sequential playback retains the same TLS connection across cache windows.
            // Seekable files reopen at the new offset; pipes can only reopen from zero.
            if(stream==null || at<next || (seekable && at-next>WINDOW)) open(at);
            byte[] data=new byte[chunk]; DataInputStream input=new DataInputStream(stream);
            while(next<at) {
                int skip=(int)Math.min(data.length,at-next);
                input.readFully(data,0,skip); next+=skip;
            }
            input.readFully(data); next+=chunk; cache=data; start=at;
        }
        int i=(int)(at-start), count=Math.min(n,cache.length-i);
        System.arraycopy(cache,i,buffer,offset,count); return count;
    }
    @Override public void close() throws IOException {
        final SSLSocket socket;
        synchronized(life) { if(closed) return; closed=true; socket=active; active=null; }
        try {
            if(socket!=null) {
                if(android.os.Looper.myLooper()==android.os.Looper.getMainLooper()) {
                    Thread t=new Thread(()->{ try { socket.close(); } catch(IOException ignored) {} },"QP video TLS close");
                    t.setDaemon(true); t.start();
                } else socket.close();
            }
        } finally { try { lease.close(); } catch(Exception e) { throw new IOException(e); } }
    }
}
