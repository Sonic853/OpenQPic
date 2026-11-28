package com.alensw.ui.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.FloatMath;
import android.view.animation.Animation;
import android.view.animation.Transformation;

final class CubeTransition extends Animation {
   final float a;
   final float b;
   final Camera c;
   final float d;
   final float e;
   final float f;
   final boolean g;

   CubeTransition(float var1, float var2, Camera var3, float var4, float var5, float var6, boolean var7) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
      this.g = var7;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      float var3 = 0.0F;
      float var4 = 0.0F;
      Matrix var8 = var2.getMatrix();
      float var6 = this.a + (this.b - this.a) * var1;
      float var7 = (float)(Math.PI * var6 / 180.0);
      float var5 = (float) Math.sin(var7);
      var7 = (float) Math.cos(var7);
      this.c.save();
      this.c.translate(var5 * this.d, 0.0F, (1.0F - var7) * this.d);
      this.c.rotateY(var6);
      this.c.getMatrix(var8);
      this.c.restore();
      var8.preTranslate(-this.e, -this.f);
      var8.postTranslate(this.e, this.f);
      if (this.g) {
         if (var1 <= 0.25F) {
            var1 = var4;
         } else {
            var1 = (var1 - 0.25F) / 0.75F;
         }

         var2.setAlpha(var1);
      } else {
         if (var1 >= 0.75F) {
            var1 = var3;
         } else {
            var1 = (0.75F - var1) / 0.75F;
         }

         var2.setAlpha(var1);
      }
   }

   @Override
   public boolean willChangeBounds() {
      return false;
   }
}
