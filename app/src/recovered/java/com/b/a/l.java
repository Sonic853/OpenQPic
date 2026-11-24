package com.b.a;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public class l extends Drawable {
   public static final boolean c;
   private final int a;
   private final int b;
   private final Picture d;
   private Bitmap e;
   private Matrix f;
   private Paint g;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      c = var0;
   }

   public l(Picture var1, int var2, int var3) {
      this.d = var1;
      this.a = var2;
      this.b = var3;
   }

   public Picture a() {
      return this.d;
   }

   @Override
   public void draw(Canvas var1) {
      Rect var5 = this.getBounds();
      if (this.e != null) {
         float var2 = (var5.left + var5.right - this.e.getWidth()) / 2.0F;
         int var4 = var5.top;
         float var3 = (var5.bottom + var4 - this.e.getHeight()) / 2.0F;
         var1.drawBitmap(this.e, var2, var3, this.g);
      } else {
         com.alensw.compat.LegacyPlatform.save(var1, 3);
         var1.clipRect(var5);
         if (this.f != null) {
            var1.concat(this.f);
         }

         this.d.draw(var1);
         var1.restore();
      }
   }

   @Override
   public int getIntrinsicHeight() {
      return this.b;
   }

   @Override
   public int getIntrinsicWidth() {
      return this.a;
   }

   @Override
   public int getOpacity() {
      return -3;
   }

   @Override
   protected void onBoundsChange(Rect var1) {
      int var5 = var1.right - var1.left;
      int var6 = var1.bottom - var1.top;
      int var3 = this.d.getWidth();
      int var4 = this.d.getHeight();
      float var2 = Math.min((float)var5 / var3, (float)var6 / var4);
      if (!c && this.g == null) {
         if (this.f == null) {
            this.f = new Matrix();
         } else {
            this.f.reset();
         }

         this.f.postScale(var2, var2);
         this.f.postTranslate((var5 - var3 * var2) / 2.0F + var1.left, (var6 - var4 * var2) / 2.0F + var1.top);
      } else {
         var6 = Math.round(var3 * var2);
         var5 = Math.round(var4 * var2);
         if (this.e != null) {
            if (this.e.getWidth() == var6 && this.e.getHeight() == var5) {
               return;
            }

            this.e.recycle();
            this.e = null;
         }

         try {
            this.e = Bitmap.createBitmap(var6, var5, Config.ARGB_8888);
            if (this.e != null) {
               Canvas var8 = new Canvas(this.e);
               var8.scale(var2, var2);
               var8.translate((var6 - var3 * var2) / 2.0F, (var5 - var4 * var2) / 2.0F);
               this.d.draw(var8);
            }
         } catch (Throwable var7) {
            var7.printStackTrace();
         }
      }
   }

   @Override
   public void setAlpha(int var1) {
   }

   @Override
   public void setColorFilter(ColorFilter var1) {
      if (var1 != null) {
         if (this.g == null) {
            this.g = new Paint(6);
         }

         this.g.setColorFilter(var1);
      } else {
         this.g = null;
      }

      if (!c) {
         this.onBoundsChange(this.getBounds());
      }

      this.invalidateSelf();
   }

   @Override
   public void setDither(boolean var1) {
   }

   @Override
   public void setFilterBitmap(boolean var1) {
   }
}
