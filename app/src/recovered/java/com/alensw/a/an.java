package com.alensw.a;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import java.util.concurrent.ConcurrentLinkedQueue;

public class an implements MediaScannerConnectionClient {
   private final MediaScannerConnection a;
   private final MediaScannerConnectionClient b;
   private final ConcurrentLinkedQueue c;

   public an(Context var1, MediaScannerConnectionClient var2) {
      this.b = var2;
      this.a = new MediaScannerConnection(var1, this);
      this.c = new ConcurrentLinkedQueue();
   }

   private void a() {
      String path = (String)this.c.peek();
      synchronized (this.a) {
         if (path != null) this.a.scanFile(path, null);
         else this.a.disconnect();
      }
   }

   public void a(String var1) {
      if (var1 != null) {
         this.c.remove(var1);
      }
   }

   public void b(String param1) {
        if (param1 == null) {
            return;
        }
        if (!this.c.contains(param1)) {
            this.c.add(param1);
        }
        synchronized (this.a) {
            if (!this.a.isConnected()) {
                this.a.connect();
            }
        }
    }

   @Override
   public void onMediaScannerConnected() {
      if (this.b != null) {
         this.b.onMediaScannerConnected();
      }

      this.a();
   }

   @Override
   public void onScanCompleted(String var1, Uri var2) {
      this.c.remove(var1);
      this.a();
      if (this.b != null) {
         this.b.onScanCompleted(var1, var2);
      }
   }
}
