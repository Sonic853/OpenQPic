package com.alensw.ui.view;

import android.view.animation.Interpolator;

public class cc implements Interpolator {
   private float a = 6.0F;
   private float b = 1.0F;

   public cc() {
      this(6.0F);
   }

   public cc(float var1) {
      this.a = var1;
      this.b = 1.0F / this.getInterpolation(1.0F);
   }

   @Override
   public float getInterpolation(float var1) {
      var1 = this.a * var1;
      if (var1 < 1.0F) {
         var1 -= 1.0F - (float)Math.exp(-var1);
      } else {
         var1 = (1.0F - (float)Math.exp(1.0F - var1)) * (1.0F - 0.36787945F) + 0.36787945F;
      }

      return var1 * this.b;
   }
}
