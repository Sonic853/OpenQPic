package com.alensw.ui.activity;

class AutoHideActionBar implements Runnable {
   final GalleryChromeActivity a;

   AutoHideActionBar(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(false);
      this.a.I.removeCallbacks(this);
   }
}
