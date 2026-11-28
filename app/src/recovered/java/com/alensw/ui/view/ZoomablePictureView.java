package com.alensw.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Build.VERSION;
import android.widget.OverScroller;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import com.alensw.PicFolder.QuickApp;
import com.alensw.image.BitmapUtils;
import com.alensw.image.Picture;
import com.alensw.ui.shape.GalleryDrawables;
public class ZoomablePictureView extends View {
   public static final Shader al;
   public static final boolean x;
   protected int A;
   protected int B;
   protected int C;
   protected int D;
   protected int E;
   protected boolean F;
   protected boolean G;
   protected boolean H;
   protected boolean I;
   protected ViewFrameAnimator J;
   protected PictureNavigationListener K;
   protected final Interpolator L;
   protected final Paint M;
   protected final Paint N;
   protected final Paint O;
   protected final RectF P = new RectF();
   protected final RectF Q = new RectF();
   protected final Matrix R = new Matrix();
   protected final Matrix S = new Matrix();
   protected final Matrix T = new Matrix();
   protected final Matrix U = new Matrix();
   protected final Matrix V = new Matrix();
   protected final float[] W = new float[3];
   private Picture a;
   protected final float[] aa = new float[2];
   protected final float[] ab = new float[9];
   public final int ac;
   public final int ad;
   protected final float ae;
   protected final float af;
   protected final float ag;
   protected final float ah;
   protected final float ai;
   protected final String aj;
   protected final String ak;
   private Picture b;
   private RectF c;
   private Bitmap d;
   private Canvas e;
   private final Matrix f = new Matrix();
   private final RectF g = new RectF();
   private final Matrix h = new Matrix();
   private int i;
   private int j;
   private float k;
   private float l;
   private OverScroller m;
   protected int y;
   protected int z;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      x = var0;
      al = GalleryDrawables.b(16);
   }

   public ZoomablePictureView(Context var1) {
      this(var1, null);
   }

   public ZoomablePictureView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setDrawingCacheEnabled(false);
      this.setWillNotCacheDrawing(true);
      Resources var3 = this.getResources();
      DisplayMetrics var5 = var3.getDisplayMetrics();
      ViewConfiguration var4 = ViewConfiguration.get(var1);
      this.ai = var5.density;
      this.ac = (int)(this.ai * 24.0F);
      this.y = var3.getColor(com.alensw.PicFolder.R.color.material_background_light);
      this.z = var3.getColor(com.alensw.PicFolder.R.color.material_warning);
      this.M = new Paint(6);
      this.N = new Paint(6);
      this.O = new Paint(1);
      this.O.setTextAlign(Align.CENTER);
      this.O.setTextSize(this.ai * 18.0F);
      this.aj = var3.getString(com.alensw.PicFolder.R.string.loading);
      this.ak = var3.getString(com.alensw.PicFolder.R.string.load_failed);
      this.L = new ViscousFluidInterpolator();
      this.ad = QuickApp.i;
      this.ae = var4.getScaledMaximumFlingVelocity();
      this.af = var4.getScaledMinimumFlingVelocity();
      this.ag = this.af;
      this.ah = this.ai * 256.0F;
   }

   private float a() {
      RectF var4 = this.P;
      float var3 = var4.width();
      float var1 = var4.left;
      float var2 = var4.right;
      if (var3 < this.C) {
         var2 = var1 - (this.C - var3) / 2.0F;
         var3 = this.C;
         var1 = var2;
         var2 = var3 + var2;
      }

      if (var1 > 0.0F) {
         this.A = -1;
      } else if (var2 < this.C) {
         this.A = 1;
         var1 = var2 - this.C;
      } else {
         this.A = 0;
         var1 = 0.0F;
      }

      return var1;
   }

   static Picture a(ZoomablePictureView var0) {
      return var0.b;
   }

   static Picture a(ZoomablePictureView var0, Picture var1) {
      var0.b = var1;
      return var1;
   }

   private void a(Canvas var1, Picture var2, Matrix var3, RectF var4, boolean var5) {
      Bitmap var15 = var2.d();
      Canvas var14 = this.e;
      Matrix var16 = this.f;
      int var10 = var15.getWidth();
      int var11 = var15.getHeight();
      int var12 = var2.b.d;
      boolean var7;
      if (var14 != null && var14 != var1) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var8;
      if (var12 % 180 != 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      boolean var13 = var2.i();
      boolean var6;
      if (var10 <= this.i && var11 <= this.j) {
         var6 = false;
      } else {
         var6 = true;
      }

      boolean var9 = var6;
      if (!var6) {
         var9 = var6;
         if (var5) {
            var9 = var6;
            if (var10 > this.i >> 1) {
               var9 = var6;
               if (var11 > this.j >> 1) {
                  var9 = true;
               }
            }
         }
      }

      if (var8 && (!var5 || var9) && var7) {
         com.alensw.compat.LegacyPlatform.save(var14, 2);
         var14.clipRect(0, 0, this.D, this.C);
         if (var13 || this.H) {
            var14.drawRect(0.0F, 0.0F, this.D, this.C, this.N);
         }

         var16.set(var3);
         var16.postRotate(360 - var12, this.C / 2.0F, this.D / 2.0F);
         var16.postTranslate((this.D - this.C) / 2.0F, (this.C - this.D) / 2.0F);
         var14.drawBitmap(var15, var16, this.M);
         var16.reset();
         var16.preTranslate(-this.D / 2.0F, -this.C / 2.0F);
         var16.postRotate(var12);
         var16.postTranslate(this.C / 2.0F, this.D / 2.0F);
         var1.drawBitmap(this.d, var16, null);
         var14.restore();
      } else if (var5 && var9 && var7) {
         com.alensw.compat.LegacyPlatform.save(var14, 2);
         var14.clipRect(0, 0, this.C, this.D);
         if (var13 || this.H) {
            var14.drawRect(var4, this.N);
         }

         var14.drawBitmap(var15, var3, this.M);
         var1.drawBitmap(this.d, 0.0F, 0.0F, null);
         var14.restore();
      } else {
         if (var13) {
            com.alensw.compat.LegacyPlatform.save(var1, 1);
            var1.concat(var3);
            var1.drawRect(0.0F, 0.0F, var10, var11, this.N);
            var1.restore();
         }

         var1.drawBitmap(var15, var3, this.M);
      }
   }

   private void a(Canvas var1, Picture var2, RectF var3) {
      if (var2.k()) {
         this.O.setColor(this.z);
         var1.drawText(this.ak, var3.centerX(), var3.centerY() + this.O.getTextSize() / 2.0F, this.O);
      } else {
         this.O.setColor(813727872);
         var1.drawRect(var3, this.O);
      }
   }

   private void a(Canvas var1, Picture var2, boolean var3, float var4) {
      var2.a(this.h, this.C, this.D, 1);
      this.h.postTranslate(var4, 0.0F);
      if (var2.k()) {
         this.g.set(var4, 0.0F, this.C + var4, this.D);
      } else {
         var2.a(this.h, this.g);
      }

      var1.save();
      this.transformPictureForDrawing(var1, this.g);
      var1.clipRect(this.g);
      if (var2.j()) {
         this.a(var1, var2, this.h, this.g, var3);
      } else {
         this.a(var1, var2, this.g);
      }

      var1.restore();
   }

   static OverScroller b(ZoomablePictureView var0) {
      return var0.m;
   }

   static Picture c(ZoomablePictureView var0) {
      return var0.a;
   }

   public float a(float var1, float var2) {
      if (this.getPictureType() <= 0) {
         var1 = 1.0F;
      } else {
         float[] var7 = this.k();
         float[] var8 = this.getScaleRanges();
         float var5 = this.getTransScale();
         int var6 = 0;

         float var3;
         for (var3 = var5; var6 < 3; var6++) {
            this.B = (this.B + 1) % 3;
            var3 = var7[this.B];
            if (Math.abs(var3 - var5) > 0.05F && var3 >= var8[0]) {
               break;
            }
         }

         float var4 = var3;
         if (var3 == var5) {
            var4 = 4.0F * var5 / 2.0F;
         }

         if (var4 != var5) {
            this.b(var4, var1, var2, this.ad);
         }

         var1 = var4 / var5;
      }

      return var1;
   }

   public Picture a(Config var1) {
      RectF var2 = this.getClipRect();
      Bitmap var3 = BitmapUtils.a(Math.round(var2.width()), Math.round(var2.height()), var1);
      Picture var5;
      if (var3 != null) {
         Canvas var4 = new Canvas(var3);
         var3.eraseColor(0);
         var4.translate(-var2.left, -var2.top);
         this.a(var4, false, false);
         var4.translate(var2.left, var2.top);
         var5 = new Picture(var3, 1);
      } else {
         var5 = null;
      }

      return var5;
   }

   public void a(float var1, float var2, float var3) {
      this.S.postScale(var1, var1, var2, var3);
      this.j();
      this.invalidate();
      if (!this.I && this.K != null) {
         this.K.a(var1, false);
      }
   }

   public void a(float f, float f2, float f3, float f4, float f5, float f6, int i, Runnable runnable) {
        float f7 = f2 - f;
        float f8 = f4 - f3;
        int iRound = (Math.round((f4 > 0.0f ? 45 : -45) + f4) / 90) * 90;
        Matrix matrix = new Matrix(this.S);
        if (f != f2) {
            this.S.postScale(f2 / f, f2 / f, f5, f6);
        }
        if (f3 != f4) {
            this.S.postRotate(f8, f5, f6);
        }
        j();
        float fCenterX = this.P.centerX();
        float fCenterY = this.P.centerY();
        c(0.0f);
        float fCenterX2 = this.P.centerX() - fCenterX;
        float fCenterY2 = this.P.centerY() - fCenterY;
        boolean z = f != f2;
        boolean z2 = f3 != f4;
        boolean z3 = (fCenterX2 == 0.0f && fCenterY2 == 0.0f) ? false : true;
        if (this.J != null) {
            this.J.c();
        }
        this.J = new PictureTransformAnimation(this, this, this.L, z, z2, z3, matrix, f, f7, f5, f6, f8, fCenterX2, fCenterY2, iRound, runnable, f2);
        this.J.a(i, false);
    }

   public void a(float var1, float var2, float var3, Runnable var4) {
      float var6 = 0.8F;
      float var8 = Math.abs(var1);
      float var5 = Math.abs(var2);
      float var7 = Math.abs(var3);
      if (var7 > 0.0F) {
         var3 = var8 / var7 / 2.0F * 1.25F;
      } else {
         var3 = 0.8F;
      }

      if (var7 > 0.0F) {
         var5 = var5 / var7 / 2.0F * 1.25F;
      } else {
         var5 = 0.8F;
      }

      var5 = Math.max(var3, var5);
      if (var5 < 0.4F) {
         var3 = 0.4F;
      } else {
         var3 = var6;
         if (!(var5 > 0.8F)) {
            var3 = var5;
         }
      }

      this.a(var1, var2, (int)(1000.0F * var3), var4);
   }

   public void a(float var1, float var2, int var3, Runnable var4) {
      if (this.J != null) {
         this.J.c();
      }

      this.J = new PicturePanAnimation(this, this, this.L, var1, var2, var4);
      this.J.a(var3, false);
   }

   public void a(float f, boolean bl, float f2, float f3, int n, Runnable runnable) {
        float f4;
        float f5;
        float f6 = this.getTransRotation();
        int n2 = f > 0.0f ? 45 : -45;
        n2 = Math.round((float)n2 + f) / 90 * 90;
        float[] fArray = this.getScaleRanges();
        if (n2 % 180 != 0) {
            float[] fArray2 = this.ab;
            this.R.getValues(fArray2);
            f5 = Picture.a(fArray2);
            if (this.a != null) {
                this.a.c(n2);
            }
            this.i();
            this.R.getValues(fArray2);
            f4 = Picture.a(fArray2);
            if (this.a != null) {
                this.a.c(-n2);
            }
            this.i();
            if (f5 > 0.0f) {
                fArray[0] = fArray[0] * f4 / f5;
            }
        } else {
            f4 = 1.0f;
            f5 = 1.0f;
        }
        float f7 = this.getTransScale();
        f4 = bl ? (f4 /= f5) : Math.max(fArray[0], Math.min(fArray[1], f7));
        this.a(f7, f4, f6, f, f2, f3, n, runnable);
    }

   public void a(int var1, boolean var2) {
      float[] var5 = this.k();
      if (var1 >= 0 && var1 < var5.length) {
         float var3 = this.k()[var1];
         float var4 = this.getTransScale();
         this.B = var1;
         if (var3 != var4) {
            if (var2) {
               this.b(var3, this.C / 2.0F, this.D / 2.0F, this.ad);
            } else {
               this.b(var3, this.C / 2.0F, this.D / 2.0F);
               this.c(0.0F);
            }
         }
      }
   }

   protected void a(Canvas var1, boolean var2, boolean var3) {
      int var4;
      if (this.b != null) {
         var4 = this.b.d;
      } else {
         var4 = 0;
      }

      if (var4 == 2 && this.b.j()) {
         this.a(var1, this.b, this.V, this.P, var2);
      } else if (var4 > 0) {
         this.b.a(var1, this.V, this.a, this.T, this.M, var3);
      } else if (this.a != null && this.a.j()) {
         this.a(var1, this.a, this.T, this.P, var2);
      } else if (this.a != null) {
         this.a(var1, this.a, this.P);
      } else {
         this.O.setColor(this.y);
         var1.drawText(this.aj, this.P.centerX(), this.P.centerY() + this.O.getTextSize() / 2.0F, this.O);
      }
   }

   public void a(Picture var1, boolean var2) {
      boolean var8 = true;
      Uri var9;
      if (var1 != null) {
         var9 = var1.i;
      } else {
         var9 = Uri.EMPTY;
      }

      boolean var7;
      if (this.a != null && var1 != null) {
         var7 = false;
      } else {
         var7 = true;
      }

      if (this.a != null && !this.a.a(var9)) {
         this.a.m();
         this.a = null;
      }

      if (this.b != null && !this.b.a(var9)) {
         this.b.a(true);
         this.b.m();
         this.b = null;
      }

      if (var1 != null) {
         if (var1.d <= 1) {
            if (this.a == null || this.a.d < var1.d) {
               if (this.a != null) {
                  this.a.m();
               }

               this.a = var1.c();
            }
         } else if (var1.d == 3) {
            if (this.a != null && this.a.j()) {
               if (this.b != null) {
                  if (this.b != var1) this.b.a(true);
                  this.b.m();
               }

               this.b = var1.c();
            }
         } else {
            if (this.b != null) {
               if (this.b != var1) this.b.a(true);
               this.b.m();
            }

            this.b = var1.c();
         }
      }

      if (var7 | var2) {
         this.S.reset();
         this.B = 0;
         this.b(true);
      } else if (!this.P.isEmpty()) {
         RectF var10 = this.P;
         float var3 = var10.width();
         float var4 = var10.height();
         float var6 = var10.left;
         float var5 = var10.top;
         this.b(false);
         if (Math.abs(var4 - this.D) < Math.abs(var3 - this.C)) {
            var7 = var8;
         } else {
            var7 = false;
         }

         var3 /= var10.width();
         var4 /= var10.height();
         if (var7) {
            var3 = var4;
         }

         this.a(var3, var10.centerX(), var10.centerY());
         if (var7) {
            this.b(0.0F, var5 - var10.top);
         } else {
            this.b(var6 - var10.left, 0.0F);
         }
      }
   }

   public void a(boolean var1, Runnable var2) {
      float var4 = this.C / 2.0F;
      float var3 = this.D / 2.0F;
      Camera var5 = new Camera();
      Matrix var6 = new Matrix();
      Matrix var7 = new Matrix(this.S);
      if (this.J != null) {
         this.J.c();
      }

      this.J = new PictureFlipAnimation(this, this, this.L, var5, var1, var6, var4, var3, var7, var2);
      this.J.a(this.ad, false);
   }

   public boolean a(float f) {
        int i;
        float fA = a();
        float fAbs = Math.abs(fA);
        if (fAbs > this.ac && ((int) Math.signum(fA)) != ((int) Math.signum(f))) {
            return false;
        }
        if (f > 0.0f) {
            i = 1;
        } else {
            i = f < 0.0f ? -1 : 0;
        }
        int i2 = -i;
        if (this.K == null || !this.K.a(i2)) {
            this.A = 0;
            c(f);
            return true;
        }
        if (this.b != null) {
            this.b.a(true);
        }
        Picture srcType0202A = this.K.a(i2, true);
        if (srcType0202A != null) {
            srcType0202A.m();
        }
        this.G = true;
        this.A = i2;
        a(f < 0.0f ? (fAbs - this.C) - this.ac : (this.C - fAbs) + this.ac, 0.0f, f, new FinishPictureSwipe(this, i2));
        return true;
    }

   public void b(float var1, float var2) {
      this.S.postTranslate(var1, var2);
      this.j();
      this.invalidate();
   }

   public void b(float var1, float var2, float var3) {
      this.a(var1 / this.getTransScale(), var2, var3);
   }

   public void b(float var1, float var2, float var3, int var4) {
      if (var4 > this.ad) {
         float var5 = this.getTransScale();
         if (this.J != null) {
            this.J.c();
         }

         this.J = new PictureZoomAnimation(this, this, this.L, var5, var1 - var5, var2, var3, var1);
         this.J.a(var4, false);
      } else {
         float var6 = this.getTransRotation();
         this.a(this.getTransScale(), var1, var6, var6, var2, var3, var4, null);
      }
   }

   public void b(int var1) {
      float var2 = this.getTransRotation();
      this.a(var1 + var2, false, this.C / 2.0F, this.D / 2.0F, this.ad, null);
   }

   public void b(int var1, int var2) {
      this.y = var1;
      this.N.setColor(var2);
      this.invalidate();
   }

   public void b(RectF var1) {
      if (this.b != null) {
         this.b.a(this.U, var1);
      } else if (this.a != null) {
         this.a.a(this.R, var1);
      }
   }

   public void b(boolean var1) {
      this.i();
      this.j();
      if (var1) {
         this.invalidate();
      }
   }

   public boolean b(float var1) {
      float var2 = this.a();
      var1 = Math.max(Math.abs(var1), this.ah);
      boolean var4;
      if (this.A != 0 && Math.abs(var2) > this.E) {
         byte var3;
         if (this.A > 0) {
            var3 = -1;
         } else {
            var3 = 1;
         }

         var4 = this.a(var3 * var1);
      } else {
         var4 = this.c(var1);
      }

      return var4;
   }

   public void c(float var1, float var2) {
      RectF var5 = this.P;
      RectF var4 = this.Q;
      if ((int)(var5.height() - var4.height()) <= 0) {
         var2 = 0.0F;
      }

      float var3 = var1;
      if (this.K == null) {
         var3 = var1;
         if ((int)(var5.width() - var4.width()) <= 0) {
            var3 = 0.0F;
         }
      }

      if (var3 != 0.0F || var2 != 0.0F) {
         this.b(var3, var2);
      }
   }

   public void c(float var1, float var2, float var3) {
      this.H = true;
      this.S.postRotate(var1, var2, var3);
      this.j();
      this.invalidate();
   }

   public void c(boolean var1) {
      Canvas var2 = this.e;
      if (var1 && var2 != null) {
         if (this.d != null) {
            this.d.eraseColor(0);
         }

         this.c = this.getClipRect();
         com.alensw.compat.LegacyPlatform.save(var2, 2);
         var2.clipRect(this.c);
         this.a(var2, false, false);
         var2.restore();
      } else {
         this.c = null;
         this.invalidate();
      }
   }

   public boolean c(float var1) {
      RectF var9 = this.Q;
      RectF var10 = this.P;
      float var2 = var10.width();
      float var3 = var10.height();
      float var7 = var9.width();
      float var4 = var9.height();
      if (var2 <= var7) {
         float var6 = var9.left;
         float var5 = var10.left;
         var2 = (var7 - var2) / 2.0F + (var6 - var5);
      } else if (var10.left > var9.left) {
         var2 = var9.left - var10.left;
      } else if (var10.right < var9.right) {
         var2 = var9.right - var10.right;
      } else {
         var2 = 0.0F;
      }

      if (var3 <= var4) {
         var3 = var9.top - var10.top + (var4 - var3) / 2.0F;
      } else if (var10.top > var9.top) {
         var3 = var9.top - var10.top;
      } else if (var10.bottom < var9.bottom) {
         var3 = var9.bottom - var10.bottom;
      } else {
         var3 = 0.0F;
      }

      boolean var8;
      if (!(Math.abs(var2) > 0.01F) && !(Math.abs(var3) > 0.01F)) {
         this.invalidate();
         var8 = false;
      } else {
         if (var1 != 0.0F) {
            this.a(var2, var3, Math.signum(var1) * Math.min(Math.abs(var1), this.ag * 2.0F), null);
         } else {
            this.b(var2, var3);
         }

         var8 = true;
      }

      return var8;
   }

   public float d(boolean var1) {
      float var2;
      if (this.getPictureType() <= 0) {
         var2 = 1.0F;
      } else {
         float[] var7 = this.getScaleRanges();
         float var3 = this.getTransScale();
         float var5 = this.C / 2.0F;
         float var4 = this.D / 2.0F;
         if (var1) {
            var2 = 1.4142135F;
         } else {
            var2 = 0.70710677F;
         }

         float var6 = var3 * var2;
         if (var6 > var7[1]) {
            this.a(var2, var5, var4);
            this.e(var5, var4);
         } else if (var6 < var7[0]) {
            this.a(var2, var5, var4);
            this.e(var5, var4);
         } else if (var6 != var3) {
            this.b(var6, var5, var4, this.ad);
         }

         var2 = var6 / var3;
      }

      return var2;
   }

   public boolean d(float var1, float var2) {
      RectF var12 = this.P;
      float var5 = var12.width();
      float var6 = var12.height();
      float var4 = this.C;
      float var3 = this.D;
      int var8 = (int)Math.max(var5 - var4, 0.0F);
      int var9 = (int)Math.max(var6 - var3, 0.0F);
      if (!(Math.ceil(var12.left) >= 0.0) || !(var1 >= this.ah) || var9 != 0 && !(Math.abs(var1) > Math.abs(var2))) {
         if (Math.floor(var12.right) <= var4 && var1 <= -this.ah && (var9 == 0 || Math.abs(var1) > Math.abs(var2)) && this.a(var1)) {
            boolean var14 = true;
            return var14;
         }
      } else if (this.a(var1)) {
         return true;
      }

      var5 = (float)Math.hypot(var1, var2);
      if (var8 == 0) {
         var1 = 0.0F;
      }

      if (var9 == 0) {
         var2 = 0.0F;
      }

      boolean var11;
      if (var1 == 0.0F && var2 == 0.0F) {
         var11 = this.b(var5 * 0.8F);
      } else {
         int var10 = -((int)(0.8F * var1));
         int var7 = -((int)(0.8F * var2));
         if (this.J != null) {
            this.J.c();
         }

         if (this.m == null) {
            this.m = new OverScroller(this.getContext());
         }

         this.J = new PictureFlingAnimation(this, this, null, var12, var10, var7, var8, var9);
         this.J.a(0L, false);
         var11 = true;
      }

      return var11;
   }

   public void e(float var1, float var2) {
      float[] var5 = this.getScaleRanges();
      float var4 = this.getTransScale();
      float var3 = Math.max(var5[0], Math.min(var5[1], var4));
      if (var3 != var4) {
         this.b(var3, var1, var2, this.ad);
      } else {
         this.l();
      }
   }

   public boolean e() {
      boolean var1 = true;
      if (this.b == null) {
         if (this.a != null) {
            if (this.a.d <= 1) {
               var1 = false;
            }
         } else {
            var1 = false;
         }
      }

      return var1;
   }

   public void f() {
      this.c(false);
      if (this.b != null) {
         this.b.a(this.C, this.D, this.V, this.P);
         this.b.a(this, this.N);
         this.invalidate();
      }
   }

   public boolean hasAnimationPreview() {
      return "image/gif".equals(this.getMimeType())
              || (this.b == null && this.a != null && this.a.animatedPreview);
   }

   public void g() {
      this.h();
      if (this.b != null) {
         if (this.b.d == 2) {
            if (this.b.g() * this.b.h() > 3145728) {
               this.c(true);
               this.b.m();
               this.b = null;
            }
         } else {
            this.c(true);
            this.b.a(true);
         }
      }
   }

   public RectF getClipRect() {
      RectF var2 = new RectF(0.0F, 0.0F, this.C, this.D);
      if (var2.isEmpty()) {
         View var3 = this.getRootView();
         var2.right = var2.left + var3.getWidth();
         float var1 = var2.top;
         var2.bottom = var3.getHeight() + var1;
      }

      var2.intersect(this.P);
      return var2;
   }

   public Picture getFullPicture() {
      Picture var1;
      if (this.b != null) {
         var1 = this.b.c();
      } else {
         var1 = null;
      }

      return var1;
   }

   public String getMimeType() {
      String var1;
      if (this.b != null) {
         var1 = this.b.h;
      } else if (this.a != null) {
         var1 = this.a.h;
      } else {
         var1 = "";
      }

      return var1;
   }

   public Picture getPicture() {
      Picture var1;
      if (this.a != null) {
         var1 = this.a.c();
      } else {
         var1 = null;
      }

      return var1;
   }

   public long getPictureDuration() {
      long var1;
      if (this.b != null) {
         var1 = this.b.b();
      } else if (this.a != null) {
         var1 = this.a.b();
      } else {
         var1 = 0L;
      }

      return var1;
   }

   public int getPictureHeight() {
      int var1;
      if (this.b != null) {
         var1 = this.b.h();
      } else if (this.a != null) {
         var1 = this.a.h();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getPictureRotation() {
      int var1;
      if (this.b != null) {
         var1 = this.b.b.d;
      } else if (this.a != null) {
         var1 = this.a.b.d;
      } else {
         var1 = 360;
      }

      return var1;
   }

   public int getPictureType() {
      int var1;
      if (this.b != null) {
         var1 = this.b.d;
      } else if (this.a != null) {
         var1 = this.a.d;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public Uri getPictureUri() {
      Uri var1;
      if (this.b != null) {
         var1 = this.b.i;
      } else if (this.a != null) {
         var1 = this.a.i;
      } else {
         var1 = Uri.EMPTY;
      }

      return var1;
   }

   public int getPictureWidth() {
      int var1;
      if (this.b != null) {
         var1 = this.b.g();
      } else if (this.a != null) {
         var1 = this.a.g();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public float[] getScaleRanges() {
      float[] var1 = this.k();
      this.aa[0] = Math.min(var1[2], var1[0]);
      this.aa[1] = Math.max(var1[2] * 4.0F, var1[0]);
      return this.aa;
   }

   public float getTransRotation() {
      return Picture.b(this.getTransValues());
   }

   public float getTransScale() {
      return Picture.a(this.getTransValues());
   }

   public float[] getTransValues() {
      this.S.getValues(this.ab);
      return this.ab;
   }

   public void h() {
      if (this.J != null) {
         this.J.c();
         this.J = null;
      }

      boolean var1 = this.G;
      this.F = false;
      this.G = false;
      this.I = false;
      if (!var1) {
         this.c(0.0F);
      } else {
         this.invalidate();
      }
   }

   protected void i() {
      float var1 = this.C;
      float var2 = this.D;
      if (this.a != null && !this.a.k()) {
         this.a.a(this.R, var1, var2, 1);
      } else {
         this.R.reset();
      }

      if (this.b != null) {
         if (this.a != null) {
            this.b.b.d = this.a.b.d;
            this.k = (float)this.b.e() / this.a.e();
            this.l = (float)this.b.f() / this.a.f();
         }

         this.b.a(this.U, var1, var2, 1);
      } else {
         this.U.reset();
      }
   }

   protected void j() {
      if (this.b != null) {
         this.V.set(this.U);
         this.V.postConcat(this.S);
         this.b.a(this.V, this.P);
         this.b.a(this.C, this.D, this.V, this.P);
         if (this.a != null) {
            this.T.setScale(this.k, this.l);
            this.T.postConcat(this.V);
         } else {
            this.T.set(this.R);
            this.T.postConcat(this.S);
         }
      } else {
         this.T.set(this.R);
         this.T.postConcat(this.S);
         if (this.a != null && !this.a.k()) {
            this.a.a(this.T, this.P);
         } else {
            this.P.set(0.0F, 0.0F, this.C, this.D);
            this.T.mapRect(this.P);
         }
      }
   }

   public float[] k() {
      float var1;
      float var2;
      float var3;
      float var4;
      label11: {
         var4 = this.C;
         var3 = this.D;
         var2 = this.getPictureWidth();
         float var5 = this.getPictureHeight();
         if (!(var2 <= 0.0F)) {
            var1 = var5;
            if (!(var5 <= 0.0F)) {
               break label11;
            }
         }

         var1 = var3;
         var2 = var4;
      }

      var2 = var4 / var2;
      var1 = var3 / var1;
      this.W[0] = 1.0F;
      this.W[1] = Math.max(var2, var1) / Math.min(var2, var1);
      this.W[2] = 1.0F / Math.min(var2, var1);
      return this.W;
   }

   public boolean l() {
      return this.c(this.ag);
   }

   public boolean m() {
      boolean var1;
      if ((this.m == null || this.m.isFinished()) && !this.G) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   @Override
   protected void onDetachedFromWindow() {
      if (this.b != null) {
         this.b.a(true);
         this.b.m();
         this.b = null;
      }

      if (this.a != null) {
         this.a.m();
         this.a = null;
      }

      this.e = null;
      if (this.d != null) {
         this.d.recycle();
         this.d = null;
      }

      super.onDetachedFromWindow();
   }

   protected void transformPictureForDrawing(Canvas canvas, RectF bounds) {
   }

   @TargetApi(14)
   @Override
   public void onDraw(Canvas var1) {
      if (this.J != null) {
         this.J.d();
      }

      boolean var5;
      if (x && var1.isHardwareAccelerated()) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         if (VERSION.SDK_INT >= 14) {
            this.i = var1.getMaximumBitmapWidth();
            this.j = var1.getMaximumBitmapHeight();
         } else {
            this.i = 2048;
            this.j = 2048;
         }
      } else {
         this.i = 1024;
         this.j = 1024;
      }

      boolean var3;
      if ((this.G || !this.I) && this.K != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      float var9;
      if (var3) {
         var9 = this.a();
         boolean var4;
         if (Math.abs(var9) > this.ac) {
            var4 = true;
         } else {
            var4 = false;
         }

         var3 &= var4;
         if (var3) {
            if (var9 > 0.0F) {
               var9 = var9 - this.C - this.ac;
            } else {
               var9 = this.C + var9 + this.ac;
            }
         } else {
            var9 = 0.0F;
         }
      } else {
         var9 = 0.0F;
      }

      if (this.c != null && var1 != this.e) {
         Bitmap var10 = this.d;
         var1.save();
         this.transformPictureForDrawing(var1, this.P);
         var1.clipRect(this.c);
         if (var10 != null && !var10.isRecycled()) {
            var1.drawBitmap(var10, 0.0F, 0.0F, null);
         }

         var1.restore();
      } else {
         boolean var6;
         if (!this.I && !this.G) {
            var6 = false;
         } else {
            var6 = true;
         }

         boolean var7;
         if (!var5 && (var6 || this.F)) {
            var7 = false;
         } else {
            var7 = true;
         }

         this.M.setDither(var7);
         this.M.setFilterBitmap(var7);
         var1.save();
         this.transformPictureForDrawing(var1, this.P);
         var1.clipRect(this.P);
         this.a(var1, var5, var6);
         var1.restore();
         if (var3 && this.A != 0 && this.K.a(this.A)) {
            Picture var8;
            if (this.K != null && this.A != 0) {
               var8 = this.K.a(this.A, false);
            } else {
               var8 = null;
            }

            if (var8 != null) {
               this.a(var1, var8, var5, var9);
               var8.m();
            }
         }
      }
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.h();
      boolean var10;
      if (this.C == var1 && this.D == var2) {
         var10 = false;
      } else {
         var10 = true;
      }

      this.C = var1;
      this.D = var2;
      this.E = Math.min(var1, var2) / 2;
      this.Q.set(0.0F, 0.0F, var1, var2);
      if (var1 > 0 && var2 > 0) {
         int var7 = QuickApp.f;
         int var8 = var7 % 8;
         var4 = var7;
         if (var8 != 0) {
            var4 = var7 + (8 - var8);
         }

         if (this.d != null && this.d.getWidth() < var4) {
            this.d.recycle();
            this.d = null;
         }

         if (this.d == null) {
            this.d = BitmapUtils.a(var4, var4, Config.RGB_565);
         }

         Canvas var9;
         if (this.d != null) {
            var9 = new Canvas(this.d);
         } else {
            var9 = null;
         }

         this.e = var9;
      }

      if (var10) {
         float var6 = this.P.width();
         this.B = 0;
         this.S.reset();
         this.b(false);
         float var5 = this.P.width();
         if (var5 != var6) {
            if (this.a != null) {
               var5 = var6 / var5;
            } else {
               var5 = 1.0F;
            }

            this.a(Math.max(var5, 1.0F) / this.getTransScale(), var1 / 2, var2 / 2);
            this.c(0.0F);
         }
      }
   }

   @Override
   protected void onWindowVisibilityChanged(int var1) {
      super.onWindowVisibilityChanged(var1);
       if (this.b != null && this.b.d == 4) {
         if (var1 == VISIBLE && this.c == null && !this.G) this.b.a(this, this.N);
         else this.b.a(true);
      }
      this.invalidate();
   }

   public void setListener(PictureNavigationListener var1) {
      this.K = var1;
   }

   public void setPattern(boolean var1) {
      Paint var3 = this.N;
      Shader var2;
      if (var1) {
         var2 = al;
      } else {
         var2 = null;
      }

      var3.setShader(var2);
      this.invalidate();
   }

   public void setPictureUri(Uri var1) {
      Uri var2 = var1;
      if (var1 == null) {
         var2 = Uri.EMPTY;
      }

      if (this.a != null) {
         this.a.i = var2;
      }

      if (this.b != null) {
         this.b.i = var2;
      }
   }

   public void setTransValues(float[] var1) {
      if (var1 != null && var1.length >= 9) {
         this.S.setValues(var1);
         this.j();
         this.invalidate();
      }
   }
}
