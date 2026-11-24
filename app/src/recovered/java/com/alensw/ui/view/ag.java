package com.alensw.ui.view;

import android.graphics.RectF;
import android.view.animation.Transformation;

class ag extends com.alensw.ui.view.bu {
   final float a;
   final float b;
   final ImageGridView c;

   ag(ImageGridView var1, boolean var2, boolean var3, RectF var4, RectF var5, float var6, float var7) {
      super(var2, var3, var4, var5);
      this.c = var1;
      this.a = var6;
      this.b = var7;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      ImageGridView.a(this.c, this.a + this.b * var1);
      super.applyTransformation(var1, var2);
   }
}
