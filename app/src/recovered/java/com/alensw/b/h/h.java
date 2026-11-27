package com.alensw.b.h;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.shapes.Shape;
import java.io.ByteArrayOutputStream;

public class h extends com.alensw.b.h.l {
   private Bitmap a;
   public final com.alensw.b.h.i b = new com.alensw.b.h.i();

   public h(int var1, int var2, int var3, int var4) {
      this.b.a = var1;
      this.b.b = var2;
      this.b.c = var3;
      this.b.d = (var4 + 360) % 360;
   }

   public h(Bitmap var1) {
      this(var1, 0, 0);
   }

   public h(Bitmap var1, int var2, int var3) {
      this(var1.getWidth(), var1.getHeight(), var2, var3);
      this.a = var1;
   }

   public static com.alensw.b.h.h a(int var0, int var1, Config var2) {
      Bitmap var3 = com.alensw.b.h.b.a(var0, var1, var2);
      com.alensw.b.h.h var4;
      if (var3 != null) {
         var4 = new com.alensw.b.h.h(var3);
      } else {
         var4 = null;
      }

      return var4;
   }

   public static com.alensw.b.h.h a(byte[] var0, Options var1) {
        try {
            Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(var0, 0, var0.length, var1);
            if (bitmapDecodeByteArray != null) {
                return new com.alensw.b.h.h(bitmapDecodeByteArray);
            }
        } catch (Throwable th) {
        }
        return null;
    }

   public static void a(Matrix var0, float var1, float var2, float var3, float var4, int var5) {
      float var9 = var3 / var1;
      float var8 = var4 / var2;
      float var6;
      float var7;
      if (var5 == 1) {
         var6 = Math.min(var9, var8);
         var7 = var6;
      } else {
         var6 = var8;
         var7 = var9;
         if (var5 == 2) {
            var6 = Math.max(var9, var8);
            var7 = var6;
         }
      }

      var0.postScale(var7, var6);
      var0.postTranslate((var3 - var7 * var1) / 2.0F, (var4 - var6 * var2) / 2.0F);
   }

   public com.alensw.b.h.h a(int var1, int var2, boolean var3, Config var4) {
      com.alensw.b.h.h var12;
      if (!this.j()) {
         var12 = null;
      } else {
         Matrix var10 = new Matrix();
         if (var3) {
            this.a(var10, var1, var2, 2);
         } else {
            int var7 = this.g();
            int var8 = this.h();
            float var6 = Math.min((float)var1 / var7, (float)var2 / var8);
            float var5 = var7 * var6;
            var6 = var8 * var6;
            this.a(var10, var5, var6, 1);
            var1 = Math.round(var5);
            var2 = Math.round(var6);
         }

         Bitmap var11 = this.a;
         boolean alpha = var11.hasAlpha();
         if (var4 == null) {
            var4 = var11.getConfig();
         }

         Config var9 = alpha && (var4 == null || var4 == Config.RGB_565) ? Config.ARGB_8888 : var4;
         if (var9 == null) var9 = Config.RGB_565;

         Bitmap var13 = com.alensw.b.h.b.a(var1, var2, var9);
         if (var13 == null) {
            var12 = null;
         } else {
            new Canvas(var13).drawBitmap(var11, var10, new Paint(6));
            var13.setHasAlpha(alpha);
            var12 = new com.alensw.b.h.h(var13);
         }
      }

      return var12;
   }

   @Override
   protected void a() {
      Bitmap var1 = this.a;
      this.a = null;
      if (var1 != null) {
         this.a(var1);
      }
   }

   public void a(int var1, int var2, float var3, Shape var4, Paint var5) {
      if (this.j()) {
         Bitmap var12 = this.a;
         int var9 = var12.getWidth();
         int var8 = var12.getHeight();
         Bitmap var10;
         if (!var12.isMutable()) {
            var10 = com.alensw.b.h.b.a(var9, var8, this.a.getConfig());
            if (var10 == null) {
               return;
            }
         } else {
            var10 = null;
         }

         Bitmap var11;
         if (var10 != null) {
            var11 = var10;
         } else {
            var11 = var12;
         }

         Canvas var14 = new Canvas(var11);
         if (var10 != null) {
            var14.drawBitmap(var12, 0.0F, 0.0F, null);
         }

         float var6 = Math.max(var1 * var3, var2 * var3);
         float var7 = (var9 - var6) / 2.0F;
         var3 = (var8 - var6) / 2.0F;
         var14.translate(var7, var3);
         var4.resize(var6, var6);
         var4.draw(var14, var5);
         var14.translate(-var7, -var3);
         if (var10 != null) {
            var12.recycle();
            this.a = var10;
            this.b.a = var9;
            this.b.b = var8;
         }
      }
   }

   protected void a(Bitmap var1) {
      var1.recycle();
   }

   public void a(Canvas var1, Rect var2, RectF var3, Paint var4) {
      if (this.j()) {
         var1.drawBitmap(this.a, var2, var3, var4);
      }
   }

   public void a(Matrix var1) {
      var1.reset();
      if (this.b.d != 0) {
         int var2;
         if (this.b.e) {
            var2 = this.b.a / 2;
         } else {
            var2 = this.b.a;
         }

         int var3 = this.b.b;
         var1.preTranslate(-var2 / 2, -var3 / 2);
         var1.postRotate(this.b.d);
         var1.postTranslate(this.g() / 2, this.h() / 2);
      }
   }

   public void a(Matrix var1, float var2, float var3, int var4) {
      int var6 = this.g();
      int var5 = this.h();
      this.a(var1);
      if (var6 >= 0 && var5 >= 0) {
         a(var1, var6, var5, var2, var3, var4);
      }
   }

   public void a(Matrix var1, RectF var2) {
      var2.set(0.0F, 0.0F, this.b.a, this.b.b);
      var1.mapRect(var2);
      if (this.b.e) {
         switch (this.b.d) {
            case 0:
               var2.right = var2.left + var2.width() / 2.0F;
               break;
            case 90:
               var2.bottom = var2.top + var2.height() / 2.0F;
               break;
            case 180:
               var2.left = var2.right - var2.width() / 2.0F;
               break;
            case 270:
               var2.top = var2.bottom - var2.height() / 2.0F;
         }
      }
   }

   public byte[] a(CompressFormat var1, int var2) {
      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream(8192);
         if (this.a.compress(var1, var2, var3)) {
            byte[] var5 = var3.toByteArray();
            var3.close();
            return var5;
         }
      } catch (Throwable var4) {
      }

      return null;
   }

   public int c(int var1) {
      com.alensw.b.h.i var2 = this.b;
      var2.d += var1;
      if (this.b.d < 0) {
         var2 = this.b;
         var2.d += 360;
      }

      var2 = this.b;
      var2.d %= 360;
      return this.b.d;
   }

   public final Bitmap d() {
      return this.a;
   }

   public final int e() {
      return this.b.a;
   }

   public final int f() {
      return this.b.b;
   }

   public final int g() {
      int var1;
      if (this.b.d % 180 == 0) {
         if (this.b.e) {
            var1 = this.b.a / 2;
         } else {
            var1 = this.b.a;
         }
      } else {
         var1 = this.b.b;
      }

      return var1;
   }

   public final int h() {
      int var1;
      if (this.b.d % 180 == 0) {
         var1 = this.b.b;
      } else if (this.b.e) {
         var1 = this.b.a / 2;
      } else {
         var1 = this.b.a;
      }

      return var1;
   }

   public final boolean i() {
      return this.a != null && this.a.hasAlpha();
   }

   public final boolean j() {
      boolean var1;
      if (this.a != null && !this.a.isRecycled()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean k() {
      boolean var1;
      if (this.b.a > 0 && this.b.b > 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   @Override
   public String toString() {
      StringBuilder var2 = new StringBuilder().append("(").append(this.g()).append("x").append(this.h()).append("x").append(this.b.d).append('°');
      String var1;
      if (this.b.e) {
         var1 = "/2)";
      } else {
         var1 = ")";
      }

      return var2.append(var1).toString();
   }
}
