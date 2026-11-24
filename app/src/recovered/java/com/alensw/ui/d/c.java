package com.alensw.ui.d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

public class c extends Shape {
   protected final Path k = new Path();

   public static void a(Path var0, float[][] var1, float var2, boolean var3) {
      var0.moveTo(var1[0][0] * var2, var1[0][1] * var2);

      for (int var4 = 1; var4 < var1.length; var4++) {
         var0.lineTo(var1[var4][0] * var2, var1[var4][1] * var2);
      }

      if (var3) {
         var0.close();
      }
   }

   @Override
   public void draw(Canvas var1, Paint var2) {
      var1.drawPath(this.k, var2);
   }
}
