package com.alensw.ui.view;

import android.view.View;
import android.view.animation.Interpolator;

class PatternDotScaleAnimation extends ViewFrameAnimator {
   final PatternCellState a;
   final float b;
   final float c;
   final Runnable d;
   final LockPatternView e;

   PatternDotScaleAnimation(LockPatternView var1, View var2, Interpolator var3, PatternCellState var4, float var5, float var6, Runnable var7) {
      super(var2, var3);
      this.e = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
   }

   @Override
   public void a(float var1) {
      this.a.a = this.b + (this.c - this.b) * var1;
      this.e.invalidate();
   }

   @Override
   public void b() {
      if (this.d != null) {
         this.d.run();
      }
   }
}
