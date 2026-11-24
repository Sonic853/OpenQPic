package com.alensw.ui.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Interpolator;

class o extends com.alensw.ui.view.bt {
   final Camera a;
   final boolean b;
   final Matrix c;
   final float d;
   final float e;
   final Matrix f;
   final Runnable g;
   final com.alensw.ui.view.j h;

   o(com.alensw.ui.view.j var1, View var2, Interpolator var3, Camera var4, boolean var5, Matrix var6, float var7, float var8, Matrix var9, Runnable var10) {
      super(var2, var3);
      this.h = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
      this.f = var9;
      this.g = var10;
   }

   @Override
   public void a() {
      this.h.I = true;
   }

   @Override
   public void a(float var1) {
      var1 = 180.0F * var1;
      this.a.save();
      if (this.b) {
         this.a.rotateY(var1);
      } else {
         this.a.rotateX(var1);
      }

      this.a.getMatrix(this.c);
      this.a.restore();
      this.c.preScale(0.2F, 0.2F);
      this.c.postScale(5.0F, 5.0F);
      this.c.preTranslate(-this.d, -this.e);
      this.c.postTranslate(this.d, this.e);
      this.h.S.set(this.f);
      this.h.S.postConcat(this.c);
      this.h.j();
      this.h.invalidate();
   }

   @Override
   public void b() {
      this.h.I = false;
      this.h.invalidate();
      if (this.g != null) {
         this.g.run();
      }
   }
}
