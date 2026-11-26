package org.openqpic.videoprobe;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/** Emulator-only third-party HTTP player probe. No storage permissions or custom TLS trust. */
public final class ProbePlayer extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Uri uri=getIntent().getData();
        if(uri==null || !"http".equals(uri.getScheme()) || !"127.0.0.1".equals(uri.getHost())) { finish(); return; }
        setTitle("HTTP playback fixture");
        new Thread(()->{
            HttpURLConnection c=null;
            try {
                c=(HttpURLConnection)new URL(uri.toString()).openConnection();
                c.setInstanceFollowRedirects(false); c.setReadTimeout(5000); c.setConnectTimeout(5000);
                c.setRequestProperty("Range","bytes=0-255");
                if(c.getResponseCode()!=206) throw new java.io.IOException("Expected partial response");
                byte[] prefix=new byte[256];
                try(DataInputStream in=new DataInputStream(c.getInputStream())) { in.readFully(prefix); }
                File ready=File.createTempFile("prefix",".tmp",getFilesDir());
                try(FileOutputStream out=new FileOutputStream(ready)) { out.write(prefix); }
                if(!ready.renameTo(new File(getFilesDir(),"prefix"))) throw new java.io.IOException("Cannot publish prefix");
            } catch(Exception ex) { android.util.Log.e("VideoFixture","External player failed",ex); }
            finally { if(c!=null) c.disconnect(); }
        }).start();
    }
}
