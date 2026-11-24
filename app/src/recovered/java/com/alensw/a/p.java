package com.alensw.a;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import com.alensw.PicFolder.QuickApp;
public class p extends com.alensw.b.h.h {
   private static final RectF a = new RectF();
   private static final Matrix c = new Matrix();
   private static final Paint d = new Paint(1);
   private static final Bitmap o = com.alensw.b.h.b.a(1, 1, Config.RGB_565);
   private static final Canvas p = new Canvas();
   private static final Paint q = new Paint(3);
   private static final RectF r = new RectF();
   private int h;
   private final int i;
   private final int j;
   private final int k;
   private final float l;
   private final boolean m;
   private final com.alensw.b.h.h[] n = new com.alensw.b.h.h[5];

   public p(int var1, int var2, int var3, float var4, int var5, int var6, boolean var7) {
      super(var2, var3, 0, 0);
      this.k = var1;
      this.i = var6;
      this.l = var4;
      this.m = var7;
      this.j = Math.min(b(var1), var5);
      if (var1 == 2) {
         this.b.a = Math.min(var2, (this.j - 1) * var3 / 10 + var3);
      }
   }

   public static com.alensw.b.h.h a(Resources var0, int var1, int var2, int var3, int var4) {
      byte var7 = 0;
      int var5;
      if (var1 == 2) {
         var5 = QuickApp.r.d;
      } else {
         var5 = var2;
      }

      int var6;
      if (var1 == 2) {
         var6 = QuickApp.r.e;
      } else {
         var6 = var3;
      }

      com.alensw.b.h.h var11 = com.alensw.b.h.h.a(var5, var6, com.alensw.a.bc.a);
      Object var10;
      if (var11 == null) {
         var10 = null;
      } else {
         int var9 = Color.argb(255, (Color.red(var4) * 208 + 6144) / 256, (Color.green(var4) * 208 + 6144) / 256, (208 * Color.blue(var4) + 6144) / 256);
         var11.d().eraseColor(var9);
         int var8 = Math.min(var5 * 3 / 4, var6 * 3 / 4);
         com.b.a.l var16 = com.b.a.b.a(var0, com.alensw.PicFolder.R.raw.ic_menu_folder, -7829368, var8);
         var16.setBounds((var5 - var8) / 2, (var6 - var8) / 2, (var5 + var8) / 2, (var8 + var6) / 2);
         var16.draw(new Canvas(var11.d()));
         var10 = var11;
         if (var1 == 2) {
            com.alensw.b.h.h var12 = com.alensw.b.h.h.a(var5, var6, com.alensw.a.bc.a);
            var10 = var11;
            if (var12 != null) {
               var12.d().eraseColor(var9);
               var10 = new com.alensw.a.p(var1, var2, var3, var0.getDisplayMetrics().density, 4, var4, false);
               var2 = b(var1);

               for (int var14 = var7; var14 < var2; var14++) {
                  com.alensw.b.h.h var13;
                  if (var14 == 0) {
                     var13 = var11;
                  } else {
                     var13 = var12;
                  }

                  ((com.alensw.a.p)var10).a(var14, var13);
               }

               var11.m();
               var12.m();
            }
         }
      }

      return (com.alensw.b.h.h)var10;
   }

   private void a(Canvas var1, RectF var2, Paint var3) {
      if (this.k == 2) {
         for (int var4 = this.j - 1; var4 >= 0; var4--) {
            com.alensw.b.h.h var5 = this.n[var4];
            if (var5 != null && var5.j()) {
               this.c(var1, var2, var4, var5, var3);
            }
         }
      } else {
         for (int var6 = 0; var6 < this.j; var6++) {
            com.alensw.b.h.h var7 = this.n[var6];
            if (var7 != null && var7.j()) {
               if (this.k == 1) {
                  this.a(var1, var2, var6, var7, var3);
               } else if (this.k == 3) {
                  this.b(var1, var2, var6, var7, var3);
               }
            }
         }
      }
   }

   public static int b(int var0) {
      byte var1;
      switch (var0) {
         case 1:
            var1 = 4;
            break;
         case 2:
         case 3:
            var1 = 3;
            break;
         default:
            var1 = 1;
      }

      return var1;
   }

   private com.alensw.b.h.h o() {
      int var1 = this.e();
      int var2 = this.f();
      com.alensw.b.h.h var3 = com.alensw.b.h.h.a(var1, var2, com.alensw.a.bc.a);
      if (var3 != null) {
         p.setBitmap(var3.d());
         if (!var3.i()) {
            p.drawColor(this.i);
         }

         r.set(0.0F, 0.0F, var1, var2);
         this.a(p, r, q);
         p.setBitmap(o);
         this.p();
         this.n[4] = var3;
      }

      return var3;
   }

   private void p() {
      for (int var1 = 0; var1 < this.n.length; var1++) {
         if (this.n[var1] != null) {
            this.n[var1].m();
            this.n[var1] = null;
         }
      }
   }

   @Override
   protected void a() {
      super.a();
      this.p();
   }

   public void a(int var1, com.alensw.b.h.h var2) {
      if (this.n() > 0) {
         boolean var3 = this.b();
         if (var1 >= 0 && var1 < 4) {
            this.h |= 1 << var1;
            if (this.n[var1] != null) {
               this.n[var1].m();
            }

            com.alensw.b.h.h[] var4 = this.n;
            if (var2 != null) {
               var2 = (com.alensw.b.h.h)var2.l();
            } else {
               var2 = null;
            }

            var4[var1] = var2;
         }

         if (!var3 && this.b()) {
            this.o();
         }
      }
   }

   @Override
   public void a(Canvas var1, Rect var2, RectF var3, Paint var4) {
      com.alensw.b.h.h var11 = this.n[4];
      if (var11 != null && var11.j()) {
         int var10 = this.b.a;
         int var9 = this.b.b;
         float var7 = var3.width() - var10;
         float var5 = var3.height() - var9;
         if (Math.abs(var7) < 2.0F && Math.abs(var5) < 2.0F) {
            Bitmap var12 = var11.d();
            float var6 = var3.left;
            float var8 = var7 / 2.0F;
            var7 = var3.top;
            var1.drawBitmap(var12, var8 + var6, var5 / 2.0F + var7, var4);
         } else {
            var1.drawBitmap(var11.d(), var2, var3, var4);
         }
      } else {
         this.a(var1, var3, var4);
      }
   }

   protected void a(Canvas var1, RectF var2, int var3, com.alensw.b.h.h var4, Paint var5) {
      float var6 = var2.width();
      float var7 = var2.height();
      var6 /= 2.0F;
      var7 /= 2.0F;
      int var9 = var3 / 2;
      RectF var10 = a;
      float var8 = var2.left;
      var10.left = (var3 - var9 * 2) * var6 + var8;
      var10.right = var10.left + var6;
      var8 = var2.top;
      var10.top = var9 * var7 + var8;
      var10.bottom = var10.top + var7;
      c.reset();
      Matrix var11 = c;
      byte var12;
      if (this.m) {
         var12 = 2;
      } else {
         var12 = 1;
      }

      var4.a(var11, var6, var7, var12);
      c.postTranslate(var10.left, var10.top);
      var4.a(c, var10);
      com.alensw.compat.LegacyPlatform.save(var1, 2);
      var1.clipRect(var10);
      var1.drawBitmap(var4.d(), c, var5);
      var1.restore();
   }

   public boolean a(int var1) {
      boolean var2 = true;
      if ((this.h & 1 << var1) == 0) {
         var2 = false;
      }

      return var2;
   }

   protected void b(Canvas var1, RectF var2, int var3, com.alensw.b.h.h var4, Paint var5) {
      float var7 = Math.min(var2.width(), var2.height());
      RectF var9 = a;
      if (var3 == 0) {
         var9.left = var2.left;
         var9.right = var9.left + var7;
         var9.top = var2.top;
         var9.bottom = var2.bottom;
      } else {
         var9.left = var2.left + var7;
         var9.right = var9.left + var7 / 2.0F;
         float var8 = var2.top;
         float var6;
         if (var3 == 1) {
            var6 = 0.0F;
         } else {
            var6 = var7 / 2.0F;
         }

         var9.top = var6 + var8;
         var9.bottom = var9.top + var7 / 2.0F;
      }

      c.reset();
      Matrix var10 = c;
      float var12 = var9.width();
      var7 = var9.height();
      byte var11;
      if (this.m) {
         var11 = 2;
      } else {
         var11 = 1;
      }

      var4.a(var10, var12, var7, var11);
      c.postTranslate(var9.left, var9.top);
      var4.a(c, var9);
      com.alensw.compat.LegacyPlatform.save(var1, 2);
      var1.clipRect(var9);
      var1.drawBitmap(var4.d(), c, var5);
      var1.restore();
   }

   public boolean b() {
      boolean var1 = true;
      if (this.h != (1 << this.j) - 1) {
         var1 = false;
      }

      return var1;
   }

   protected void c(Canvas var1, RectF var2, int var3, com.alensw.b.h.h var4, Paint var5) {
      float var8 = var2.width();
      float var6 = Math.min(var8, var2.height());
      float var7 = var6 / 10.0F;
      float var9 = var7 / 4.0F;
      var8 = (var8 - var6 - (this.j - 1) * var7) / 2.0F;
      var9 = var6 - var9 * var3;
      RectF var10 = a;
      var10.right = var8 + var2.left + var6 + var3 * var7;
      var10.left = var10.right - var9;
      var10.bottom = var2.bottom;
      var10.top = var10.bottom - var9;
      c.reset();
      var4.a(c, var10.width(), var10.height(), 1);
      c.postTranslate(var10.left, var10.top);
      var4.a(c, var10);
      d.setColor(this.i);
      d.setShadowLayer(this.l / 3.0F, this.l / 3.0F, 0.0F, -16777216);
      var1.drawRect(var10, d);
      var1.drawBitmap(var4.d(), c, var5);
   }

   public boolean c() {
      boolean var1;
      if (this.n() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
