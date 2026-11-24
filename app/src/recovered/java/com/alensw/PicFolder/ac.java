package com.alensw.PicFolder;

import android.content.Intent;
import android.net.Uri;
import java.io.File;

class ac implements Runnable {
   final boolean a;
   final Intent b;
   final boolean c;
   final File d;
   final GalleryActivity e;

   ac(GalleryActivity var1, boolean var2, Intent var3, boolean var4, File var5) {
      this.e = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   @Override
   public void run() {
      if (this.a) {
         this.e.a(com.alensw.ui.c.bz.class, this.b, null);
      }

      if (this.a || this.c) {
         this.e.a(com.alensw.ui.c.bf.class, GalleryActivity.a(null, Uri.fromFile(this.d), this.b), null);
      }

      if (!this.c) {
         this.e.a(com.alensw.ui.c.dp.class, GalleryActivity.a(null, Uri.fromFile(this.d), this.b), null);
      }
   }
}
