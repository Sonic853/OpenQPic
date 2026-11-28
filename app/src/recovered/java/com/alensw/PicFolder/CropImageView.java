package com.alensw.PicFolder;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.image.BitmapUtils;
import com.alensw.image.Picture;
import com.alensw.ui.view.PictureGestureDetector;
import com.alensw.ui.view.PictureGestureListener;
import com.alensw.ui.view.ViewFrameAnimator;
import com.alensw.ui.view.ZoomablePictureView;
import java.io.File;

@SuppressLint("Instantiatable")
class CropImageView extends ZoomablePictureView {
   protected float a;
   private boolean am;
   private RectF an;
   private final PictureGestureListener ao;
   protected float b;
   protected float c = 1.0F;
   protected boolean d = true;
   protected boolean e = true;
   protected boolean f;
   protected boolean g;
   protected boolean h;
   protected boolean i;
   protected boolean j;
   protected boolean k;
   protected String l;
   protected Uri m;
   protected int n;
   protected int o;
   protected long p;
   protected Bitmap q;
   protected CompressFormat r;
   protected Handler s;
   protected PictureGestureDetector t;
   protected final CropOverlay u = new CropOverlay(this);
   protected final RectF v = this.Q;
   protected final Rect w = new Rect();

   public CropImageView(Context var1) {
      this(var1, null);
   }

   public CropImageView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.an = new RectF();
      this.ao = new CropGestures(this);
   }

   static float a(CropImageView var0) {
      return var0.ai;
   }

   static ViewFrameAnimator a(CropImageView var0, ViewFrameAnimator var1) {
      var0.J = var1;
      return var1;
   }

   static void a(CropImageView var0, float var1, float var2, float var3) {
      var0.d(var1, var2, var3);
   }

   static boolean a(CropImageView var0, boolean var1) {
      var0.I = var1;
      return var1;
   }

   static RectF b(CropImageView var0) {
      return var0.P;
   }

   static boolean b(CropImageView var0, boolean var1) {
      var0.I = var1;
      return var1;
   }

   static RectF c(CropImageView var0) {
      return var0.P;
   }

   static int d(CropImageView var0) {
      return var0.C;
   }

   private void d(float var1, float var2, float var3) {
      RectF var4 = this.v;
      var2 -= (var2 - var4.left) * var1;
      var3 -= (var3 - var4.top) * var1;
      var4.set(var2, var3, var4.width() * var1 + var2, var4.height() * var1 + var3);
   }

   static int e(CropImageView var0) {
      return var0.D;
   }

   static float f(CropImageView var0) {
      return var0.ag;
   }

   static RectF g(CropImageView var0) {
      return var0.P;
   }

   static RectF h(CropImageView var0) {
      return var0.P;
   }

   static int i(CropImageView var0) {
      return var0.C;
   }

   static int j(CropImageView var0) {
      return var0.D;
   }

   static float k(CropImageView var0) {
      return var0.ai;
   }

   static float l(CropImageView var0) {
      return var0.ai;
   }

   static float m(CropImageView var0) {
      return var0.ai;
   }

   static float n(CropImageView var0) {
      return var0.ai;
   }

   static int o(CropImageView var0) {
      return var0.C;
   }

   static int p(CropImageView var0) {
      return var0.D;
   }

   static RectF q(CropImageView var0) {
      return var0.P;
   }

   static float r(CropImageView var0) {
      return var0.ai;
   }

   static float s(CropImageView var0) {
      return var0.ai;
   }

   static int t(CropImageView var0) {
      return var0.D;
   }

   protected void a() {
      RectF var7 = this.v;
      RectF var8 = this.P;
      float var3 = var8.width();
      float var2 = var8.height();
      float var5 = this.C;
      float var6 = this.D;
      float var4 = (float)this.getPictureWidth() / this.getPictureHeight();
      float var1;
      if (this.b != 0.0F) {
         var1 = this.b;
      } else {
         var1 = var4;
      }

      if (var4 < var1) {
         var1 = var3 / var1;
         var2 = var3;
      } else {
         var3 = var2 * var1;
         var1 = var2;
         var2 = var3;
      }

      var7.left = (var5 - var2) / 2.0F;
      var7.right = var2 + var7.left;
      var7.top = (var6 - var1) / 2.0F;
      var7.bottom = var1 + var7.top;
      if (var5 > 0.0F && var6 > 0.0F) {
         this.c();
      }
   }

   protected void a(float var1, float var2, float var3, int var4) {
      float var5 = this.getTransScale();
      if (this.J != null) {
         this.J.c();
      }

      this.J = new CropZoomAnimation(this, this, this.L, var5, var1 - var5, var2, var3, var1);
      this.J.a(var4, false);
   }

   protected void a(int var1) {
      byte var2 = 0;
      boolean var3;
      if (var1 != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.am = var3;
      Handler var4 = this.s;
      Handler var5 = this.s;
      if (!this.k) {
         var2 = 1;
      }

      var4.sendMessage(Message.obtain(var5, 1000, var1, var2));
      if (this.getPictureType() != 2) {
         if (this.am) {
            this.g();
         } else {
            this.f();
         }
      }
   }

   protected void a(RectF var1) {
      this.V.getValues(this.ab);
      float var10 = Picture.a(this.ab);
      float[] var11 = this.getTransValues();
      double var2 = Math.abs(Math.atan2(var11[3], var11[4]));
      double var4 = Math.sin(var2);
      double var8 = Math.cos(var2);
      var2 = this.getPictureWidth() * var10;
      double var6 = var10 * this.getPictureHeight();
      var4 = Math.min(var6 * var6 / (var2 * var4 + var6 * var8), var6 * var2 / (var8 * var2 + var4 * var6));
      var2 = var4 * var2 / var6;
      var1.left = (float)(this.P.centerX() - var2 / 2.0);
      var1.right = (float)(var2 + var1.left);
      var1.top = (float)(this.P.centerY() - var4 / 2.0);
      var1.bottom = (float)(var1.top + var4);
   }

   protected void a(Picture var1) {
      this.a(com.alensw.PicFolder.R.string.running_wait);
      var1.c();
      QuickApp.u.a(new com.alensw.PicFolder.FaceDetectionTask(this, var1));
   }

   public void a(boolean var1) {
      this.e = false;
      super.a(0, false);
      super.a(var1, new FinishCropFlip(this));
   }

   protected void a(boolean var1, int var2, int var3, float var4) {
      this.d = var1;
      if (var2 > 0 && var3 > 0) {
         this.b = (float)var2 / var3;
      } else if (var4 != 0.0F) {
         this.b = var4;
      } else {
         this.b = this.a;
      }

      this.n = var2;
      this.o = var3;
      if (this.e()) {
         this.S.reset();
         this.j();
         this.a();
      }
   }

   protected boolean a(int var1, int var2) {
      Bitmap var5;
      if (!this.f) {
         var5 = BitmapUtils.a(this.C, this.D, Config.ARGB_8888);
      } else {
         var5 = null;
      }

      this.q = BitmapUtils.a(var1, var2, null);
      if (this.q == null && !this.f) {
         for (int var3 = 15; var3 >= 1 && this.q == null; var3--) {
            this.q = BitmapUtils.a(var1 * var3 / 16, var2 * var3 / 16, null);
         }
      }

      if (var5 != null) {
         var5.recycle();
      }

      boolean var4;
      if (this.q != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean a(Intent intent, boolean z, Handler handler, GalleryMediaStore srcType0101) {
        Context context = getContext();
        this.s = handler;
        this.i = z;
        this.t = new PictureGestureDetector(context, this.ao, this.s);
        Bitmap bitmap = null;
        Bundle extras = intent.getExtras();
        Uri data = intent.getData();
        if (extras != null) {
            bitmap = (Bitmap) extras.getParcelable("data");
            this.g = bitmap != null || extras.getBoolean("return-data");
            if (!this.i) {
                this.m = (Uri) extras.getParcelable("output");
                if (this.m != null) {
                    try {
                        String string = extras.getString("outputFormat");
                        if (string != null) {
                            this.r = android.graphics.Bitmap.CompressFormat.valueOf(string.toUpperCase());
                        }
                    } catch (Throwable th) {
                    }
                }
            }
            int i = extras.getInt("aspectX");
            int i2 = extras.getInt("aspectY");
            if (i != 0 && i2 != 0) {
                this.a = (float) i / i2;
                this.b = this.a;
            }
            this.n = extras.getInt("outputX");
            this.o = extras.getInt("outputY");
            this.h = !extras.getBoolean("noFaceDetection");
        }
        this.f = this.n > 0 && this.o > 0;
        if (bitmap != null) {
            b(new Picture(bitmap, 2));
            return true;
        }
        if (data == null) {
            return false;
        }
        if (this.f && !a(this.n, this.o)) {
            Toast.makeText(context, "Create output bitmap failed: " + this.n + "x" + this.o, 1).show();
            return false;
        }
        String scheme = data.getScheme();
        if ("file".equals(scheme)) {
            this.l = data.getPath();
            this.j = srcType0101.a(this.l, 'I') != null;
        } else if ("content".equals(scheme)) {
            this.l = QuickApp.s.b(data);
            this.j = "media".equals(data.getAuthority());
        }
        if (this.l == null) {
            return true;
        }
        this.p = new File(this.l).lastModified();
        return true;
    }

   protected void b() {
      RectF var3 = this.v;
      float[] var4 = this.getScaleRanges();
      float var2 = Math.min(this.C / var3.width(), this.D / var3.height());
      float var1 = this.getTransScale();
      var2 = Math.max(Math.min(var2 * 0.6F * var1, var4[1]), 1.0F);
      if (!(var1 / var2 < 0.9F) && !(var1 / var2 > 1.11F)) {
         this.c();
      } else {
         this.a(var2, var3.centerX(), var3.centerY(), this.ad / 2);
      }
   }

   @Override
   public void b(int var1) {
      this.v.set(0.0F, 0.0F, this.C, this.D);
      this.e = false;
      float var2 = this.getTransRotation();
      super.a(var1 + var2, true, this.C / 2.0F, this.D / 2.0F, this.ad, new FinishCropRotation(this));
   }

   public void b(Picture var1) {
      super.a(var1, true);
      if (var1 != null && var1.d > 1) {
         this.f();
         this.a();
         if (this.h) {
            this.post(new LoadCropPreview(this));
         }
      }
   }

   public void c() {
      float var5 = this.b;
      RectF var6 = this.v;
      float var1 = var6.width();
      float var2 = var6.height();
      RectF var7 = this.P;
      float var4 = Math.min(var7.width(), (float)this.C);
      float var3 = Math.min(var7.height(), (float)this.D);
      if (var1 > var4) {
         var1 = var4;
         if (var5 != 0.0F) {
            var2 = var4 / var5;
            var1 = var4;
         }
      }

      if (var2 > var3) {
         if (var5 != 0.0F) {
            var1 = var3 * var5;
            var2 = var3;
         } else {
            var2 = var3;
         }
      }

      var7 = this.getClipRect();
      if (var6.left < var7.left) {
         var6.offset(var7.left - var6.left, 0.0F);
         var6.right = var1 + var6.left;
      } else if (var6.right > var7.right) {
         var6.offset(var7.right - var6.right, 0.0F);
         var6.left = var6.right - var1;
      } else {
         var6.inset((var6.width() - var1) / 2.0F, 0.0F);
      }

      if (var6.top < var7.top) {
         var6.offset(0.0F, var7.top - var6.top);
         var6.bottom = var2 + var6.top;
      } else if (var6.bottom > var7.bottom) {
         var6.offset(0.0F, var7.bottom - var6.bottom);
         var6.top = var6.bottom - var2;
      } else {
         var6.inset((var6.height() - var2) / 2.0F, 0.0F);
      }

      this.c(0.0F);
   }

   protected void c(int var1) {
      var1 = (var1 + 360) % 360;
      Picture var2 = this.getPicture();
      if (var2 != null) {
         var2.b.d = var1;
         var2.m();
      }

      var2 = this.getFullPicture();
      if (var2 != null) {
         var2.b.d = var1;
         var2.m();
      }
   }

   protected void d() {
      RectF var5 = this.v;
      float var1 = Math.max(0.0F, -var5.left);
      float var4 = Math.min(0.0F, this.C - var5.right);
      float var2 = Math.max(0.0F, -var5.top);
      float var3 = Math.min(0.0F, this.D - var5.bottom);
      if (var1 == 0.0F) {
         var1 = var4;
      }

      if (var2 == 0.0F) {
         var2 = var3;
      }

      if (var1 != 0.0F || var2 != 0.0F) {
         this.b(var1, var2);
         this.l();
      }
   }

   protected void d(int var1) {
      boolean var2;
      if (var1 == com.alensw.PicFolder.R.id.share) {
         var2 = true;
      } else {
         var2 = false;
      }

      Context var6 = this.getContext();
      ContentResolver var4 = var6.getContentResolver();
      Matrix var5 = new Matrix(this.T);
      Matrix var3 = new Matrix(this.V);
      if (!this.d || this.b == 0.0F) {
         RectF var7 = new RectF();
         this.a(var7);
         this.v.intersect(var7);
         this.invalidate();
      }

      this.k = true;
      this.a(com.alensw.PicFolder.R.string.running_wait);
      RectF var8 = new RectF(this.v);
      QuickApp.u.a(new RenderCropResult(this, var8, var3, var5, var2, var6, var4, var1));
   }

   @Override
   public float[] getScaleRanges() {
      float[] var3 = super.getScaleRanges();
      float[] var1 = this.k();
      RectF var2 = this.an;
      this.b(var2);
      var3[0] = Math.max(this.v.width() / var2.width(), this.v.height() / var2.height()) * var1[0];
      var3[1] = Math.max(var1[2] * 4.0F, var3[0]);
      return var3;
   }

   @Override
   protected void onDetachedFromWindow() {
      if (this.q != null) {
         this.q.recycle();
         this.q = null;
      }

      super.onDetachedFromWindow();
   }

   @Override
   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.e() && this.d && this.e) {
         this.u.a(var1);
      }
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (this.e()) {
         Intent var6 = ((CropActivity)this.getContext()).getIntent();
         float[] var5 = var6.getFloatArrayExtra("QuickPic.matrix_values");
         var6.removeExtra("QuickPic.matrix_values");
         if (var5 != null) {
            this.setTransValues(var5);
         } else {
            this.S.reset();
         }

         this.j();
         this.a();
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2;
      if (!this.am && this.t.a(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
