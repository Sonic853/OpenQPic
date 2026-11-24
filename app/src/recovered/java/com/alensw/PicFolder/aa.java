package com.alensw.PicFolder;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
class aa {
   protected int a;
   protected final Paint b;
   final com.alensw.PicFolder.q c;
   private final Path d;
   private final RectF e;

   aa(com.alensw.PicFolder.q var1) {
      this.c = var1;
      this.a = 0;
      this.b = new Paint(1);
      this.d = new Path();
      this.e = new RectF();
   }

   public int a(float f, float f2) {
        float fS = com.alensw.PicFolder.q.s(this.c) * 22.0f;
        RectF rectF = this.c.v;
        boolean z = f >= rectF.left - fS && f < rectF.right + fS;
        boolean z2 = f2 >= rectF.top - fS && f2 < rectF.bottom + fS;
        int i = (!z2 || Math.abs(rectF.left - f) >= fS) ? 0 : 1;
        if (z2 && Math.abs(rectF.right - f) < fS) {
            i |= 2;
        }
        if (z && (Math.abs(rectF.top - f2) < fS || (rectF.top <= f2 && f2 < this.c.w.top + fS))) {
            i |= 16;
        }
        int i2 = (!z || (Math.abs(rectF.bottom - f2) >= fS && (rectF.bottom < f2 || f2 < ((float) (com.alensw.PicFolder.q.t(this.c) - this.c.w.bottom)) - fS))) ? i : i | 32;
        if (i2 == 0) {
            float fWidth = rectF.width() / 3.0f;
            float fHeight = rectF.height() / 3.0f;
            RectF rectF2 = this.e;
            for (int i3 = 0; i3 <= 2; i3++) {
                for (int i4 = 1; i4 <= 2; i4++) {
                    rectF2.left = (rectF.left + (i4 * fWidth)) - fS;
                    rectF2.right = rectF2.left + (fS * 2.0f);
                    rectF2.top = (rectF.top + (i3 * fHeight)) - fS;
                    rectF2.bottom = rectF2.top + (fS * 2.0f);
                    if (rectF2.contains(f, f2)) {
                        i2 = 256;
                        break;
                    }
                }
            }
        }
        this.a = i2;
        return i2;
    }

   public void a(int var1, float var2, float var3) {
      RectF var10 = this.c.v;
      float var4 = this.c.b;
      float var7 = com.alensw.PicFolder.q.o(this.c);
      float var5 = com.alensw.PicFolder.q.p(this.c);
      float var8 = var10.width();
      float var6 = var10.height();
      if (var4 != 0.0F) {
         int var9;
         if ((var1 & 15) == 1) {
            var10.left = var10.left + Math.max(var2, -var10.left);
            if ((var1 & 240) == 16) {
               var10.top = var10.bottom - var8 / var4;
            } else {
               var2 = var10.top;
               var10.bottom = var8 / var4 + var2;
            }

            var9 = 0;
         } else {
            var9 = var1;
            if ((var1 & 15) == 2) {
               var10.right = var10.right + Math.min(var2, var7 - var10.right);
               if ((var1 & 240) == 16) {
                  var10.top = var10.bottom - var8 / var4;
               } else {
                  var2 = var10.top;
                  var10.bottom = var8 / var4 + var2;
               }

               var9 = 0;
            }
         }

         var7 = Math.min(com.alensw.PicFolder.q.q(this.c).width(), var7);
         var8 = var10.width() / var4;
         if ((var9 & 240) == 16) {
            var10.top = var10.top + Math.max(var3, -var10.top);
            if ((var9 & 15) == 1) {
               var10.left = var10.right - var6 * var4;
            } else {
               var10.right = var10.left + var6 * var4;
            }

            if (var10.width() >= var7 && var10.height() > var8) {
               var10.top = var10.bottom - var8;
            }
         } else if ((var9 & 240) == 32) {
            var2 = var10.bottom;
            var10.bottom = Math.min(var3, var5 - var10.bottom) + var2;
            if ((var9 & 15) == 1) {
               var10.left = var10.right - var6 * var4;
            } else {
               var10.right = var10.left + var6 * var4;
            }

            if (var10.width() >= var7 && var10.height() > var8) {
               var10.bottom = var10.top + var8;
            }
         }
      } else {
         if ((var1 & 15) == 1) {
            var10.left = var10.left + Math.max(var2, -var10.left);
         } else if ((var1 & 15) == 2) {
            var6 = var10.right;
            var10.right = Math.min(var2, var7 - var10.right) + var6;
         }

         if ((var1 & 240) == 16) {
            var10.top = var10.top + Math.max(var3, -var10.top);
         } else if ((var1 & 240) == 32) {
            var10.bottom = var10.bottom + Math.min(var3, var5 - var10.bottom);
         }
      }

      var5 = var10.width();
      var6 = var10.height();
      var3 = 56.0F * com.alensw.PicFolder.q.r(this.c);
      if (var4 != 0.0F) {
         var2 = var3 / var4;
      } else {
         var2 = var3;
      }

      if (var5 < var3) {
         var10.inset((var5 - var3) / 2.0F, 0.0F);
      }

      if (var6 < var2) {
         var10.inset(0.0F, (var6 - var2) / 2.0F);
      }
   }

   public void a(Canvas var1) {
      RectF var8 = this.c.v;
      float var3 = var8.width();
      float var2 = var8.height();
      float var4 = com.alensw.PicFolder.q.i(this.c);
      float var5 = com.alensw.PicFolder.q.j(this.c);
      Paint var7 = this.b;
      var7.setARGB(128, 64, 64, 64);
      var1.drawRect(0.0F, 0.0F, var4, var8.top, var7);
      var1.drawRect(0.0F, var8.bottom, var4, var5, var7);
      var1.drawRect(0.0F, var8.top, var8.left, var8.bottom, var7);
      var1.drawRect(var8.right, var8.top, var4, var8.bottom, var7);
      var7.setColor(-1);
      float var6 = com.alensw.PicFolder.q.k(this.c) * 2.0F;
      var4 = -var6 / 2.0F;
      var5 = -var6 / 2.0F;
      var8.inset(var4, var5);
      com.alensw.b.l.b.a(var1, var8, var6, var7);
      var8.inset(-var4, -var5);
      var7.setStrokeWidth(var6);
      this.a(var1, var8, var7);
      var3 /= 3.0F;
      var2 /= 3.0F;
      var7.setStrokeWidth((int)com.alensw.PicFolder.q.l(this.c));
      var1.drawLine(var8.left + var3, var8.top, var8.left + var3, var8.bottom, var7);
      var1.drawLine(var8.right - var3, var8.top, var8.right - var3, var8.bottom, var7);
      var1.drawLine(var8.left, var8.top + var2, var8.right, var8.top + var2, var7);
      var1.drawLine(var8.left, var8.bottom - var2, var8.right, var8.bottom - var2, var7);
      this.a(var1, var8.left + var3, var8.top + var2, var7);
      this.a(var1, var8.right - var3, var8.top + var2, var7);
      this.a(var1, var8.left + var3, var8.bottom - var2, var7);
      this.a(var1, var8.right - var3, var8.bottom - var2, var7);
   }

   public void a(Canvas var1, float var2, float var3, Paint var4) {
      float var5 = com.alensw.PicFolder.q.n(this.c) * 6.0F;
      Path var6 = this.d;
      var6.moveTo(var2 - var5, var3);
      var6.lineTo(var2, var3 - var5);
      var6.lineTo(var2 + var5, var3);
      var6.lineTo(var2, var5 + var3);
      var6.close();
      var1.drawPath(var6, var4);
      var6.reset();
   }

   public void a(Canvas var1, RectF var2, Paint var3) {
      float var5 = var2.top;
      float var4 = var2.bottom;
      float var6 = com.alensw.PicFolder.q.m(this.c) * 12.0F;
      Path var7 = this.d;
      var7.moveTo(var2.left, var5);
      var7.lineTo(var2.left + var6, var5);
      var7.lineTo(var2.left, var5 + var6);
      var7.close();
      var7.moveTo(var2.right - var6, var5);
      var7.lineTo(var2.right, var5);
      var7.lineTo(var2.right, var5 + var6);
      var7.close();
      var7.moveTo(var2.left, var4);
      var7.lineTo(var2.left + var6, var4);
      var7.lineTo(var2.left, var4 - var6);
      var7.close();
      var7.moveTo(var2.right - var6, var4);
      var7.lineTo(var2.right, var4);
      var7.lineTo(var2.right, var4 - var6);
      var7.close();
      var1.drawPath(var7, var3);
      var7.reset();
   }

   public boolean a() {
      boolean var1;
      if (this.a != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean b() {
      boolean var1;
      if ((this.a & 15) == 0 && (this.a & 240) == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean c() {
      boolean var1;
      if (this.a == 256) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
