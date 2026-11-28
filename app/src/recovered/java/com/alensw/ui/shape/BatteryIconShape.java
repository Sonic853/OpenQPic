package com.alensw.ui.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;

public class BatteryIconShape extends OutlinedIconShape {
   public static final float[][] g;
   protected boolean h;
   protected int i;
   protected RectF j = new RectF();

   static {
      float[] var0 = new float[]{88.0F, 70.0F};
      float[] var1 = new float[]{14.0F, 70.0F};
      float[] var2 = new float[]{8.0F, 58.0F};
      g = new float[][]{{8.0F, 38.0F}, {14.0F, 38.0F}, {14.0F, 26.0F}, {88.0F, 26.0F}, var0, var1, {14.0F, 58.0F}, var2};
   }

   @Override
   protected Object a(float var1, float var2) {
      var1 = Math.min(var1, var2) / 96.0F;
      var2 = 6.0F * var1;
      Path var6 = this.k;
      RectF var3 = this.j;
      a(var6, g, var1, true);
      float[] var4 = g[2];
      float[] var5 = g[4];
      var3.set(var4[0] * var1, var4[1] * var1, var5[0] * var1, var1 * var5[1]);
      var3.inset(var2, var2);
      var6.addRect(var3, Direction.CCW);
      var3.inset(var2 / 2.0F, var2 / 2.0F);
      return this.k;
   }

   public void a(int var1, boolean var2) {
      this.h = var2;
      this.i = var1;
      this.c = true;
   }

   @Override
   protected void b(Canvas var1, Paint var2, float var3, float var4) {
      RectF var6 = this.j;
      var6.left = var6.right - var6.width() * this.i / 100.0F;
      int var5 = var2.getColor();
      if (this.h) {
         var2.setColor(-1727987968);
      }

      var1.drawRect(var6, var2);
      var2.setColor(var5);
   }
}
