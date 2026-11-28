package com.alensw.ui.view;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class BoundsTransition extends Animation {
   private final boolean a;
   private final boolean b;
   private final RectF c;
   private float d;
   private float e;
   private float f;
   private float g;
   private float h;
   private float i;
   private float j;
   private float k;
   private float l;
   private boolean m;
   private float exitOffsetY;

   public BoundsTransition(boolean var1, boolean var2, RectF var3, RectF var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      if (var4 != null) {
         this.a(var4, null);
      }
   }

   void setExitOffsetY(float offsetY) {
      this.exitOffsetY = offsetY;
   }

   public boolean a(RectF var1, RectF var2) {
      float var10 = var1.width();
      float var9 = var1.height();
      float var12 = this.c.width();
      float var11 = this.c.height();
      float var3 = Math.max(var12 / var10, var11 / var9);
      float var4;
      if (this.a) {
         var4 = 1.0F;
      } else {
         var4 = var3;
      }

      float var5;
      if (this.a) {
         var5 = var3;
      } else {
         var5 = 1.0F;
      }

      this.d = var5;
      this.e = var4 - this.d;
      RectF var15;
      if (this.a) {
         var15 = this.c;
      } else {
         var15 = var1;
      }

      RectF var16;
      if (this.a) {
         var16 = var1;
      } else {
         var16 = this.c;
      }

      float var6 = var15.centerX();
      float var7 = var15.centerY();
      var5 = var16.centerX();
      float var8 = var16.centerY();
      if (this.a) {
         var4 = var5;
      } else {
         var4 = var6;
      }

      this.f = var4;
      if (this.a) {
         var4 = var8;
      } else {
         var4 = var7;
      }

      this.g = var4;
      this.j = var5 - var6;
      this.k = var8 - var7;
      if (this.a) {
         var4 = -this.j;
      } else {
         var4 = 0.0F;
      }

      this.h = var4;
      if (this.a) {
         var4 = -this.k;
      } else {
         var4 = 0.0F;
      }

      this.i = var4;
      boolean var14 = false;
      boolean var13 = var14;
      if (var2 != null) {
         var4 = var12 / var3;
         var3 = var11 / var3;
         var2.set(var1);
         if (var10 > var4) {
            var2.inset((var10 - var4) / 2.0F * (1.0F - this.l), 0.0F);
            var13 = true;
         } else {
            var13 = var14;
            if (var9 > var3) {
               var2.inset(0.0F, (var9 - var3) / 2.0F * (1.0F - this.l));
               var13 = true;
            }
         }
      }

      this.m = true;
      return var13;
   }

   @Override
   protected void applyTransformation(float var1, Transformation var2) {
      float var3;
      if (this.a) {
         var3 = var1;
      } else {
         var3 = 1.0F - var1;
      }

      this.l = var3;
      if (!this.m) {
         if (this.a) {
            var1 = 0.0F;
         } else {
            var1 = 255.0F;
         }

         var2.setAlpha(var1);
         var2.setTransformationType(1);
      } else {
         Matrix var4 = var2.getMatrix();
         var3 = this.d + this.e * var1;
         var4.setScale(var3, var3, this.f, this.g);
         float dragCorrectionY = this.exitOffsetY * (1.0F - var1);
         var4.postTranslate(this.h + this.j * var1, this.i + this.k * var1 + dragCorrectionY);
         if (this.b) {
            if (!this.a) {
               var1 = 1.0F - var1;
            }

            var2.setAlpha(var1);
         } else {
            var2.setTransformationType(2);
         }
      }
   }

   @Override
   public boolean willChangeBounds() {
      return false;
   }
}
