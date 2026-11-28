package com.alensw.ui.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;

public class PlaybackIconShape extends OutlinedIconShape {
   public static final float[][] g;
   public static final float[][] h;
   public static final float[][] i;
   public static final float[][] j = new float[][]{{28.8F, 67.2F}, {40.0F, 40.0F}, {67.2F, 28.8F}, {56.0F, 56.0F}};
   public boolean l = true;
   private int m;

   static {
      float[] var0 = new float[]{67.11F, 48.24F};
      g = new float[][]{{36.23F, 30.41F}, var0, {36.23F, 66.07F}};
      var0 = new float[]{32.53F, 32.53F};
      float[] var2 = new float[]{43.91F, 32.53F};
      float[] var1 = new float[]{32.53F, 63.47F};
      h = new float[][]{var0, var2, {43.91F, 63.47F}, var1};
      var0 = new float[]{51.0F, 47.0F};
      float[] var3 = new float[]{74.0F, 48.0F};
      var2 = new float[]{51.0F, 49.0F};
      var1 = new float[]{28.0F, 62.0F};
      i = new float[][]{{28.0F, 34.0F}, var0, {51.0F, 34.0F}, var3, {51.0F, 62.0F}, var2, var1};
   }

   public PlaybackIconShape(int var1) {
      this.m = var1;
   }

   @Override
   protected Object a(float var1, float var2) {
      float var3 = Math.min(var1, var2) / 96.0F;
      var1 /= 2.0F;
      float var4 = var2 / 2.0F;
      var2 = 40.0F * var3;
      Path var7 = this.k;
      if (this.l) {
         var7.addCircle(var1, var4, var2, Direction.CW);
         var7.addCircle(var1, var4, var2 - 4.0F * var3, Direction.CCW);
      }

      if (this.m == 0) {
         a(var7, g, var3, true);
      } else if (this.m == 1) {
         var1 = 52.09F - h[0][0];
         a(var7, h, var3, true);

         for (float[] var9 : h) {
            var9[0] += var1;
         }

         a(var7, h, var3, true);

         for (float[] var22 : h) {
            var22[0] -= var1;
         }
      } else if (this.m == 2) {
         a(var7, i, var3, true);
      } else if (this.m == 3) {
         for (float[] var25 : i) {
            var25[0] = 96.0F - var25[0];
         }

         a(var7, i, var3, true);

         for (float[] var24 : i) {
            var24[0] = 96.0F - var24[0];
         }
      } else if (this.m == 4) {
         a(var7, j, var3, true);
         var7.addCircle(var1, var4, 4.0F * var3, Direction.CCW);
      }

      if (this.l) {
         var1 = 0.0F;
      } else {
         var1 = 6.0F;
      }

      this.a = var1;
      return this.k;
   }

   public void a(int var1) {
      if (this.m != var1) {
         this.m = var1;
         this.c = true;
      }
   }

   @Override
   protected void a(Canvas var1, Paint var2, float var3, float var4) {
      float var5 = Math.min(var3, var4) / 96.0F;
      var3 /= 2.0F;
      var4 /= 2.0F;
      if (this.l) {
         int var6 = var2.getColor();
         var2.setColor(1610612736);
         var1.drawCircle(var3, var4, var5 * 40.0F, var2);
         var2.setColor(var6);
      }
   }

   @Override
   protected void onResize(float var1, float var2) {
      if (!this.l) {
         super.onResize(var1, var2);
      }
   }
}
