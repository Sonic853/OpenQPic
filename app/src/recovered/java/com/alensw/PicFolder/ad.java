package com.alensw.PicFolder;

import android.content.Intent;
class ad implements Runnable {
   final Intent a;
   final GalleryActivity b;

   ad(GalleryActivity var1, Intent var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.a(com.alensw.ui.c.z.class, this.a, null);
   }
}
