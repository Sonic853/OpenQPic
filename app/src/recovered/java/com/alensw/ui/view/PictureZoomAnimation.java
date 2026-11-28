package com.alensw.ui.view;

import android.view.View;
import android.view.animation.Interpolator;

class PictureZoomAnimation extends ViewFrameAnimator {
   final float a;
   final float b;
   final float c;
   final float d;
   final float e;
   final ZoomablePictureView f;

   PictureZoomAnimation(ZoomablePictureView var1, View var2, Interpolator var3, float var4, float var5, float var6, float var7, float var8) {
      super(var2, var3);
      this.f = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
   }

   @Override
   public void a() {
      this.f.I = true;
   }

   @Override
   public void a(float var1) {
      float var2 = this.a;
      float var3 = this.b;
      this.f.b(var2 + var3 * var1, this.c, this.d);
      this.f.c(0.0F);
   }

   @Override
   public void b() {
      this.f.J = null;
      this.f.I = false;
      this.f.b(this.e, this.c, this.d);
      this.f.c(0.0F);
   }
}
