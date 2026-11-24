package org.openqpic.shareprobe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

/** Test APK with no storage permission. Read the actual EXTRA_STREAM grants in the receiving UID. */
public final class ShareProbe extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Intent in=getIntent();
        try(PrintStream out=new PrintStream(openFileOutput("result",MODE_PRIVATE))) {
            out.println(in.getAction());
            List<Uri> streams=Intent.ACTION_SEND_MULTIPLE.equals(in.getAction())
                    ?in.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
                    :Collections.singletonList((Uri)in.getParcelableExtra(Intent.EXTRA_STREAM));
            for(Uri uri:streams) {
                if(!"content".equals(uri.getScheme())) throw new IllegalArgumentException("Not a content URI: "+uri);
                MessageDigest hash=MessageDigest.getInstance("SHA-256");
                try(InputStream stream=getContentResolver().openInputStream(uri)) {
                    byte[] buf=new byte[8192]; int n;
                    while((n=stream.read(buf))!=-1) hash.update(buf,0,n);
                }
                StringBuilder hex=new StringBuilder();
                for(byte b:hash.digest()) hex.append(String.format("%02x",b&255));
                out.println(hex);
            }
        } catch(Exception ex) { android.util.Log.e("ShareProbe","Could not read shared media",ex); }
        finish();
    }
}
