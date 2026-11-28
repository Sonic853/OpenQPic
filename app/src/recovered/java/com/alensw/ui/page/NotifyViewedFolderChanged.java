package com.alensw.ui.page;

import com.alensw.PicFolder.GalleryStorage;

class NotifyViewedFolderChanged implements Runnable {
   final MediaViewerPage a;

   NotifyViewedFolderChanged(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.H == 2) {
         this.a.C.getContentResolver().notifyChange(GalleryStorage.observerUri(MediaViewerPage.b(this.a).i()), null);
         this.a.b.postDelayed(this, 3600000L);
      }
   }
}
