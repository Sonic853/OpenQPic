package com.alensw.ui.view;

import android.view.View;
import android.view.animation.Interpolator;

class PicturePanAnimation extends ViewFrameAnimator {
   final float a;
   final float b;
   final Runnable c;
   final ZoomablePictureView d;
   private float e;
   private float f;

   PicturePanAnimation(ZoomablePictureView var1, View var2, Interpolator var3, float var4, float var5, Runnable var6) {
      super(var2, var3);
      this.d = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.e = 0.0F;
      this.f = 0.0F;
   }

   @Override
   public void a() {
      this.d.F = true;
   }

   @Override
   public void a(float var1) {
      float var2 = this.a * var1;
      var1 = this.b * var1;
      this.d.b(var2 - this.e, var1 - this.f);
      this.e = var2;
      this.f = var1;
   }

   @Override
   public void b() {
      this.d.J = null;
      this.d.F = false;
      this.d.j();
      this.d.invalidate();
   }

   @Override
   public void c() {
      super.c();
      if (this.c != null) {
         this.c.run();
      }
   }
}
