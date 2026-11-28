package com.alensw.ui.view;

class FinishOverlayFade implements Runnable {
   final FadingOverlay a;

   FinishOverlayFade(FadingOverlay var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      FadingOverlay var2 = this.a;
      byte var1;
      if (FadingOverlay.a(this.a)) {
         var1 = 3;
      } else {
         var1 = 0;
      }

      var2.a(var1);
   }
}
