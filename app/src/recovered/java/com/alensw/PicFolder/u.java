package com.alensw.PicFolder;

import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
class u extends com.alensw.ui.view.bt {
   final float a;
   final float b;
   final float c;
   final float d;
   final float e;
   final com.alensw.PicFolder.q f;
   private float g;

   u(com.alensw.PicFolder.q var1, View var2, Interpolator var3, float var4, float var5, float var6, float var7, float var8) {
      super(var2, var3);
      this.f = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
      this.g = 0.0F;
   }

   @Override
   public void a() {
      com.alensw.PicFolder.q.a(this.f, true);
      this.g = this.a;
   }

   @Override
   public void a(float var1) {
      var1 = this.a + this.b * var1;
      this.f.b(var1, this.c, this.d);
      com.alensw.PicFolder.q.a(this.f, var1 / this.g, this.c, this.d);
      this.g = var1;
   }

   @Override
   public void b() {
      com.alensw.PicFolder.q.a(this.f, null);
      com.alensw.PicFolder.q.b(this.f, false);
      this.f.b(this.e, this.c, this.d);
      com.alensw.PicFolder.q.a(this.f, this.e / this.g, this.c, this.d);
      RectF var3 = this.f.v;
      float var1 = com.alensw.PicFolder.q.d(this.f);
      float var2 = com.alensw.PicFolder.q.e(this.f);
      if (var3.left < 0.0F) {
         var1 = -var3.left;
      } else if (var3.right > var1) {
         var1 -= var3.right;
      } else {
         var1 = 0.0F;
      }

      if (var3.top < 0.0F) {
         var2 = -var3.top;
      } else if (var3.bottom > var2) {
         var2 -= var3.bottom;
      } else {
         var2 = 0.0F;
      }

      if (var1 != 0.0F || var2 != 0.0F) {
         this.f.b(var1, var2);
         var3.offset(var1, var2);
      }

      this.f.c();
   }
}
