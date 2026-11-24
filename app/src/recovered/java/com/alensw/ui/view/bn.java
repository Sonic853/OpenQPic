package com.alensw.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.PaintDrawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
public class bn extends com.alensw.ui.view.v {
   private int A;
   private int B;
   private int C;
   private boolean D;
   private String E;
   private final Matrix k = new Matrix();
   private final Paint l = new Paint(5);
   private final Paint m = new Paint(5);
   private final Rect n = new Rect();
   private final Rect o = new Rect();
   private final Bitmap p;
   private final com.alensw.ui.view.bp q;
   private final PaintDrawable r;
   private final int s;
   private final int t;
   private final int u;
   private final int v;
   private final int w;
   private final int x;
   private int y;
   private int z;

   public bn(Context var1, com.alensw.ui.view.bp var2) {
      super(var1, var2);
      this.A = 0;
      this.E = "";
      this.q = var2;
      Resources var8 = var1.getResources();
      float var5 = var8.getDisplayMetrics().density;
      this.s = (int)(var5 * 3.0F);
      this.t = (int)(24.0F * var5);
      this.l.setColor(-7829368);
      this.u = (int)(28.0F * var5);
      this.v = (int)(56.0F * var5);
      this.p = com.alensw.b.h.b.a(this.u, this.v, Config.ARGB_8888);
      if (this.p != null) {
         Paint var11 = new Paint(5);
         var11.setColor(-1);
         var11.setShadowLayer(2.0F * var5, var5, var5, -1610612736);
         Canvas var10 = new Canvas(this.p);
         RectF var9 = new RectF();
         var9.set(this.u - 18.0F * var5, var5 * 6.0F, this.u - var5 * 4.0F, this.v - 6.0F * var5);
         var10.drawRoundRect(var9, var5 * 3.0F, var5 * 3.0F, var11);
         var11.clearShadowLayer();
         float var3 = var9.width() / 4.0F;
         float var7 = var9.centerX();
         float var4 = var9.centerY();
         float var6 = var4 / 3.0F;
         Path var12 = new Path();
         var12.moveTo(var7 - var3, var4 - var6);
         var12.lineTo(var7 + var3, var4 - var6);
         var12.lineTo(var7, var4 - var6 - var3);
         var12.moveTo(var7 - var3, var4 + var6);
         var12.lineTo(var7 + var3, var4 + var6);
         var12.lineTo(var7, var4 + var6 + var3);
         var11.setColor(-12303292);
         var10.drawPath(var12, var11);
      }

      this.m.setColor(-1);
      this.m.setTextSize(TypedValue.applyDimension(2, 16.0F, var8.getDisplayMetrics()));
      this.r = new com.alensw.ui.view.bo(this);
      this.r.setCornerRadius(var5 * 3.0F);
      this.x = (int)(8.0F * var5);
      this.w = (int)(180.0F * var5);
      this.C = Math.round(this.m.getTextSize() + this.x * 2);
   }

   static int a(com.alensw.ui.view.bn var0) {
      return var0.A;
   }

   @Override
   public void a(int var1) {
      if (var1 == 0) {
         this.D = false;
      }

      super.a(var1);
   }

   public void a(Canvas var1) {
      int var3 = 0;
      int var5 = this.q.getScrollX();
      int var4 = this.q.getScrollY();
      var1.translate(var5, var4);
      if (this.f != 0) {
         int var2;
         label27: {
            if (this.b == 1) {
               if (this.A == 1) {
                  var2 = (this.c - this.d) * this.j.height() / this.c + 0;
                  break label27;
               }

               if (this.A == 2) {
                  var3 = (this.c - this.d) * this.j.width() / this.c + 0;
                  var2 = 0;
                  break label27;
               }
            }

            var2 = 0;
         }

         if (this.D && this.E.length() > 0) {
            this.o.offset(var3, var2);
            this.r.setBounds(this.o);
            this.r.getPaint().setAlpha(this.d);
            this.r.draw(var1);
            this.m.setAlpha(this.d);
            com.alensw.compat.LegacyPlatform.save(var1, 2);
            var1.clipRect(this.o.left, this.o.top, this.o.right - this.x, this.o.bottom);
            var1.drawText(this.E, this.o.left + this.x, this.o.bottom - this.x - this.m.descent() / 2.0F, this.m);
            var1.restore();
         }

         if (this.p != null) {
            var3 += this.j.left;
            var2 += this.j.top;
            this.k.postTranslate(var3, var2);
            com.alensw.compat.LegacyPlatform.save(var1, 1);
            var1.concat(this.k);
            this.l.setAlpha(this.d);
            var1.drawBitmap(this.p, 0.0F, 0.0F, this.l);
            var1.restore();
            this.k.postTranslate(-var3, -var2);
         }
      } else {
         this.l.setAlpha(this.d);
         var1.drawRect(this.j.left, this.j.top, this.j.right, this.j.bottom, this.l);
      }

      var1.translate(-var5, -var4);
   }

   @Override
   protected boolean a(int var1, int var2) {
      boolean var3 = super.a(var1, var2);
      this.D = var3;
      if (this.D) {
         this.d();
      }

      return var3;
   }

   @Override
   protected void b() {
      this.n.set(this.j);
      this.n.union(this.o);
      int var1 = this.a.getScrollX();
      int var2 = this.a.getScrollY();
      this.n.offset(var1, var2);
      if (VERSION.SDK_INT >= 16) {
         this.a.postInvalidateOnAnimation(this.n.left, this.n.top, this.n.right, this.n.bottom);
      } else {
         this.a.invalidate(this.n);
      }
   }

   public void c(boolean z) {
        int clientHeight;
        int scrollRangeY;
        int iMax;
        int iMax2 = 0;
        int scrollMode = this.q.getScrollMode();
        if (this.A != scrollMode) {
            this.A = scrollMode;
            this.k.reset();
            if (this.A == 1) {
                this.y = this.v;
                this.z = this.u;
                this.k.preTranslate((-this.u) / 2.0f, (-this.v) / 2.0f);
                this.k.postRotate(90.0f);
                this.k.postTranslate(this.y / 2.0f, this.z / 2.0f);
                this.k.postScale(-1.0f, 1.0f, this.y / 2.0f, this.z / 2.0f);
            } else {
                this.y = this.u;
                this.z = this.v;
            }
        }
        if (this.A == 1) {
            this.f = 1;
            clientHeight = this.q.getClientWidth();
            scrollRangeY = this.q.getScrollRangeX();
        } else if (this.A != 2) {
            this.f = 0;
            a(false);
            return;
        } else {
            this.f = 2;
            clientHeight = this.q.getClientHeight();
            scrollRangeY = this.q.getScrollRangeY();
        }
        if (clientHeight < 0 || scrollRangeY < clientHeight * 3) {
            this.f = 0;
            z = false;
        }
        this.c = z ? 255 : 128;
        if (this.b != 4) {
            if (this.A == 1) {
                int clientWidth = this.q.getClientWidth();
                float fMax = Math.max(0.0f, Math.min(1.0f, this.q.getScrollPosX()));
                if (z) {
                    iMax = this.y;
                } else {
                    iMax = clientWidth > 0 ? (int) Math.max(((float) clientWidth / (this.q.getScrollRangeX() + clientWidth)) * clientWidth, this.t) : 0;
                }
                this.j.left = ((int) ((clientWidth - iMax) * fMax)) + this.q.getPaddingLeft();
                this.j.right = iMax + this.j.left;
                this.j.bottom = (this.q.getHeight() - this.q.getPaddingBottom()) - (this.s / 2);
                this.j.top = this.j.bottom - (z ? this.z : this.s);
            } else if (this.A == 2) {
                int clientHeight2 = this.q.getClientHeight();
                float fMax2 = Math.max(0.0f, Math.min(1.0f, this.q.getScrollPosY()));
                if (z) {
                    iMax2 = this.z;
                } else if (clientHeight2 > 0) {
                    iMax2 = (int) Math.max(((float) clientHeight2 / (this.q.getScrollRangeY() + clientHeight2)) * clientHeight2, this.t);
                }
                this.j.top = ((int) ((clientHeight2 - iMax2) * fMax2)) + this.q.getPaddingTop();
                this.j.bottom = iMax2 + this.j.top;
                this.j.right = (this.q.getWidth() - this.q.getPaddingRight()) - (this.s / 2);
                this.j.left = this.j.right - (z ? this.y : this.s);
            }
            a(true);
            this.q.removeCallbacks(this.e);
            this.q.postDelayed(this.e, 1000L);
        }
    }

   protected void d() {
      if (this.A == 1) {
         int var1 = (this.j.left + this.j.right - this.B) / 2;
         int var2 = this.B;
         int var3 = this.j.top - this.j.height();
         int var4 = this.C;
         this.o.set(var1, var3 - var4, var2 + var1, var3);
      } else if (this.A == 2) {
         int var9 = this.j.left - this.j.width();
         int var7 = this.B;
         int var5 = (this.j.top + this.j.bottom - this.C) / 2;
         int var10 = this.C;
         this.o.set(var9 - var7, var5, var9, var10 + var5);
      }

      if (this.A == 1) {
         int var6 = this.q.getPaddingLeft();
         int var8 = this.q.getWidth() - this.q.getPaddingRight();
         if (this.o.left < var6) {
            this.o.offset(var6 - this.o.left, 0);
         } else if (this.o.right > var8) {
            this.o.offset(var8 - this.o.right, 0);
         }
      }
   }

   @Override
   protected void e(int var1, int var2) {
      float var3 = (float)var1 / var2;
      if (this.A == 1) {
         this.q.setScrollPosX(var3);
      } else if (this.A == 2) {
         this.q.setScrollPosY(var3);
      }

      String var5 = this.q.e();
      String var4 = var5;
      if (var5 != null) {
         var4 = var5;
         if (var5.length() == 1) {
            char var6 = var5.charAt(0);
            var4 = var5;
            if (var6 >= 'a') {
               var4 = var5;
               if (var6 <= 'z') {
                  var4 = Character.toString((char)(var6 - ' '));
               }
            }
         }
      }

      if (!this.E.equals(var4)) {
         if (var4 == null) {
            var4 = "";
         }

         this.E = var4;
         this.B = Math.min(Math.round(this.m.measureText(this.E) + this.x * 2), this.w);
      }

      if (this.D) {
         this.d();
      }
   }

   public void f(int var1, int var2) {
      this.m.setColor(var1);
      this.r.getPaint().setColor(var2);
   }
}
