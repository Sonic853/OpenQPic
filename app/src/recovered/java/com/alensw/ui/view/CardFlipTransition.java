package com.alensw.ui.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.FloatMath;
import android.view.animation.Animation;
import android.view.animation.Transformation;

final class CardFlipTransition extends Animation {
   final float a;
   final float b;
   final Camera c;
   final float d;
   final float e;
   final boolean f;

   CardFlipTransition(float var1, float var2, Camera var3, float var4, float var5, boolean var6) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      Matrix var4 = var2.getMatrix();
      float var3 = this.a + (this.b - this.a) * var1;
      Math.cos((float)(Math.PI * var3 / 180.0));
      this.c.save();
      this.c.rotateY(var3);
      this.c.getMatrix(var4);
      this.c.restore();
      var4.preScale(0.2F, 0.2F);
      var4.postScale(5.0F, 5.0F);
      var4.preTranslate(-this.d, -this.e);
      var4.postTranslate(this.d, this.e);
      if (this.f) {
         if (!(var1 >= 0.5F)) {
            var1 = 0.0F;
         }
      } else if (var1 <= 0.5F) {
         var1 = 1.0F - var1;
      } else {
         var1 = 0.0F;
      }

      var2.setAlpha(var1);
   }

   @Override
   public boolean willChangeBounds() {
      return false;
   }
}
