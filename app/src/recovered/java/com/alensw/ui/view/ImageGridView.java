package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.Region.Op;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
@SuppressLint("Instantiatable")
public class ImageGridView extends com.alensw.ui.view.bp {
   public static final boolean a;
   private int A;
   private int B;
   private int C;
   private int D;
   private int E;
   private int F;
   private int G;
   private int H;
   private int I;
   private int J;
   private int K;
   private int L;
   private int M;
   private int N;
   private int O;
   private int P;
   private int Q;
   private int R;
   private float S;
   private float T;
   private float U;
   private final float V;
   private final float W;
   private float aA;
   private float aB;
   private final Runnable aC;
   private final float aa;
   private final float ab;
   private final Runnable ac;
   private final OnClickListener ad;
   private final OnLongClickListener ae;
   private Runnable af;
   private float ag;
   private RectF ah;
   private long ai;
   private long aj;
   private int ak;
   private int al;
   private float am;
   private final RectF an;
   private final Rect ao;
   private final RectF ap;
   private final RectF aq;
   private final Paint ar;
   private float as;
   private final Rect at;
   private final RectF au;
   private int av;
   private int aw;
   private int ax;
   private int ay;
   private float az;
   public boolean b;
   public boolean c;
   public boolean d;
   public int e;
   private com.alensw.ui.view.aj r;
   private Bitmap s;
   private String t;
   private StaticLayout u;
   private com.alensw.ui.view.ak[] v;
   private final RectF w;
   private final Paint x;
   private final TextPaint y;
   private int z;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      a = var0;
   }

   public ImageGridView(Context var1) {
      this(var1, null);
   }

   public ImageGridView(Context var1, AttributeSet var2) {
      super(var1, var2);
      boolean var7;
      if (VERSION.SDK_INT >= 16) {
         var7 = true;
      } else {
         var7 = false;
      }

      this.b = var7;
      this.e = 0;
      this.v = new com.alensw.ui.view.ak[8];
      this.w = new RectF();
      this.x = new Paint();
      this.y = new TextPaint(129);
      this.G = -1;
      this.H = -1;
      this.I = Integer.MAX_VALUE;
      this.J = Integer.MIN_VALUE;
      this.S = 100.0F;
      this.T = 100.0F;
      this.ac = new com.alensw.ui.view.ab(this);
      this.ad = new com.alensw.ui.view.ac(this);
      this.ae = new com.alensw.ui.view.ad(this);
      this.af = new com.alensw.ui.view.ae(this);
      this.an = new RectF();
      this.ao = new Rect();
      this.ap = new RectF();
      this.aq = new RectF();
      this.ar = new Paint(5);
      this.as = 0.0F;
      this.at = new Rect();
      this.au = new RectF();
      this.av = -1;
      this.aw = -1;
      this.ax = -1;
      this.ay = -1;
      this.az = -1.0F;
      this.aA = -1.0F;
      this.aB = -1.0F;
      this.aC = new com.alensw.ui.view.ai(this);
      this.setDrawingCacheEnabled(false);
      this.setWillNotCacheDrawing(true);
      Resources var11 = this.getResources();
      Theme var8 = var1.getTheme();
      TypedValue var9 = new TypedValue();
      var8.resolveAttribute(16842806, var9, true);
      this.B = var11.getColor(var9.resourceId);
      var8.resolveAttribute(com.alensw.PicFolder.R.attr.colorBackground, var9, true);
      this.A = var11.getColor(var9.resourceId);
      var8.resolveAttribute(com.alensw.PicFolder.R.attr.colorForeground, var9, true);
      this.z = var11.getColor(var9.resourceId);
      int var4 = var11.getColor(com.alensw.PicFolder.R.color.material_accent_light);
      this.o.f(-1, var4);
      this.V = var11.getDisplayMetrics().density;
      this.M = 2;
      int var5 = Math.round(this.V * this.M);
      this.O = var5;
      this.N = var5;
      this.P = (int)(this.V * 2.0F);
      float var3 = var11.getDimension(com.alensw.PicFolder.R.dimen.grid_text_size);
      this.W = var3;
      this.U = var3;
      this.aa = var11.getDimension(com.alensw.PicFolder.R.dimen.grid_subtext_size);
      this.ab = this.V * 10.0F;
      var5 = (int)(this.V * 28.0F);
      this.s = com.alensw.b.h.b.a(var5, var5, Config.ARGB_8888);
      if (this.s != null) {
         Canvas var10 = new Canvas(this.s);
         int var6 = (int)(this.V * 2.0F);
         ShapeDrawable var14 = new ShapeDrawable(new OvalShape());
         var14.getPaint().setColor(var4);
         var14.getPaint().setShadowLayer(this.V, this.V / 2.0F, this.V / 2.0F, -1610612736);
         var14.setBounds(var6, var6, var5 - var6, var5 - var6);
         var14.draw(var10);
         com.b.a.l var12 = com.b.a.b.a(var11, com.alensw.PicFolder.R.raw.ic_menu_done, -1, var5);
         var12.setBounds(0, 0, var5, var5);
         var12.draw(var10);
      }

      this.setOnClickListener(this.ad);
      this.setOnLongClickListener(this.ae);
   }

   static float a(ImageGridView var0, float var1) {
      var0.ag = var1;
      return var1;
   }

   public static int a(float var0) {
      return Math.max(Math.round(var0), 1);
   }

   static int a(ImageGridView var0) {
      return var0.H;
   }

   static int a(ImageGridView var0, int var1) {
      var0.I = var1;
      return var1;
   }

   static RectF a(ImageGridView var0, RectF var1) {
      var0.ah = var1;
      return var1;
   }

   static int b(ImageGridView var0, int var1) {
      var0.J = var1;
      return var1;
   }

   static Runnable b(ImageGridView var0) {
      return var0.ac;
   }

   static int c(ImageGridView var0, int var1) {
      var0.ax = var1;
      return var1;
   }

   static com.alensw.ui.view.aj c(ImageGridView var0) {
      return var0.r;
   }

   static int d(ImageGridView var0) {
      return var0.I;
   }

   static int d(ImageGridView var0, int var1) {
      var0.ay = var1;
      return var1;
   }

   static int e(ImageGridView var0) {
      return var0.J;
   }

   static RectF f(ImageGridView var0) {
      return var0.ah;
   }

   static long g(ImageGridView var0) {
      return var0.aj;
   }

   static int h(ImageGridView var0) {
      return var0.al;
   }

   static int i(ImageGridView var0) {
      return var0.F;
   }

   private void i() {
      if (this.t != null) {
         TextPaint var1 = this.y;
         var1.setTextAlign(Align.CENTER);
         var1.setTextSize(this.V * 18.0F);
         var1.setColor(this.B);
         this.u = new StaticLayout(this.t, var1, this.getClientWidth() * 9 / 10, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
      } else {
         this.u = null;
      }
   }

   static int j(ImageGridView var0) {
      return var0.E;
   }

   static float k(ImageGridView var0) {
      return var0.V;
   }

   static float l(ImageGridView var0) {
      return var0.aB;
   }

   static float m(ImageGridView var0) {
      return var0.az;
   }

   static float n(ImageGridView var0) {
      return var0.aA;
   }

   static int o(ImageGridView var0) {
      return var0.ax;
   }

   static int p(ImageGridView var0) {
      return var0.ay;
   }

   public long a(float var1, float var2) {
      float var5 = this.S + this.N;
      float var6 = this.T + this.O;
      float var3 = var1 + (this.getScrollX() - this.getPaddingLeft());
      float var4 = var2 + (this.getScrollY() - this.getPaddingTop());
      int var7;
      int var8;
      if (this.c) {
         var7 = (int)((var3 - this.N) / var5);
         var8 = (int)((var4 - this.O) / var6);
         var1 = var5 * var7 + this.N;
         var2 = var6 * var8 + this.O;
      } else {
         var7 = (int)((var4 - this.O) / var6);
         var8 = (int)((var3 - this.N) / var5);
         var2 = var6 * var7 + this.O;
         var1 = var5 * var8 + this.N;
      }

      long var9;
      if (var3 >= var1 && var3 < var1 + this.S && var4 >= var2 && var4 < var2 + this.T) {
         var9 = var7;
         var9 = var8 | var9 << 32;
      } else {
         var9 = -1L;
      }

      return var9;
   }

   public long a(boolean z, long j, RectF rectF) {
        Animation srcType0794;
        this.ai = j;
        if (j <= 0) {
            return 0L;
        }
        this.ag = 0.0f;
        if (this.ah != rectF) {
            this.ah = new RectF(rectF);
        }
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return j;
        }
        this.aj = System.currentTimeMillis() + j;
        float fMin = Math.min(rectF.width() / width, rectF.height() / height);
        float f = z ? fMin : 1.0f;
        float f2 = z ? 1.0f - fMin : fMin - 1.0f;
        if (z) {
            srcType0794 = new com.alensw.ui.view.af(this, f, f2);
        } else {
            this.au.set(0.0f, 0.0f, getWidth(), getHeight());
            srcType0794 = new com.alensw.ui.view.ag(this, z, true, this.au, this.au, f, f2);
            this.o.a(false);
        }
        srcType0794.setDuration(j);
        srcType0794.setInterpolator(new com.alensw.ui.view.cc());
        srcType0794.setAnimationListener(new com.alensw.ui.view.ah(this, z));
        startAnimation(srcType0794);
        return j;
    }

   public void a(int var1) {
      if (this.G != -1) {
         this.d(this.G);
      }

      if (this.e != 2) {
         this.G = var1;
      }

      if (this.G != -1) {
         this.d(this.G);
      }
   }

   public void a(int var1, int var2, boolean var3) {
      if (this.r != null) {
         this.removeCallbacks(this.af);
         if (var1 < var2) {
            this.r.a(var1, var2, this.al);
         }

         if (var3) {
            this.postDelayed(this.af, 300L);
         }
      }
   }

   public void a(int var1, boolean var2) {
      this.e = var1;
      float var3;
      if (var2) {
         var3 = this.W;
      } else {
         var3 = this.aa;
      }

      this.U = var3;
      this.requestLayout();
   }

   protected void a(Canvas var1, int var2, RectF var3) {
      RectF var15;
      if (this.ah != null && this.ag != 0.0F) {
         float var5 = 1.0F - this.ag;
         float var9 = var3.centerX();
         float var10 = this.getScrollX();
         float var8 = var3.centerY();
         float var7 = this.getScrollY();
         float var6 = this.ah.centerX();
         float var4 = this.ah.centerY();
         var15 = this.aq;
         var15.set(var3);
         var15.offset((var6 - (var9 - var10)) * var5, (var4 - (var8 - var7)) * var5);
      } else {
         var15 = var3;
      }

      com.alensw.ui.view.ak var16 = this.e(var2);
      boolean var13;
      if ((var16.b & 2) != 0) {
         var13 = true;
      } else {
         var13 = false;
      }

      boolean var14 = this.isFocused();
      boolean var11;
      if (var2 == this.H && var14) {
         var11 = true;
      } else {
         var11 = false;
      }

      if (this.n == null && var11 && !var13 && (this.e == 2 || var14)) {
         this.a(var1, var15);
      }

      if (this.e == 2) {
         float var17 = var3.bottom + this.O / 2;
         this.y.setColor(-1602191232);
         float var19 = var3.left;
         float var18 = var3.left;
         var1.drawLine(var19, var17, this.S + var18, var17, this.y);
      }

      if (var2 != this.G) {
         if (this.ah == null && this.b) {
            if (var16.a == null) {
               com.alensw.ui.view.ak.b(var16, 0);
            } else {
               int var12;
               if (com.alensw.ui.view.ak.b(var16) >= 223) {
                  var12 = 255;
               } else {
                  var12 = com.alensw.ui.view.ak.b(var16) + 32;
               }

               com.alensw.ui.view.ak.b(var16, var12);
            }

            this.x.setAlpha(com.alensw.ui.view.ak.b(var16));
         } else {
            this.x.setAlpha(255);
         }

         if (var11 && !this.d) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.a(var1, var16, var15, var14, var13);
      }

      if ((var16.b & 16) != 0) {
         this.r.a(var2, var1, var15);
      } else if (var16.c != null) {
         if ((var16.b & 1) != 0) {
            var13 = true;
         } else {
            var13 = false;
         }

         this.a(var1, var16, var15, var13);
      }

      if (this.n != null && var2 == this.aw) {
         this.a(var1, var15);
      }
   }

   protected void a(Canvas var1, com.alensw.ui.view.ak var2, RectF var3, boolean var4) {
      float var7 = this.V * 4.0F;
      float var8 = this.V * 2.0F;
      float var5;
      if (this.e == 2) {
         var5 = this.U + this.V * 2.0F;
      } else {
         var5 = this.U;
      }

      int var12 = this.B;
      int var11 = -1610612736 | 16777215 & var12;
      String var16 = var2.d[0];
      TextPaint var14 = this.y;
      RectF var15 = this.ap;
      var15.set(var3);
      var14.setTextAlign(Align.LEFT);
      com.alensw.compat.LegacyPlatform.save(var1, 2);
      if (this.e == 1) {
         float var6;
         if (var16 != null) {
            var6 = this.aa + var8;
         } else {
            var6 = 0.0F;
         }

         var15.top = var15.bottom - (var6 + var5 + 5.0F * var8);
         var14.setColor(this.z);
         var1.drawRect(var15, var14);
         var15.inset(var7, 0.0F);
         var1.clipRect(var15);
         var15.top += 2.0F * var8;
         if (var16 != null) {
            var14.setColor(var11);
            var14.setTextSize(this.aa);
            var1.drawText(var16, var15.left, var15.top + var5 + var8 * 2.0F + this.aa, var14);
         }

         var14.setTextSize(var5);
      } else if (this.e == 2) {
         int var13 = var2.d.length;
         float var17 = this.aa;
         var8 = var13 - 1;
         var8 = (var15.height() - (var17 * var8 + var5)) / (var13 + 1);
         var15.left = var15.left + (this.Q + var7);
         var15.top += var8;
         var15.inset(var7, 0.0F);
         var1.clipRect(var15);
         var14.setColor(var11);
         if (var16 != null) {
            var14.setTextSize(var5);
            float var9 = var14.measureText(var2.c);
            var14.setTextSize(this.aa);
            var17 = var14.measureText(var16);
            var9 = Math.min(var9 + var15.left + var7, var15.right - var17);
            var14.setColor(var11);
            var1.drawText(var16, var9, var15.top + var5, var14);
            var15.right -= var7 + var17;
         } else {
            var14.setTextSize(this.aa);
         }

         var7 = var15.left;
         var17 = var15.top;
         var17 = this.aa + (var17 + var5 + var8);

         for (int var10 = 1; var10 < var13 && var2.d[var10] != null; var10++) {
            var1.drawText(var2.d[var10], var7, var17, var14);
            var17 += this.aa + var8;
         }

         var14.setTextSize(var5);
      } else if (this.e == 3) {
         var15.top = var15.bottom - var5 - this.ab;
         var15.inset(var7, var8);
         var1.clipRect(var15);
         var14.setTextSize(var5);
         var8 = var14.measureText(var2.c);
         if (var16 != null) {
            var14.setTextSize(this.aa);
            float var21 = var14.measureText(var16);
            float var29 = (var15.left + var15.right - (var8 + var21 + var7)) / 2.0F;
            if (var15.left < var29) {
               var15.left = var29;
            }

            var8 = Math.min(var8 + var15.left + var7, var15.right - var21);
            var14.setColor(var11);
            var1.drawText(var16, var8, var15.top + var5, var14);
            var15.right -= var7 + var21;
         } else {
            float var22 = (var15.left + var15.right - var8) / 2.0F;
            if (var15.left < var22) {
               var15.left = var22;
            }
         }

         var14.setTextSize(var5);
      }

      var1.restore();
      boolean var30;
      if (var15.right < var3.right) {
         var30 = true;
      } else {
         var30 = false;
      }

      if (var30) {
         com.alensw.compat.LegacyPlatform.save(var1, 2);
         var1.clipRect(var15);
      }

      if (!var4) {
         var11 = var12;
      }

      var14.setColor(var11);
      var1.drawText(var2.c, var15.left, var15.top + var14.getTextSize(), var14);
      if (var30) {
         var1.restore();
      }
   }

   protected void a(Canvas canvas, com.alensw.ui.view.ak srcType0798, RectF rectF, boolean z, boolean z2) {
        RectF rectF2 = this.an;
        rectF2.set(rectF);
        boolean z3 = srcType0798.a != null;
        int alpha = this.x.getAlpha();
        if ((!z3 || alpha < 255) && this.ah == null && this.e <= 1) {
            this.y.setColor(((z3 ? ((255 - alpha) * 48) / 255 : 48) << 24) | 8421504);
            if (z) {
                rectF2.inset(this.P, this.P);
            }
            a(rectF2, this.Q, this.R);
            canvas.drawRect(rectF2, this.y);
        }
        if (z3) {
            int iE = srcType0798.a.e();
            int iF = srcType0798.a.f();
            if (z && this.e != 2) {
                rectF2.inset(this.P, this.P);
            }
            a(rectF2, iE, iF);
            float fWidth = rectF2.width() - iE;
            float fHeight = rectF2.height() - iF;
            Bitmap bitmapD = srcType0798.a.d();
            if (bitmapD != null && !a && Math.abs(fWidth) <= 2.0f && Math.abs(fHeight) <= 2.0f) {
                canvas.drawBitmap(bitmapD, rectF2.left + (fWidth / 2.0f), rectF2.top + (fHeight / 2.0f), this.x);
            } else if (bitmapD != null) {
                this.ao.set(0, 0, iE, iF);
                canvas.drawBitmap(bitmapD, this.ao, rectF2, this.x);
            } else {
                this.ao.set(0, 0, iE, iF);
                srcType0798.a.a(canvas, this.ao, rectF2, this.x);
            }
        }
        if (!z2 || this.s == null) {
            return;
        }
        float f = this.V * 4.0f;
        canvas.drawBitmap(this.s, (rectF.right - f) - this.s.getWidth(), f + rectF.top, (Paint) null);
    }

   protected void a(RectF var1, int var2, int var3) {
      if (this.e == 2) {
         var1.right = var1.left + this.Q;
      } else if (this.e == 3) {
         var1.bottom = var1.bottom - (this.U + this.ab);
      }

      float var6 = var1.width();
      float var5 = var1.height();
      float var7 = Math.min(var6 / var2, var5 / var3);
      float var4 = var2 * var7;
      var7 *= var3;
      float var8 = var1.left;
      var1.left = (var6 - var4) / 2.0F + var8;
      var1.right = var1.left + var4;
      var1.top += (var5 - var7) / 2.0F;
      var1.bottom = var1.top + var7;
   }

   @Override
   public void a(MotionEvent var1) {
      int var2 = this.b(var1.getX(), var1.getY());
      this.aw = var2;
      this.av = var2;
   }

   @Override
   public void a(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      if (this.H != -1) {
         this.post(this.ac);
      }
   }

   public void a(com.alensw.ui.view.aj var1, int var2, int var3) {
      this.r = var1;
      this.Q = var2;
      this.R = var3;
      this.requestLayout();
   }

   public boolean a() {
      int var4 = this.getCount();
      float var1;
      float var7;
      int var9;
      if (this.c) {
         var7 = this.S + this.N;
         var9 = this.getScrollX();
         var1 = (var9 - this.getPaddingLeft() - this.N) / var7;
         var7 = (var9 + this.getWidth() - this.N) / var7;
         var9 = this.D;
      } else {
         var7 = this.T + this.O;
         var9 = this.getScrollY();
         var1 = (var9 - this.getPaddingTop() - this.O) / var7;
         var7 = (var9 + this.getHeight() - this.O) / var7;
         var9 = this.C;
      }

      int var5 = Math.max((int)var1 * var9, 0);
      var9 = Math.min((int)Math.ceil(var7) * var9, var4);
      boolean var6;
      if (this.E == var5 && this.F == var9) {
         var6 = false;
      } else {
         this.E = var5;
         this.F = var9;
         var6 = true;
      }

      return var6;
   }

   public boolean a(int var1, RectF var2) {
      boolean var5;
      if (var1 >= 0 && var1 < this.getCount() && this.C > 0 && this.D > 0) {
         float var4 = this.S + this.N;
         float var3 = this.T + this.O;
         if (this.c) {
            var2.left = var4 * (var1 / this.D) + this.N;
            var2.top = var1 % this.D * var3 + this.O;
         } else {
            var2.left = var4 * (var1 % this.C) + this.N;
            var2.top = var1 / this.C * var3 + this.O;
         }

         var2.left = var2.left + (-this.getScrollX() + this.getPaddingLeft());
         var2.top = var2.top + (-this.getScrollY() + this.getPaddingTop());
         var2.right = var2.left + this.S;
         var2.bottom = var2.top + this.T;
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public int b(float var1, float var2) {
      byte var4 = -1;
      long var7 = this.a(var1, var2);
      int var3;
      if (var7 == -1L) {
         var3 = var4;
      } else {
         int var5 = (int)(var7 >> 32 & 2147483647L);
         int var6 = (int)var7;
         if (this.c) {
            var3 = this.D;
         } else {
            var3 = this.C;
         }

         var5 = var3 * var5 + var6;
         var3 = var4;
         if (var5 >= 0) {
            var3 = var4;
            if (var5 < this.getCount()) {
               var3 = var5;
            }
         }
      }

      return var3;
   }

   public int b(int var1, boolean var2) {
      if (this.H == var1) {
         var1 = this.H;
      } else {
         this.removeCallbacks(this.ac);
         if (this.H != -1) {
            this.d(this.H);
         }

         if (var1 < 0 || var1 >= this.getCount()) {
            var1 = -1;
         }

         this.H = var1;
         if (this.H != -1) {
            this.d(this.H);
            if (var2) {
               this.c(this.H);
            }
         }

         if (this.H != -1) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.setPressed(var2);
         var1 = this.H;
      }

      return var1;
   }

   public void b() {
      for (int var1 = 0; var1 < this.v.length; var1++) {
         if (this.v[var1] != null) {
            this.v[var1].a();
         }
      }

      this.invalidate();
   }

   public void b(int var1) {
      if (this.b(var1, true) != -1) {
         this.postDelayed(this.ac, this.l);
      }
   }

   @Override
   public void b(MotionEvent var1) {
      int var2 = this.b(var1.getX(), var1.getY());
      this.aw = var2;
      this.b(var2, false);
   }

   @Override
   public void b(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      if (this.H != -1) {
         this.post(this.ac);
      }
   }

   public boolean b(int var1, RectF var2) {
      boolean var5;
      if (this.a(var1, var2)) {
         int var3 = this.Q;
         int var4 = this.R;
         com.alensw.ui.view.ak var6 = this.e(var1);
         var1 = var4;
         if (var6.a != null) {
            var3 = var6.a.g();
            var1 = var6.a.h();
            var6.a.m();
            var6.a = null;
         }

         this.a(var2, var3, var1);
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public void c() {
      this.removeCallbacks(this.af);
   }

   @Override
   public void c(float var1, float var2) {
      if (this.c) {
         var1 *= this.S;
      } else {
         var2 *= this.T;
      }

      this.a((int)var1, (int)var2, this.getScrollX(), this.getScrollY(), this.i, this.i, false);
   }

   public void c(int var1) {
      RectF var4 = this.w;
      if (this.a(var1, var4)) {
         if (this.c) {
            int var3 = this.getClientWidth();
            var1 = this.getPaddingLeft();
            var3 += var1;
            float var2 = var4.width() + this.N;
            if (var4.left >= var3) {
               var2 = var4.left - var3 + var2;
            } else if (var4.right <= var1) {
               var2 = var4.right - var1 - var2;
            } else {
               var2 = 0.0F;
            }

            if (var2 != 0.0F) {
               this.scrollBy(Math.round(var2), 0);
            }
         } else {
            int var11 = this.getClientHeight();
            var1 = this.getPaddingTop();
            var11 += var1;
            float var8 = var4.height() + this.O;
            if (var4.top >= var11) {
               var8 = var4.top - var11 + var8;
            } else if (var4.bottom <= var1) {
               var8 = var4.bottom - var1 - var8;
            } else {
               var8 = 0.0F;
            }

            if (var8 != 0.0F) {
               this.scrollBy(0, Math.round(var8));
            }
         }
      }
   }

   @Override
   public void c(MotionEvent var1) {
      int var2 = this.b(var1.getX(), var1.getY());
      if (var2 != this.av) {
         var2 = -1;
      }

      this.b(var2, false);
      this.av = -1;
      if (this.H != -1 && this.isShown()) {
         this.performClick();
      }
   }

   @Override
   protected int computeHorizontalScrollRange() {
      return this.K;
   }

   @Override
   protected int computeVerticalScrollRange() {
      return this.L;
   }

   protected void d() {
      float var3 = this.getClientWidth();
      float var2 = this.getClientHeight();
      int var6 = this.getCount();
      int var4 = Math.round(this.V * this.M);
      this.O = var4;
      this.N = var4;
      if (this.c) {
         if (this.e == 2) {
            this.D = a((var2 - this.N) / this.R);
            this.S = Math.min(this.V * 400.0F, var3) - this.N;
            this.T = (var2 - this.O * (this.D + 1)) / this.D;
         } else {
            int var5 = this.R;
            var4 = var5;
            if (this.e == 3) {
               var4 = (int)(var5 + (this.U + this.ab));
            }

            this.D = a((var2 - this.O) / (var4 + this.O));
            if (this.D > 8 && (this.D & 1) == 1) {
               this.D--;
            }

            this.T = (var2 - this.O * (this.D + 1)) / this.D;
            float var1;
            if (this.e == 3) {
               var1 = this.T - this.U - this.ab;
            } else {
               var1 = this.T;
            }

            this.S = var1 * this.Q / this.R;
         }

         this.C = (this.D + var6 - 1) / this.D;
      } else {
         if (this.e == 2) {
            this.C = a((var3 - this.N) / (this.V * 400.0F));
            this.S = (var3 - this.N * (this.C + 1)) / this.C - this.N;
            this.T = this.R;
         } else {
            this.C = a((var3 - this.N) / (this.Q + this.N));
            if (this.C > 8 && (this.C & 1) == 1) {
               this.C--;
            }

            this.S = (var3 - this.N * (this.C + 1)) / this.C;
            this.T = this.S * this.R / this.Q;
            if (this.e == 3) {
               this.T = this.T + (this.U + this.ab);
            }
         }

         this.D = (this.C + var6 - 1) / this.C;
      }

      this.K = Math.round(Math.max((this.S + this.N) * this.C + this.N, var3));
      this.L = Math.round(Math.max((this.T + this.O) * this.D + this.O, var2));
   }

   @Override
   public void d(MotionEvent var1) {
      this.aA = var1.getX();
      float var2 = var1.getY();
      this.az = var2;
      this.aB = var2;
      if (this.H != -1 && this.isShown()) {
         this.performLongClick();
         if (this.d && this.isPressed() && this.isShown()) {
            this.a(this.aC);
         }
      }
   }

   public boolean d(int var1) {
      if (var1 >= this.E && var1 < this.F) {
         RectF var3 = this.w;
         if (this.a(var1, var3)) {
            com.alensw.ui.view.ak var4 = this.v[var1 % this.v.length];
            if (var4 != null) {
               var4.a();
            }

            var3.offset(this.getScrollX(), this.getScrollY());
            this.invalidate((int)var3.left, (int)var3.top, Math.round(var3.right), Math.round(var3.bottom));
            return true;
         }
      }

      return false;
   }

   protected com.alensw.ui.view.ak e(int var1) {
      com.alensw.ui.view.ak var4 = this.v[var1 % this.v.length];
      com.alensw.ui.view.ak var3 = var4;
      if (var4 == null) {
         com.alensw.ui.view.ak[] var6 = this.v;
         int var2 = this.v.length;
         var3 = new com.alensw.ui.view.ak(this);
         var6[var1 % var2] = var3;
      }

      boolean var5;
      if (com.alensw.ui.view.ak.a(var3) != var1) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5 || var3.a == null) {
         var3.a();
         com.alensw.ui.view.ak.a(var3, var1);
         if (var5) {
            com.alensw.ui.view.ak.b(var3, 255);
         }

         if (this.r != null) {
            this.r.a(var1, var3);
         }
      }

      return var3;
   }

   @Override
   public String e() {
      int var3 = this.E;
      RectF var4 = this.w;
      int var1 = var3;
      if (this.a(var3, var4)) {
         int var2 = var3;
         if (this.c) {
            var2 = var3;
            if (var4.right <= this.getPaddingLeft()) {
               var2 = var3 + this.D;
            }
         }

         var1 = var2;
         if (!this.c) {
            var1 = var2;
            if (var4.bottom <= this.getPaddingTop()) {
               var1 = var2 + this.C;
            }
         }
      }

      String var5;
      if (this.r != null && var1 >= 0 && var1 < this.r.a()) {
         var5 = this.r.a(var1);
      } else {
         var5 = null;
      }

      return var5;
   }

   public final int getCount() {
      int var1;
      if (this.r != null) {
         var1 = this.r.a();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public final int getFirstVisible() {
      return Math.max(this.E, 0);
   }

   @Override
   public void getFocusedRect(Rect var1) {
      int var2 = this.getPaddingLeft();
      int var3 = this.getHeight() / 2;
      var1.set(var2, var3 - 10, var2 + 20, var3 + 10);
   }

   public final int getLastVisible() {
      return Math.min(this.F, this.getCount());
   }

   public float getScrollPos() {
      float var1;
      if (this.c) {
         var1 = this.getScrollPosX();
      } else {
         var1 = this.getScrollPosY();
      }

      return var1;
   }

   public final int getSelectedIndex() {
      return this.H;
   }

   public final int getVisibleCount() {
      return this.F - this.E;
   }

   @Override
   protected void onDetachedFromWindow() {
      for (int var1 = 0; var1 < this.v.length; var1++) {
         if (this.v[var1] != null) {
            if (a) {
               this.v[var1].a = null;
            }

            this.v[var1].a();
            this.v[var1] = null;
         }
      }

      super.onDetachedFromWindow();
   }

   @Override
   protected void onDraw(Canvas canvas) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = this.ah != null;
        Animation animation = getAnimation();
        if (animation instanceof com.alensw.ui.view.bu) {
            RectF rectF = this.au;
            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
            boolean zA = ((com.alensw.ui.view.bu) animation).a(rectF, rectF);
            if (zA) {
                rectF.offset(getScrollX(), getScrollY());
                com.alensw.compat.LegacyPlatform.save(canvas, 2);
                canvas.clipRect(rectF, android.graphics.Region.Op.INTERSECT);
                z = zA;
                z2 = true;
            } else {
                z = zA;
                z2 = z4;
            }
        } else {
            z = false;
            z2 = z4;
        }
        int count = getCount();
        if (count != 0) {
            Rect rect = this.at;
            canvas.getClipBounds(rect);
            int paddingLeft = this.N + getPaddingLeft();
            int paddingTop = this.O + getPaddingTop();
            int iMax = Math.max(this.E, 0);
            int iMin = Math.min(this.F, count);
            int i = this.c ? this.D : this.C;
            int i2 = iMax / i;
            float f = this.N + this.S;
            float f2 = this.O + this.T;
            RectF rectF2 = this.w;
            if (this.c) {
                rectF2.left = (i2 * f) + paddingLeft;
                rectF2.top = paddingTop;
            } else {
                rectF2.left = paddingLeft;
                rectF2.top = (i2 * f2) + paddingTop;
            }
            this.x.setFilterBitmap(this.ah == null && !f());
            int i3 = iMax;
            boolean z5 = z2;
            while (i3 < iMin) {
                rectF2.right = rectF2.left + this.S;
                rectF2.bottom = rectF2.top + this.T;
                if (rectF2.intersects(rect.left, rect.top, rect.right, rect.bottom)) {
                    a(canvas, i3, rectF2);
                    z3 = (this.x.getAlpha() < 255) | z5;
                } else {
                    z3 = z5;
                }
                if (this.c) {
                    rectF2.top += f2;
                } else {
                    rectF2.left += f;
                }
                if ((i3 + 1) % i == 0) {
                    if (this.c) {
                        rectF2.top = paddingTop;
                        rectF2.left += f;
                    } else {
                        rectF2.left = paddingLeft;
                        rectF2.top += f2;
                    }
                }
                i3++;
                z5 = z3;
            }
            z2 = z5;
        } else if (this.t != null && this.u != null) {
            float clientWidth = getClientWidth();
            float f3 = this.V * 20.0f;
            TextPaint textPaint = this.y;
            textPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
            textPaint.setTextSize(this.V * 18.0f);
            textPaint.setColor(this.B);
            String str = this.t;
            int lineCount = this.u.getLineCount();
            float paddingLeft2 = (clientWidth / 2.0f) + getPaddingLeft();
            int i4 = 0;
            int i5 = 0;
            float clientHeight = ((getClientHeight() - (lineCount * f3)) / 2.0f) + this.V + getPaddingTop();
            while (i5 < lineCount) {
                int lineEnd = this.u.getLineEnd(i5);
                canvas.drawText(str, i4, lineEnd, paddingLeft2, clientHeight - textPaint.descent(), (Paint) textPaint);
                i5++;
                clientHeight += f3;
                i4 = lineEnd;
            }
        }
        if (z) {
            canvas.restore();
        }
        if (z2) {
            g();
        }
    }

   @Override
   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var7 = true;
      int var5 = this.getCount();
      int var3;
      if (this.c) {
         var3 = this.D;
      } else {
         var3 = 1;
      }

      int var4;
      if (this.c) {
         var4 = 1;
      } else {
         var4 = this.C;
      }

      int var6 = this.H;
      if (var6 != -1) {
         switch (var1) {
            case 19:
               var1 = var6 - var4;
               break;
            case 20:
               var1 = var6 + var4;
               break;
            case 21:
               var1 = var6 - var3;
               break;
            case 22:
               var1 = var3 + var6;
               break;
            default:
               var7 = super.onKeyDown(var1, var2);
               return var7;
         }
      } else {
         switch (var1) {
            case 19:
            case 20:
            case 21:
            case 22:
               var3 = this.E;
               if (this.c) {
                  var1 = var3;
                  if (this.a(var3, this.w)) {
                     var1 = var3;
                     if (this.w.left < -this.getPaddingLeft()) {
                        var1 = var3 + this.D;
                     }
                  }
               } else {
                  var1 = var3;
                  if (this.a(var3, this.w)) {
                     var1 = var3;
                     if (this.w.top < -this.getPaddingTop()) {
                        var1 = var3 + this.C;
                     }
                  }
               }

               var1 = Math.min(var5 - 1, var1);
               break;
            default:
               var7 = super.onKeyDown(var1, var2);
               return var7;
         }
      }

      label46: {
         if (var1 >= 0) {
            var3 = var1;
            if (var1 < var5) {
               break label46;
            }
         }

         var3 = -1;
      }

      this.b(var3, true);
      if (var3 == -1) {
         var7 = false;
      }

      return var7;
   }

   @Override
   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (var4 - var2 > 0 && var5 - var3 > 0) {
         if (this.c) {
            var2 = this.getScrollRangeX();
         } else {
            var2 = this.getScrollRangeY();
         }

         var5 = this.F;
         float var6 = this.am;
         this.d();
         var4 = this.getCount();
         if (this.ak != 0 && this.ak != var4) {
            if (this.c) {
               var3 = this.getScrollRangeX();
            } else {
               var3 = this.getScrollRangeY();
            }

            if (var3 > 0) {
               var6 = var2 * var6 / var3;
            }
         }

         this.ak = var4;
         this.setScrollPos(var6);
         if (this.a()) {
            this.al = this.F - var5;
         }

         var4 = this.F;
         var3 = this.E;
         if (this.c) {
            var2 = this.D;
         } else {
            var2 = this.C;
         }

         var2 = Math.max(var2 + (var4 - var3) + 3 >> 2 << 2, 8);
         if (this.v.length < var2) {
            com.alensw.ui.view.ak[] var7 = new com.alensw.ui.view.ak[var2];
            System.arraycopy(this.v, 0, var7, 0, this.v.length);
            this.v = var7;
         }

         this.a(this.E, this.F, true);
         this.o.c(this.h);
         this.b();
      }

      if (var1) {
         this.i();
      }
   }

   @Override
   protected void onScrollChanged(int var1, int var2, int var3, int var4) {
      super.onScrollChanged(var1, var2, var3, var4);
      this.am = this.getScrollPos();
      if (this.a()) {
         if (this.c) {
            var1 -= var3;
         } else {
            var1 = var2 - var4;
         }

         this.al = var1;
         this.a(this.E, this.F, true);
      }
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 > 0 && var2 > 0 && this.ah != null && this.ag == 0.0F) {
         this.a(true, this.ai, this.ah);
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      if (this.d && this.H != -1 && this.isPressed()) {
         switch (var1.getAction()) {
            case 1:
            case 3:
               this.ax = -1;
               this.ay = -1;
               this.az = -1.0F;
               this.aA = -1.0F;
               this.aB = -1.0F;
               this.removeCallbacks(this.aC);
               if (this.H != this.b(var1.getX(), var1.getY())) {
                  this.I = Integer.MAX_VALUE;
                  this.J = Integer.MIN_VALUE;
               }

               if (this.H != -1) {
                  this.post(this.ac);
               }
               break;
            case 2:
               this.aA = var1.getX();
               this.aB = var1.getY();
         }
      }

      return super.onTouchEvent(var1);
   }

   public void setEmptyText(String var1) {
      this.t = var1;
      this.i();
      if (this.getCount() == 0) {
         this.scrollTo(0, 0);
         this.am = 0.0F;
         this.requestLayout();
      }
   }

   public void setHorizontal(boolean var1) {
      if (!this.p.f()) {
         this.p.a(true);
      }

      if (this.c != var1) {
         this.c = var1;
         this.requestLayout();
      }
   }

   public void setMultiSelect(boolean var1) {
      this.d = var1;
      this.b();
   }

   public void setScrollPos(float var1) {
      this.am = var1;
      if (this.c) {
         this.setScrollPosX(var1);
      } else {
         this.setScrollPosY(var1);
      }
   }

   public void setSpaceWidth(int var1) {
      this.M = var1;
      this.requestLayout();
   }

   public void setTextMode(int var1) {
      this.a(var1, false);
   }
}
