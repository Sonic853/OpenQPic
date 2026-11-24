package com.alensw.ui.view;

import android.view.View;
import android.view.animation.Interpolator;

class am extends com.alensw.ui.view.bt {
   final com.alensw.ui.view.ap a;
   final float b;
   final float c;
   final float d;
   final float e;
   final LockPatternView f;

   am(LockPatternView var1, View var2, Interpolator var3, com.alensw.ui.view.ap var4, float var5, float var6, float var7, float var8) {
      super(var2, var3);
      this.f = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
   }

   @Override
   public void a(float var1) {
      this.a.d = (1.0F - var1) * this.b + this.c * var1;
      this.a.e = (1.0F - var1) * this.d + this.e * var1;
      this.f.invalidate();
   }

   @Override
   public void b() {
      this.a.f = null;
   }
}
