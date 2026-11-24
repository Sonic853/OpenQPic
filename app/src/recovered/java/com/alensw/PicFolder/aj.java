package com.alensw.PicFolder;

import android.content.Intent;
import com.alensw.bean.CommonRoot;
class aj implements Runnable {
   final int a;
   final CommonRoot b;
   final com.alensw.PicFolder.ai c;

   aj(com.alensw.PicFolder.ai var1, int var2, CommonRoot var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      GalleryActivity.e(this.c.a);
      if (GalleryActivity.a(this.c.a).c != this.a) {
         QuickApp.v.c();
         QuickApp.w.c();
      }

      if ("folders".equals(this.b.n)) {
         this.c.a.a(com.alensw.ui.c.bz.class, new Intent(), null);
      } else {
         this.c.a.a(com.alensw.ui.c.z.class, com.alensw.ui.c.z.a(this.b.k(), true, this.b.n), null);
      }

      GalleryActivity.a(this.c.a).a(this.a);
   }
}
