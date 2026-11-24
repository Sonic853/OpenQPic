package com.alensw.ui.view;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

final class i extends Animation {
   final boolean a;
   final int b;

   i(boolean var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      Matrix var3 = var2.getMatrix();
      if (this.a) {
         var1 = 1.0F - var1;
      } else {
         var1 = -var1;
      }

      var3.setTranslate(var1 * this.b, 0.0F);
      var2.setTransformationType(2);
   }

   @Override
   public boolean willChangeBounds() {
      return false;
   }
}
