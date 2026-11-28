package com.alensw.ui.view;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

final class ScaleFadeTransition extends Animation {
   final float a;
   final float b;
   final float c;
   final float d;

   ScaleFadeTransition(float var1, float var2, float var3, float var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      Matrix var3 = var2.getMatrix();
      var1 = this.a + this.b * var1;
      var3.setScale(var1, var1, this.c, this.d);
      var2.setAlpha(var1);
   }

   @Override
   public boolean willChangeBounds() {
      return false;
   }
}
