package com.alensw.ui.activity;

import com.alensw.PicFolder.QuickApp;

class ClearThumbnails implements Runnable {
   final MainPreference a;

   ClearThumbnails(MainPreference var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      QuickApp.t.a(true);
      QuickApp.v.c();
      QuickApp.w.c();
      QuickApp.r.b();
      QuickApp.q.a(4, null, null);
      QuickApp.a(true);
   }
}
