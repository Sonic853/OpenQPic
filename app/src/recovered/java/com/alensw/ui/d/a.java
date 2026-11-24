package com.alensw.ui.d;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;

public class a extends com.alensw.ui.d.c {
   protected float a;
   protected float b;
   protected boolean c;
   protected Bitmap d;
   protected final Paint e = new Paint(1);
   protected final PorterDuffXfermode f = new PorterDuffXfermode(Mode.CLEAR);

   protected Object a(float var1, float var2) {
      return this.k;
   }

   protected void a(Canvas var1, Paint var2) {
      float var5 = this.getWidth();
      float var4 = this.getHeight();
      float var6 = Math.min(var5, var4) / 96.0F;
      this.e.set(var2);
      this.e.setAntiAlias(true);
      this.a(var1, this.e, var5, var4);
      Object var8 = this.a(var5, var4);
      float var3 = this.a * var6;
      var6 *= this.b;
      if (var6 > 0.0F) {
         this.e.setShadowLayer(var6, 0.0F, 0.0F, -1610612736);
      }

      if (var3 > 0.0F) {
         this.e.setColor(-1610612736);
         this.e.setStyle(Style.FILL_AND_STROKE);
         this.e.setStrokeCap(Cap.ROUND);
         this.e.setStrokeJoin(Join.ROUND);
         this.e.setStrokeWidth(var3);
         this.a(var1, var8, this.e);
         Xfermode var7 = this.e.getXfermode();
         this.e.setStyle(Style.FILL);
         this.e.setXfermode(this.f);
         this.a(var1, var8, this.e);
         this.e.setXfermode(var7);
      }

      this.e.setColor(var2.getColor());
      this.e.setStyle(var2.getStyle());
      this.a(var1, var8, this.e);
      this.b(var1, var2, var5, var4);
      this.k.rewind();
   }

   protected void a(Canvas var1, Paint var2, float var3, float var4) {
   }

   protected void a(Canvas var1, Object var2, Paint var3) {
      if (var2 instanceof Path) {
         var1.drawPath((Path)var2, var3);
      } else if (var2 instanceof String) {
         var1.drawText((String)var2, 0.0F, 0.0F, var3);
      }
   }

   protected void b(Canvas var1, Paint var2, float var3, float var4) {
   }

   @SuppressLint("WrongCall")
   @Override
   public void draw(Canvas var1, Paint var2) {
      if (this.d != null) {
         if (this.c) {
            this.d.eraseColor(0);
            this.a(new Canvas(this.d), var2);
            this.c = false;
         }

         var1.drawBitmap(this.d, 0.0F, 0.0F, null);
      } else {
         this.a(var1, var2);
      }
   }

   @Override
   protected void onResize(float var1, float var2) {
      int var4 = (int)var1;
      int var3 = (int)var2;
      if (this.d == null || this.d.getWidth() != var4 || this.d.getHeight() != var3) {
         if (this.d != null) {
            this.d.recycle();
         }

         try {
            this.d = Bitmap.createBitmap(var4, var3, Config.ARGB_8888);
         } catch (Throwable var6) {
            this.d = null;
         }
      }

      this.c = true;
   }
}
