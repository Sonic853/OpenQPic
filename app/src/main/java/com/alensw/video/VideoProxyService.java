package com.alensw.video;

import static com.alensw.media.MediaFileTypes.webmMimeType;

import android.app.Service;
import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.provider.OpenableColumns;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import com.alensw.PicFolder.PlayerActivity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.documents.DocumentProviderAccess;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.page.MediaDetails;
import org.openqpic.localsend.LoopbackHttp;
import org.openqpic.localsend.SystemTls;
import android.content.pm.ResolveInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Serves Android content descriptors to players that need HTTP byte ranges, on loopback only. */
public final class VideoProxyService extends Service {
    private final Handler main=new Handler(Looper.getMainLooper());
    private final Runnable idle=this::stopIfIdle;
    private int streams;
    private boolean destroyed;
    private LoopbackHttp externalServer, videoServer;
    private final java.util.concurrent.ExecutorService opens=java.util.concurrent.Executors.newSingleThreadExecutor();
    private static volatile VideoProxyService current;
    private static final String INTERNAL_PLAYER="com.alensw.video.INTERNAL_PLAYER";
    private final Map<String,Playback> grants=new ConcurrentHashMap<>();
    private final Map<String,Playback> internalGrants=new ConcurrentHashMap<>();

    private static final class Playback {
        final Uri uri;
        final String webmType;
        Playback(Uri uri,String mime) {
            this.uri=uri;
            this.webmType="video/webm".equals(mime) || "audio/webm".equals(mime) ? mime : null;
        }
    }

    /** A content provider may be readable but not directly seekable by the platform player. */
    public static boolean retryInPlayer(Context context,Uri uri,String mime) {
        if(uri==null || !"content".equals(uri.getScheme())) return false;
        try {
            if(mime==null) mime=context.getContentResolver().getType(uri);
            mime=webmMimeType(context.getContentResolver(),uri,mime);
            if(mime==null || !mime.startsWith("video/")) return false;
            context.startService(new Intent(context,VideoProxyService.class).setAction(Intent.ACTION_VIEW)
                    .setDataAndType(uri,mime).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).putExtra(INTERNAL_PLAYER,true));
            return true;
        } catch(RuntimeException denied) { return false; }
    }

    public static void view(Context context, Uri uri, String mime) {
        ContentResolver resolver=context.getContentResolver();
        if(mime==null && "content".equals(uri.getScheme())) {
            try { mime=resolver.getType(uri); } catch(RuntimeException unavailable) {}
        }
        mime=webmMimeType(resolver,uri,mime);
        if("file".equals(uri.getScheme()) && uri.getPath()!=null) {
            try {
                String path=new File(uri.getPath()).getCanonicalPath();
                if(!QuickApp.p.b(path) || QuickApp.p.g(path)) {
                    // Do not turn a private file path into a grant for another app.
                    context.startActivity(new Intent(context,PlayerActivity.class).setDataAndType(uri,mime));
                    return;
                }
                // Reuse the gallery's MediaStore / read-only provider fallback for unindexed videos.
                uri=MediaDetails.a(path,'V');
            } catch(IOException e) { Log.w("VideoProxy","Cannot resolve local video",e); return; }
        }
        Intent intent=new Intent(Intent.ACTION_VIEW).setDataAndType(uri,mime)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(!GalleryActions.a(context,intent) && "content".equals(uri.getScheme())
                && mime!=null && mime.startsWith("video/")) {
            context.startService(intent.setClass(context,VideoProxyService.class));
        }
    }
    @Override public IBinder onBind(Intent i) { return null; }
    private synchronized boolean acquire() {
        if(destroyed) return false;
        streams++; main.removeCallbacks(idle);
        return true;
    }
    private synchronized void release() { if(--streams==0 && !destroyed) main.postDelayed(idle,30000); }
    private synchronized void stopIfIdle() { if(streams==0 && !destroyed) stopSelf(); }
    @Override public int onStartCommand(Intent i,int flags,int id) {
        if(i==null || !Intent.ACTION_VIEW.equals(i.getAction()) || i.getData()==null) { stopSelf(id); return START_NOT_STICKY; }
        if(!acquire()) return START_NOT_STICKY;
        opens.execute(()->openPlayback(i));
        return START_NOT_STICKY;
    }
    private void openPlayback(Intent i) {
        try {
            Uri uri=i.getData(); String name=uri.getLastPathSegment();
            try(Cursor c=getContentResolver().query(uri,new String[]{OpenableColumns.DISPLAY_NAME},null,null,null)) {
                if(c!=null && c.moveToFirst() && !c.isNull(0)) name=c.getString(0);
            }
            String type=i.getType()==null ? getContentResolver().getType(uri) : i.getType();
            String mime=webmMimeType(name,type);
            Playback source=new Playback(uri,mime);
            String key=UUID.randomUUID().toString();
            // Choose using the old HTTP filter, before disclosing a capability. If the
            // system chooser selects our player later, PlayerView upgrades that grant.
            Intent probe=new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse("http://127.0.0.1/video"),mime);
            ResolveInfo target=getPackageManager().resolveActivity(probe,android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
            boolean internal=i.getBooleanExtra(INTERNAL_PLAYER,false) ||
                    (target!=null && target.activityInfo!=null && getPackageName().equals(target.activityInfo.packageName));
            synchronized(this) { if(destroyed) return; current=this; }
            Uri url;
            if(internal) {
                url=secureUrl(source,name);
                probe.setClass(this,PlayerActivity.class);
            } else {
                synchronized(this) {
                    if(destroyed) return;
                    if(externalServer==null || !externalServer.isOpen()) externalServer=new LoopbackHttp((p,r,h,o)->serve(grants,p,r,h,o));
                    grants.put(key,source);
                    url=url("http",externalServer.port(),key,name);
                }
            }
            synchronized(this) { if(!destroyed) startActivity(probe.setDataAndType(url,mime).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); }
        } catch(Exception e) { Log.w("VideoProxy","Cannot open video",e); }
        finally { release(); }
    }
    @Override public void onDestroy() {
        synchronized(this) { destroyed=true; main.removeCallbacks(idle); }
        opens.shutdownNow();
        if(current==this) current=null;
        if(externalServer!=null) externalServer.close();
        if(videoServer!=null) videoServer.close();
        grants.clear();
        internalGrants.clear();
        super.onDestroy();
    }

    private static Uri url(String scheme,int port,String key,String name) {
        return new Uri.Builder().scheme(scheme).encodedAuthority("127.0.0.1:"+port).appendPath("video").appendPath(key).appendPath(name).build();
    }
    private synchronized Uri secureUrl(Playback source,String name) throws IOException {
        if(destroyed) throw new IOException("Video service stopped");
        if(videoServer==null || !videoServer.isOpen())
            videoServer=new LoopbackHttp((p,r,h,o)->serve(internalGrants,p,r,h,o),SystemTls.video());
        String key=UUID.randomUUID().toString();
        internalGrants.put(key,source);
        return url("https",videoServer.port(),key,name);
    }
    /** Only our live listener and an issued grant qualify. Never intercept arbitrary HTTPS URLs. */
    static boolean ownsPlayback(Uri uri) {
        try { return managedUrl(uri,false)!=null; } catch(IOException impossible) { return false; }
    }
    static Uri forInternalPlayer(Uri uri) throws IOException { return managedUrl(uri,true); }
    private static Uri managedUrl(Uri uri,boolean upgrade) throws IOException {
        VideoProxyService s=current;
        if(s==null || uri==null || !"127.0.0.1".equals(uri.getHost()) || uri.getUserInfo()!=null ||
                uri.getQuery()!=null || uri.getFragment()!=null) return null;
        java.util.List<String> parts=uri.getPathSegments();
        if(parts.size()!=3 || !"video".equals(parts.get(0))) return null;
        synchronized(s) {
            if("https".equals(uri.getScheme()) && s.videoServer!=null && uri.getPort()==s.videoServer.port()
                    && s.internalGrants.containsKey(parts.get(1))) return uri;
            if("http".equals(uri.getScheme()) && s.externalServer!=null && uri.getPort()==s.externalServer.port()) {
                Playback src=s.grants.get(parts.get(1));
                if(src!=null) return upgrade?s.secureUrl(src,parts.get(2)):uri;
            }
            return null;
        }
    }
    static AutoCloseable retain(Uri uri) throws IOException {
        VideoProxyService s=current;
        if(s==null || !"https".equals(uri.getScheme()) || !ownsPlayback(uri) || !s.acquire()) throw new IOException("Expired playback grant");
        return s::release;
    }

    private void serve(Map<String,Playback> allowed,String path, String range, boolean head, LoopbackHttp.Reply reply) throws IOException {
        int query=path.indexOf('?');
        String[] parts=Uri.decode(query<0 ? path : path.substring(0,query)).split("/",-1);
        if(parts.length!=4 || !parts[0].isEmpty() || !"video".equals(parts[1]) || parts[3].isEmpty() ||
                ".".equals(parts[3]) || "..".equals(parts[3]) || parts[3].contains("\\") || parts[3].indexOf('\0')>=0) return;
        Playback source=allowed.get(parts[2]);
        if(source==null || !acquire()) return;
        Uri uri=source.uri;
        try {
            String name; long size;
            try(Cursor c=getContentResolver().query(uri,new String[]{OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE},null,null,null)) {
                if(c==null || !c.moveToFirst() || c.isNull(0)) return;
                name=c.getString(0); size=c.isNull(1) ? -1 : c.getLong(1);
            }
            if(!parts[3].equals(name)) {
                if(!parts[3].toLowerCase(Locale.ROOT).matches(".+\\.(srt|vtt|sub|ass|ssa)")) return;
                String pathInProvider=DocumentProviderAccess.b(uri);
                if(pathInProvider==null || !pathInProvider.endsWith("/"+name)) return;
                pathInProvider=pathInProvider.substring(0,pathInProvider.length()-name.length())+parts[3];
                uri=DocumentProviderAccess.c(uri.getAuthority(),pathInProvider);
                try(Cursor c=getContentResolver().query(uri,new String[]{OpenableColumns.SIZE},null,null,null)) {
                    if(c==null || !c.moveToFirst()) return;
                    size=c.isNull(0) ? -1 : c.getLong(0);
                }
            }
            try(AssetFileDescriptor asset=getContentResolver().openAssetFileDescriptor(uri,"r")) {
                if(asset==null) return;
                long assetSize=asset.getLength(), offset=asset.getStartOffset();
                if(assetSize>=0) size=size<0 ? assetSize : Math.min(size,assetSize);
                if(size<0 || offset<0 || offset>Long.MAX_VALUE-size) return;
                long begin=0,end=size-1;
                if(range!=null) {
                    try {
                        range=range.trim();
                        if(!range.matches("bytes=(\\d+-\\d*|-\\d+)")) throw new NumberFormatException();
                        String[] bounds=range.substring(6).split("-",-1);
                        if(bounds[0].isEmpty()) begin=Math.max(0,size-Long.parseLong(bounds[1]));
                        else {
                            begin=Long.parseLong(bounds[0]);
                            if(!bounds[1].isEmpty()) end=Math.min(end,Long.parseLong(bounds[1]));
                        }
                        if(begin>end || begin>=size) throw new NumberFormatException();
                    } catch(NumberFormatException invalid) {
                        reply.headers(416,0,null,"bytes */"+size,false);
                        return;
                    }
                }
                boolean seekable=isSeekable(asset);
                if(!seekable && (offset!=0 || begin!=0)) return;
                long length=end-begin+1;
                String mime=parts[3].equals(name) ? source.webmType : null;
                if(mime==null) mime=webmMimeType(parts[3],getContentResolver().getType(uri));
                reply.headers(range==null?200:206,length,mime,
                        range==null?null:"bytes "+begin+"-"+end+"/"+size,seekable);
                if(head || length==0) return;
                if(seekable) {
                    reply.file(asset.getParcelFileDescriptor().getFd(),offset+begin,length);
                } else try(InputStream in=asset.createInputStream()) {
                    reply.stream(in,length);
                }
            }
        } finally { release(); }
    }

    private static boolean isSeekable(AssetFileDescriptor asset) {
        try { Os.lseek(asset.getFileDescriptor(),0,OsConstants.SEEK_CUR); return true; }
        catch(ErrnoException e) { return false; }
    }
}
