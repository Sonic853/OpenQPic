package com.alensw.ui.view;

import android.view.View;
import android.view.animation.Interpolator;

class OverlayFadeAnimation extends ViewFrameAnimator {
   final FadingOverlay a;

   OverlayFadeAnimation(FadingOverlay var1, View var2, Interpolator var3) {
      super(var2, var3);
      this.a = var1;
   }

   @Override
   public void a(float var1) {
      int var3 = (int)(this.a.c * var1);
      int var2 = var3;
      if (!FadingOverlay.a(this.a)) {
         var2 = this.a.c - var3;
      }

      this.a.b(var2);
   }

   @Override
   public void b() {
      FadingOverlay.b(this.a).run();
   }
}
