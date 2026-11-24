package com.alensw.a;

import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

class bb implements MediaScannerConnectionClient {
   final com.alensw.a.ba a;

   bb(com.alensw.a.ba var1) {
      this.a = var1;
   }

   @Override
   public void onMediaScannerConnected() {
   }

   @Override
   public void onScanCompleted(String var1, Uri var2) {
      try {
         int var3 = Integer.parseInt(var2.getLastPathSegment());
         com.alensw.a.ba.a(this.a).a(var1, var3);
      } catch (Throwable var4) {
      }
   }
}
