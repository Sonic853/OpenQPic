package com.alensw.video;

import android.app.Service;
import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.provider.OpenableColumns;
import android.util.Log;
import com.alensw.PicFolder.PlayerActivity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.b.e.j;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Serves Android content descriptors to players that need HTTP byte ranges, on loopback only. */
public final class VideoProxyService extends Service {
    private final Handler main=new Handler(Looper.getMainLooper());
    private final Runnable idle=this::stopSelf;
    private int streams;
    private Server server;
    private final Map<String,Uri> grants=new ConcurrentHashMap<>();

    public static void view(Context context, Uri uri, String mime) {
        if("file".equals(uri.getScheme()) && uri.getPath()!=null) {
            try {
                String path=new File(uri.getPath()).getCanonicalPath();
                if(!QuickApp.p.b(path) || QuickApp.p.g(path)) {
                    // Do not turn a private file path into a grant for another app.
                    context.startActivity(new Intent(context,PlayerActivity.class).setDataAndType(uri,mime));
                    return;
                }
                // Reuse the gallery's MediaStore / read-only provider fallback for unindexed videos.
                uri=com.alensw.ui.c.df.a(path,'V');
            } catch(IOException e) { Log.w("VideoProxy","Cannot resolve local video",e); return; }
        }
        Intent intent=new Intent(Intent.ACTION_VIEW).setDataAndType(uri,mime)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(!com.alensw.ui.activity.a.a(context,intent) && "content".equals(uri.getScheme())
                && mime!=null && mime.startsWith("video/")) {
            context.startService(intent.setClass(context,VideoProxyService.class));
        }
    }
    @Override public IBinder onBind(Intent i) { return null; }
    private synchronized void acquire() { streams++; main.removeCallbacks(idle); }
    private synchronized void release() { if(--streams==0) main.postDelayed(idle,30000); }
    @Override public int onStartCommand(Intent i,int flags,int id) {
        if(i==null || !Intent.ACTION_VIEW.equals(i.getAction()) || i.getData()==null) { stopSelf(id); return START_NOT_STICKY; }
        acquire();
        try {
            if(server==null) { server=new Server(); server.c(); }
            if(server.e()==0) throw new IOException("Cannot listen on loopback");
            Uri uri=i.getData(); String name=uri.getLastPathSegment();
            try(Cursor c=getContentResolver().query(uri,new String[]{OpenableColumns.DISPLAY_NAME},null,null,null)) {
                if(c!=null && c.moveToFirst() && !c.isNull(0)) name=c.getString(0);
            }
            String key=UUID.randomUUID().toString(); grants.put(key,uri);
            String base="http://127.0.0.1:"+server.e()+"/video/"+key+"/";
            startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(base+Uri.encode(name)),i.getType())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch(Exception e) { Log.w("VideoProxy","Cannot open video",e); }
        finally { release(); }
        return START_NOT_STICKY;
    }
    @Override public void onDestroy() {
        main.removeCallbacks(idle); if(server!=null) server.d(); grants.clear(); super.onDestroy();
    }
    private final class Server extends j {
        Server() { super(0); }
        protected InetSocketAddress bindAddress(int port) {
            return new InetSocketAddress("127.0.0.1",port);
        }
        @Override protected void a(String path,Map q,HttpRequest req,HttpResponse res,HttpContext ctx) {
            res.setStatusCode(404);
            String[] parts=path.split("/",-1);
            if(parts.length!=4 || !"video".equals(parts[1]) || parts[3].isEmpty() ||
                    ".".equals(parts[3]) || "..".equals(parts[3]) || parts[3].contains("\\")) return;
            Uri uri=grants.get(parts[2]); if(uri==null) return;
            InputStream stream=null; boolean owned=false;
            try {
                String name; long size;
                try(Cursor c=getContentResolver().query(uri,new String[]{OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE},null,null,null)) {
                    if(c==null || !c.moveToFirst()) return;
                    name=c.getString(0); size=c.getLong(1);
                }
                if(!parts[3].equals(name)) {
                    String pathInProvider=com.alensw.b.j.a.b(uri);
                    if(!parts[3].toLowerCase(java.util.Locale.ROOT).matches(".+\\.(srt|vtt|sub|ass|ssa)") ||
                            pathInProvider==null || !pathInProvider.endsWith("/"+name)) return;
                    pathInProvider=pathInProvider.substring(0,pathInProvider.length()-name.length())+parts[3];
                    uri=com.alensw.b.j.a.c(uri.getAuthority(),pathInProvider);
                    try(Cursor c=getContentResolver().query(uri,new String[]{OpenableColumns.SIZE},null,null,null)) {
                        if(c==null || !c.moveToFirst()) return; size=c.getLong(0);
                    }
                }
                String range=a(req,"Range"); long begin=0,end=size-1;
                if(range!=null) {
                    try {
                        if(!range.matches("bytes=(\\d+-\\d*|-\\d+)")) throw new NumberFormatException();
                        String[] bounds=range.substring(6).split("-",-1);
                        if(bounds[0].isEmpty()) begin=Math.max(0,size-Long.parseLong(bounds[1]));
                        else {
                            begin=Long.parseLong(bounds[0]);
                            if(!bounds[1].isEmpty()) end=Math.min(end,Long.parseLong(bounds[1]));
                        }
                        if(begin>end || begin>=size) throw new NumberFormatException();
                    } catch(NumberFormatException invalid) {
                        res.setStatusCode(416); res.addHeader("Content-Range","bytes */"+size); return;
                    }
                    range="bytes="+begin+"-"+end;
                }
                AssetFileDescriptor asset=getContentResolver().openAssetFileDescriptor(uri,"r");
                if(asset==null) return;
                try {
                    FileInputStream in=asset.createInputStream(); stream=in;
                    if(begin>0) in.getChannel().position(asset.getStartOffset()+begin);
                } catch(Exception ex) { asset.close(); throw ex; }
                String mime=getContentResolver().getType(uri);
                acquire(); owned=true;
                InputStream entity=new FilterInputStream(stream) {
                    boolean closed;
                    public void close() throws IOException { if(!closed) { closed=true; try { super.close(); } finally { release(); } } }
                };
                res.setEntity(a(entity,range==null?size:end-begin+1));
                res.addHeader("Accept-Ranges","bytes");
                if(mime!=null) res.addHeader("Content-Type",mime);
                if(range!=null) res.addHeader("Content-Range","bytes "+begin+"-"+end+"/"+size);
                res.setStatusCode(range==null?200:206); stream=null; owned=false;
            } catch(Exception e) { res.setStatusCode(404); }
            finally {
                if(stream!=null) try { stream.close(); } catch(IOException ignored) {}
                if(owned) release();
            }
        }
        @Override protected void b(String path,Map q,HttpRequest req,HttpResponse res,HttpContext ctx) { res.setStatusCode(405); }
    }
}
