package com.alensw.gallery;

import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

class ScannedMediaIdListener implements MediaScannerConnectionClient {
   final GalleryMediaStore a;

   ScannedMediaIdListener(GalleryMediaStore var1) {
      this.a = var1;
   }

   @Override
   public void onMediaScannerConnected() {
   }

   @Override
   public void onScanCompleted(String var1, Uri var2) {
      try {
         int var3 = Integer.parseInt(var2.getLastPathSegment());
         GalleryMediaStore.a(this.a).a(var1, var3);
      } catch (Throwable var4) {
      }
   }
}
