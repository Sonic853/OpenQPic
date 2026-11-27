package com.alensw.transfer;

import static com.alensw.b.f.c.webmMimeType;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.system.*;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.alensw.ui.c.ReceivedMediaUpdates;
import org.openqpic.localsend.LocalSend;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

/** Paths, provider grants and publishing are Android's responsibility. */
final class TransferFiles {
    private final Context context;
    private final ContentResolver resolver;
    private final File directory;
    static final String FOLDER="QP Transfer";
    TransferFiles(Context context) {
        this.context=context.getApplicationContext(); resolver=this.context.getContentResolver();
        File base=Build.VERSION.SDK_INT>=29 || context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                : context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        directory=new File(base==null?context.getFilesDir():base,FOLDER);
    }
    String folder() { return Build.VERSION.SDK_INT>=29?"Download/"+FOLDER:directory.toString(); }
    static String basename(String raw) {
        String n=raw.replace('\\','/'); n=n.substring(n.lastIndexOf('/')+1)
                .replaceAll("[\\p{Cntrl}:*?\"<>|]","_").trim().replaceFirst("^[. ]+","");
        if(n.isEmpty()) n="file";
        while(n.getBytes(StandardCharsets.UTF_8).length>220) n=n.substring(0,n.offsetByCodePoints(n.length(),-1));
        return n;
    }
    static boolean allowed(Uri uri) {
        if(uri==null) return false;
        if("content".equals(uri.getScheme())) return uri.getAuthority()!=null;
        if(!"file".equals(uri.getScheme()) || uri.getPath()==null) return false;
        try {
            String path=new File(uri.getPath()).getCanonicalPath();
            String publicRoot=Environment.getExternalStorageDirectory().getCanonicalPath()+File.separator;
            return path.startsWith(publicRoot) && !path.startsWith(publicRoot+"Android/");
        } catch(IOException e) { return false; }
    }
    private String name(Uri uri,CancellationSignal signal) {
        if("content".equals(uri.getScheme())) {
            try(Cursor c=resolver.query(uri,new String[]{OpenableColumns.DISPLAY_NAME},null,null,null,signal)) {
                if(c!=null && c.moveToFirst() && !c.isNull(0)) return basename(c.getString(0));
            } catch(RuntimeException ignored) {}
        }
        return basename(uri.getLastPathSegment()==null?"file":uri.getLastPathSegment());
    }
    LocalSend.Source source(Uri uri,LocalSend.Cancellation cancellation) throws IOException {
        if(!allowed(uri)) throw new IOException("Unreadable URI");
        CancellationSignal signal=new CancellationSignal(); cancellation.onCancel(signal::cancel);
        try { return openSource(uri,cancellation,signal); }
        finally { cancellation.onCancel(null); }
    }
    private LocalSend.Source openSource(Uri uri,LocalSend.Cancellation cancellation,CancellationSignal signal) throws IOException {
        String name=name(uri,signal), type=webmMimeType(name,resolver.getType(uri));
        if(type==null) type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                name.substring(name.lastIndexOf('.')+1).toLowerCase(Locale.ROOT));
        AssetFileDescriptor asset=resolver.openAssetFileDescriptor(uri,"r",signal);
        if(asset==null) throw new FileNotFoundException(name);
        File temp=null; boolean handedOff=false;
        try {
            long offset=asset.getStartOffset(), size=asset.getDeclaredLength();
            boolean regular=false;
            try {
                StructStat st=Os.fstat(asset.getFileDescriptor());
                regular=OsConstants.S_ISREG(st.st_mode);
                if(regular && size<0) size=st.st_size-offset;
                if(regular && (offset<0 || size<0 || offset>st.st_size || size>st.st_size-offset))
                    throw new IOException("Invalid asset bounds");
            } catch(ErrnoException ex) { throw new IOException(ex); }
            if(!regular || size<0) {
                temp=File.createTempFile("ls-source-",".tmp",context.getCacheDir());
                try(InputStream in=asset.createInputStream(); OutputStream out=new FileOutputStream(temp)) {
                    byte[] b=new byte[32768]; int n;
                    StructPollfd ready=new StructPollfd(); ready.fd=asset.getFileDescriptor(); ready.events=(short)OsConstants.POLLIN;
                    StructPollfd[] fds={ready};
                    while(!cancellation.cancelled()) {
                        try { if(Os.poll(fds,250)==0) continue; }
                        catch(ErrnoException ex) { if(ex.errno==OsConstants.EINTR) continue; throw new IOException(ex); }
                        if((n=in.read(b))==-1) break;
                        out.write(b,0,n);
                    }
                    if(cancellation.cancelled()) throw new IOException("Cancelled");
                }
                asset.close(); size=temp.length(); offset=0;
                asset=new AssetFileDescriptor(ParcelFileDescriptor.open(temp,ParcelFileDescriptor.MODE_READ_ONLY),0,size);
            }
            final AssetFileDescriptor keep=asset; final long start=offset; final File staged=temp;
            LocalSend.Item item=new LocalSend.Item(UUID.randomUUID().toString(),name,size,
                    type==null?"application/octet-stream":type,null);
            handedOff=true;
            return new LocalSend.Source() {
                public LocalSend.Item item() { return item; }
                public int fd() { return keep.getParcelFileDescriptor().getFd(); }
                public long offset() { return start; }
                public void close() throws IOException { try { keep.close(); } finally { if(staged!=null) staged.delete(); } }
            };
        } finally { if(!handedOff) try { asset.close(); } finally { if(temp!=null) temp.delete(); } }
    }
    LocalSend.Sink destination(LocalSend.Item item,LocalSend.Cancellation cancellation) throws IOException { return new Destination(item,cancellation); }
    private void indexed(String type) {
        // MediaStoreFolder observes these precise merged collections with notifyForDescendants=false.
        // A Downloads row or a notification for an individual image URI alone won't wake it.
        Uri collection=type.startsWith("image/")?MediaStore.Images.Media.EXTERNAL_CONTENT_URI:
                type.startsWith("video/")?MediaStore.Video.Media.EXTERNAL_CONTENT_URI:MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        try {
            resolver.notifyChange(collection,null,false);
            resolver.notifyChange(com.alensw.PicFolder.GalleryStorage.observerUri(Uri.fromFile(directory)),null,false);
        }
        catch(RuntimeException e) { Log.w("LocalSend","Cannot notify media collection",e); }
    }

    private void finishIndex(Uri uri, String type, String path) {
        if(uri==null) return;
        if("audio/webm".equals(type)) {
            // MediaStore may classify audio-only .webm as video using its extension.
            try {
                Uri file=MediaStore.Files.getContentUri(uri.getPathSegments().get(0),ContentUris.parseId(uri));
                ContentValues audio=new ContentValues();
                audio.put(MediaStore.MediaColumns.MIME_TYPE,type);
                audio.put(MediaStore.Files.FileColumns.MEDIA_TYPE,MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO);
                resolver.update(file,audio,path==null?null:MediaStore.MediaColumns.DATA+"=?",
                        path==null?null:new String[]{path});
            } catch(RuntimeException e) { Log.w("LocalSend","Cannot index WebM audio",e); }
        }
        indexed(type);
    }
    private final class Destination implements LocalSend.Sink {
        final LocalSend.Item item; final String name;
        final LocalSend.Cancellation cancellation;
        File temp; Uri row; ParcelFileDescriptor output;
        boolean committed;
        Destination(LocalSend.Item item,LocalSend.Cancellation cancellation) throws IOException {
            this.item=item; this.cancellation=cancellation; name=basename(item.name);
            try {
                if(Build.VERSION.SDK_INT>=29) {
                    ContentValues v=new ContentValues();
                    v.put(MediaStore.MediaColumns.DISPLAY_NAME,name);
                    v.put(MediaStore.MediaColumns.MIME_TYPE,item.type.contains("/")?item.type:"application/octet-stream");
                    v.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS+"/"+FOLDER);
                    v.put(MediaStore.MediaColumns.IS_PENDING,1);
                    row=resolver.insert(MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),v);
                    if(row==null) throw new IOException("Cannot allocate download");
                    output=resolver.openFileDescriptor(row,"rw");
                } else {
                    if(!directory.isDirectory() && !directory.mkdirs() && !directory.isDirectory()) throw new IOException("Cannot create "+directory);
                    if(directory.getUsableSpace()<item.size) throw new IOException("Not enough storage");
                    temp=File.createTempFile(".ls-",".part",directory);
                    output=ParcelFileDescriptor.open(temp,ParcelFileDescriptor.MODE_READ_WRITE);
                }
                if(output==null) throw new IOException("Cannot open download");
            } catch(IOException | RuntimeException e) { close(); throw e; }
        }
        public int fd() { return output.getFd(); }
        public boolean isMedia() throws IOException {
            try(ParcelFileDescriptor p=row==null?ParcelFileDescriptor.open(temp,ParcelFileDescriptor.MODE_READ_ONLY):
                    resolver.openFileDescriptor(row,"r")) {
                return p!=null && TransferMedia.readable(p,item,resolver,row==null?Uri.fromFile(temp):row);
            }
        }
        public boolean matches(String expected) throws IOException {
            try(InputStream in=row==null?new FileInputStream(temp):resolver.openInputStream(row)) {
                if(in==null) throw new IOException("Cannot verify download");
                MessageDigest digest=MessageDigest.getInstance("SHA-256");
                byte[] buf=new byte[32768]; int n;
                while((n=in.read(buf))!=-1) {
                    if(cancellation.cancelled()) throw new IOException("Cancelled");
                    digest.update(buf,0,n);
                }
                byte[] actual=digest.digest();
                for(int i=0;i<actual.length;i++) if((actual[i]&255)!=Integer.parseInt(expected.substring(i*2,i*2+2),16)) return false;
                return true;
            } catch(java.security.NoSuchAlgorithmException ex) { throw new IOException(ex); }
        }
        public void commit() throws IOException {
            output.close(); output=null;
            File saved=null;
            if(row!=null) {
                ContentValues v=new ContentValues(); v.put(MediaStore.MediaColumns.IS_PENDING,0);
                if(resolver.update(row,v,null,null)!=1) throw new IOException("Cannot publish download");
            } else {
                int dot=name.lastIndexOf('.'); String stem=dot>0?name.substring(0,dot):name, ext=dot>0?name.substring(dot):"";
                File target=new File(directory,name);
                for(int i=1;!target.createNewFile();i++) target=new File(directory,stem+" ("+i+")"+ext);
                if(!temp.renameTo(target)) { target.delete(); throw new IOException("Cannot publish "+name); }
                saved=target;
            }
            committed=true;
            if(item.type.startsWith("image/") || item.type.startsWith("video/")) ReceivedMediaUpdates.published(directory);
            if(saved==null) finishIndex(row,item.type,null); // IS_PENDING has already been cleared
            else try {
                String type=item.type;
                MediaScannerConnection.scanFile(context,new String[]{saved.toString()},new String[]{type},
                        (path,uri)->finishIndex(uri,type,path));
            } catch(RuntimeException e) { Log.w("LocalSend","Cannot request media scan",e); }
        }
        public void close() throws IOException {
            try { if(output!=null) output.close(); } finally {
                if(!committed && row!=null) resolver.delete(row,null,null);
                if(temp!=null) temp.delete();
            }
        }
    }
}
