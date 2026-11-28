package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import com.alensw.ui.activity.GalleryChromeActivity;
public class RadarView extends View {
   private float a;
   private float b;
   private float c;
   private float d;
   private Shader e;
   private final int[] f = new int[8];
   private final Matrix g = new Matrix();
   private final Paint h = new Paint(1);

   public RadarView(Context var1) {
      this(var1, null);
   }

   public RadarView(Context var1, AttributeSet var2) {
      super(var1, var2);
      int var4 = GalleryChromeActivity.d(var1);

      for (int var3 = 0; var3 < this.f.length - 1; var3++) {
         this.f[var3] = var4;
      }

      this.f[this.f.length - 1] = -1;
   }

   @Override
   protected void onDraw(Canvas var1) {
      this.g.setRotate(this.d, this.a, this.b);
      this.e.setLocalMatrix(this.g);
      var1.drawCircle(this.a, this.b, this.c, this.h);
      this.d += 3.0F;
      if (this.d >= 360.0F) {
         this.d = 0.0F;
      }

      if (VERSION.SDK_INT >= 16) {
         this.postInvalidateOnAnimation();
      } else {
         this.postInvalidateDelayed(16L);
      }
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.a = var1 / 2.0F;
      this.b = var2 / 2.0F;
      this.c = Math.min(var1, var2) / 2.0F;
      this.e = new SweepGradient(this.a, this.b, this.f, null);
      this.h.setShader(this.e);
      this.invalidate();
   }
}
