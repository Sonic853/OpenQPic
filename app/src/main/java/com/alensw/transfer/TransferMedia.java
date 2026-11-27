package com.alensw.transfer;

import static com.alensw.b.f.c.webmMimeType;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import com.alensw.b.h.AnimatedWebp;
import org.openqpic.localsend.LocalSend;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Admission uses metadata; publishing requires independent evidence from the stored bytes. */
final class TransferMedia {
    private static final Map<String,String> extensions=new HashMap<>();
    static {
        add("image/jpeg","jpg jpeg jpe"); add("image/png","png apng"); add("image/gif","gif");
        add("image/webp","webp"); add("image/bmp","bmp"); add("image/heif","heif heic");
        add("image/avif","avif"); add("image/tiff","tif tiff"); add("image/x-adobe-dng","dng");
        add("video/mp4","mp4 m4v"); add("video/quicktime","mov"); add("video/3gpp","3gp 3gpp");
        add("video/3gpp2","3g2"); add("video/webm","webm"); add("video/x-matroska","mkv");
        add("video/x-msvideo","avi"); add("video/mpeg","mpg mpeg"); add("video/mp2t","ts m2ts");
        add("audio/mpeg","mp3 mp2"); add("audio/mp4","m4a m4b"); add("audio/aac","aac adts");
        add("audio/flac","flac"); add("audio/ogg","ogg oga opus"); add("audio/wav","wav wave");
        add("audio/amr","amr"); add("audio/amr-wb","awb"); add("audio/x-matroska","mka");
        add("audio/webm","weba");
    }
    private static void add(String mime,String suffixes) {
        for(String ext:suffixes.split(" ")) extensions.put(ext,mime);
    }
    private static String normal(String type) {
        String mime=type.split(";",2)[0].trim().toLowerCase(Locale.ROOT);
        switch(mime) {
            case "image/jpg": case "image/pjpeg": return "image/jpeg";
            case "image/heic": return "image/heif";
            case "image/x-ms-bmp": return "image/bmp";
            case "image/apng": case "image/x-png": return "image/png";
            case "audio/x-wav": case "audio/wave": case "audio/vnd.wave": return "audio/wav";
            case "audio/x-flac": return "audio/flac";
            case "audio/x-m4a": return "audio/mp4";
            case "audio/opus": case "application/ogg": return "audio/ogg";
            case "video/x-m4v": return "video/mp4";
            default: return mime;
        }
    }
    static String offered(LocalSend.Item item) {
        if(item.size<=0) return null;
        String name=TransferFiles.basename(item.name);
        String mime=webmMimeType(name,normal(item.type));
        int dot=name.lastIndexOf('.');
        String ext=dot<0?null:extensions.get(name.substring(dot+1).toLowerCase(Locale.ROOT));
        if(dot>=0 && ext==null) return null;
        if("application/octet-stream".equals(mime) || "binary/octet-stream".equals(mime)) return ext;
        if(ext!=null && mime.equals(ext.substring(0,ext.indexOf('/')+1)+"*")) return ext;
        if(!extensions.containsValue(mime)) return null;
        if(ext==null || ext.equals(mime)) return mime;
        // WebM and MP4 can carry just audio, independently of their common suffix.
        return ("video/mp4".equals(ext) && "audio/mp4".equals(mime)) ||
                ("video/webm".equals(ext) && "audio/webm".equals(mime)) ? mime:null;
    }
    static boolean readable(ParcelFileDescriptor file,LocalSend.Item item,ContentResolver resolver,Uri uri) throws IOException {
        if(item.type.startsWith("image/")) {
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inJustDecodeBounds=true;
            BitmapFactory.decodeFileDescriptor(file.getFileDescriptor(),null,opts);
            if(opts.outWidth>0 && opts.outHeight>0 && opts.outMimeType!=null && item.type.equals(normal(opts.outMimeType))) {
                opts.inJustDecodeBounds=false; opts.inSampleSize=1;
                while(Math.max(opts.outWidth,opts.outHeight)/(long)opts.inSampleSize>128) opts.inSampleSize*=2;
                Bitmap sample=BitmapFactory.decodeFileDescriptor(file.getFileDescriptor(),null,opts);
                if(sample!=null) {
                    sample.recycle();
                    return true;
                }
            }
            return Build.VERSION.SDK_INT>=28 && "image/webp".equals(item.type) && AnimatedWebp.readable(resolver,uri);
        }
        MediaExtractor probe=new MediaExtractor();
        try {
            probe.setDataSource(file.getFileDescriptor(),0,item.size);
            String kind=item.type.startsWith("audio/")?"audio/":"video/";
            for(int n=0;n<probe.getTrackCount();n++) {
                String format=probe.getTrackFormat(n).getString(MediaFormat.KEY_MIME);
                if(format!=null && format.startsWith(kind)) {
                    probe.selectTrack(n);
                    if(probe.getSampleTime()>=0) return true;
                    probe.unselectTrack(n);
                }
            }
            return false;
        } catch(IOException | RuntimeException invalid) { return false; }
        finally { probe.release(); }
    }
    private TransferMedia() {}
}
