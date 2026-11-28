package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;

class ScanMediaPath implements Runnable {
   final String a;
   final MediaActionPage b;

   ScanMediaPath(MediaActionPage var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      QuickApp.s.a(this.a);
   }
}
