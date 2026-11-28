package com.alensw.ui.view;

import android.view.animation.Animation;
import android.view.animation.Transformation;

class GridEnterAnimation extends Animation {
   final float a;
   final float b;
   final ImageGridView c;

   GridEnterAnimation(ImageGridView var1, float var2, float var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      ImageGridView.a(this.c, this.a + this.b * var1);
      var2.setTransformationType(0);
   }

   @Override
   public boolean willChangeBounds() {
      return false;
   }
}
