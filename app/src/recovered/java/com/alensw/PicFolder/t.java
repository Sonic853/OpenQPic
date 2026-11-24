package com.alensw.PicFolder;

import android.graphics.Bitmap.Config;
class t implements Runnable {
   final com.alensw.PicFolder.q a;

   t(com.alensw.PicFolder.q var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      com.alensw.b.h.j var1 = this.a.a(Config.RGB_565);
      if (var1 != null) {
         this.a.h = false;
         this.a.a(var1);
         var1.m();
      }
   }
}
