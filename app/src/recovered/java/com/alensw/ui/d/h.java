package com.alensw.ui.d;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;

public class h extends com.alensw.ui.d.a {
   private boolean g = true;
   private float h;
   private float i;
   private RectF j = new RectF();

   public h(boolean var1) {
      this.g = var1;
      this.h = (float)Math.cos(0.9075712F);
      this.i = (float)Math.sin(0.9075712F);
   }

   @Override
   protected Object a(float var1, float var2) {
      var2 = Math.min(var1, var2) / 96.0F;
      float var5 = 44.0F * var2;
      float var6 = 32.0F * var2;
      var1 = 16.0F * var2;
      float var3 = 5.0F * var2;
      var2 = 2.0F * var2;
      Path var10 = this.k;
      RectF var11 = this.j;
      var10.addCircle(var5, var5, var6 - var3, Direction.CCW);
      var11.set(var5 - var6, var5 - var6, 0.0F, 0.0F);
      var11.right = var11.left + 2.0F * var6;
      var11.bottom = var11.top + 2.0F * var6;
      float var4 = this.h * var6 + var5;
      var6 = var6 * this.i + var5;
      float var7 = 2.0F * var3 + var4;
      var3 = var3 * 2.0F + var6;
      var10.moveTo(var4, var6);
      var10.arcTo(var11, 52.0F, 346.0F);
      var10.lineTo(var3, var7);
      var10.lineTo(var7, var3);
      var10.close();
      if (this.g) {
         var10.moveTo(var5 - var1, var5 - var2);

         for (int var8 = 1; var8 < 12; var8++) {
            var4 = var8 % 6;
            byte var9;
            if (var4 < 3.0F) {
               var9 = -1;
            } else {
               var9 = 1;
            }

            var6 = var9;
            if (var4 != 0.0F && var4 != 5.0F) {
               var3 = var2;
            } else {
               var3 = var1;
            }

            if (var4 != 2.0F && var4 != 3.0F) {
               var4 = var2;
            } else {
               var4 = var1;
            }

            var4 = -var4;
            if (var8 < 6) {
               var9 = 1;
            } else {
               var9 = -1;
            }

            var10.lineTo(var6 * var3 * var9 + var5, var9 * var4 + var5);
         }

         var10.close();
      } else {
         var11.set(var5 - var1, var5 - var2, 0.0F, 0.0F);
         var11.right = var1 * 2.0F + var11.left;
         var11.bottom = var2 * 2.0F + var11.top;
         var10.addRect(var11, Direction.CW);
      }

      this.a = 6.0F;
      return this.k;
   }
}
