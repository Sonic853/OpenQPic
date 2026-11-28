package com.alensw.ui.view;

class HideFadingOverlay implements Runnable {
   final FadingOverlay a;

   HideFadingOverlay(FadingOverlay var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(false);
      this.a.a.removeCallbacks(this);
   }
}
