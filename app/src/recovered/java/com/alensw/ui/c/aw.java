package com.alensw.ui.c;

import com.alensw.PicFolder.GalleryStorage;

class aw implements Runnable {
   final com.alensw.ui.c.as a;

   aw(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.H == 2) {
         this.a.C.getContentResolver().notifyChange(GalleryStorage.observerUri(com.alensw.ui.c.as.b(this.a).i()), null);
         this.a.b.postDelayed(this, 3600000L);
      }
   }
}
