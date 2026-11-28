package com.alensw.PicFolder;

import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import com.alensw.ui.view.ViewFrameAnimator;
class CropZoomAnimation extends ViewFrameAnimator {
   final float a;
   final float b;
   final float c;
   final float d;
   final float e;
   final CropImageView f;
   private float g;

   CropZoomAnimation(CropImageView var1, View var2, Interpolator var3, float var4, float var5, float var6, float var7, float var8) {
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
      CropImageView.a(this.f, true);
      this.g = this.a;
   }

   @Override
   public void a(float var1) {
      var1 = this.a + this.b * var1;
      this.f.b(var1, this.c, this.d);
      CropImageView.a(this.f, var1 / this.g, this.c, this.d);
      this.g = var1;
   }

   @Override
   public void b() {
      CropImageView.a(this.f, null);
      CropImageView.b(this.f, false);
      this.f.b(this.e, this.c, this.d);
      CropImageView.a(this.f, this.e / this.g, this.c, this.d);
      RectF var3 = this.f.v;
      float var1 = CropImageView.d(this.f);
      float var2 = CropImageView.e(this.f);
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
