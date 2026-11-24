package com.alensw.b.h;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import com.alensw.jni.JniUtils;

public class d extends com.alensw.b.h.j {
   public static final boolean a;
   private int j;
   private int k;
   private int l;
   private int m;
   private int n = -1;
   private int o;
   private int[] p;
   private int[] q;
   private Handler r;
   private com.alensw.b.h.g s;
   private View t;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 8 || VERSION.SDK_INT >= 11 && VERSION.SDK_INT <= 13) {
         var0 = false;
      } else {
         var0 = true;
      }

      a = var0;
   }

   public d(int var1, Uri var2, Handler var3, int var4, int var5, int[] var6) {
      super(var4, var5, 4, var2, "image/gif");
      this.o = JniUtils.gifGetImageWidth(var1) / var4;
      this.p = var6;
      this.a(var1, var3);
   }

   public d(int var1, Uri var2, Handler var3, Bitmap var4) {
      super(var4, 4);
      super.h = "image/gif";
      super.i = var2;
      this.a(var1, var3);
   }

   private int a(int param1, int param2, int param3) {
        int iGifDecodeFrame = 0;
        synchronized (this) {
            if (this.j != 0 && param2 != 0 && param3 == this.j) {
                iGifDecodeFrame = JniUtils.gifDecodeFrame(this.j, param1, param2);
            }
        }
        return iGifDecodeFrame;
    }

   static int a(com.alensw.b.h.d var0, int var1) {
      var0.n = var1;
      return var1;
   }

   static View a(com.alensw.b.h.d var0) {
      return var0.t;
   }

   public static com.alensw.b.h.d a(com.alensw.b.c.f var0, Uri var1, Handler var2) {
        int iGifOpenFD = 0;
        try {
            iGifOpenFD = JniUtils.gifOpenFD(var0.c(), a);
            if (iGifOpenFD == 0) {
                throw new RuntimeException("load error");
            }
            if (JniUtils.gifGetFrameCount(iGifOpenFD) <= 1) {
                throw new RuntimeException("not animated");
            }
            int iGifGetImageWidth = JniUtils.gifGetImageWidth(iGifOpenFD);
            int iGifGetImageHeight = JniUtils.gifGetImageHeight(iGifOpenFD);
            int i = iGifGetImageWidth * iGifGetImageHeight;
            while (iGifGetImageWidth > 0 && iGifGetImageHeight > 0) {
                if (i <= com.alensw.b.h.b.d) {
                    if (a) {
                        Bitmap bitmapA = com.alensw.b.h.b.a(iGifGetImageWidth, iGifGetImageHeight, Config.ARGB_8888);
                        if (bitmapA != null) {
                            return new com.alensw.b.h.d(iGifOpenFD, var1, var2, bitmapA);
                        }
                    } else {
                        int[] iArrA = a(i);
                        if (iArrA != null) {
                            return new com.alensw.b.h.d(iGifOpenFD, var1, var2, iGifGetImageWidth, iGifGetImageHeight, iArrA);
                        }
                    }
                }
                iGifGetImageWidth >>= 1;
                iGifGetImageHeight >>= 1;
                i >>= 2;
            }
            throw new RuntimeException("out of memory");
        } catch (Throwable th) {
            Log.e("GifMovie", "load GIF: ", th);
            if (iGifOpenFD != 0) {
                JniUtils.gifClose(iGifOpenFD);
            }
            return null;
        }
    }

   private void a(int var1, Handler var2) {
      this.l = JniUtils.gifGetDuration(var1);
      this.m = JniUtils.gifGetFrameCount(var1);
      int var4 = JniUtils.gifGetImageWidth(var1);
      int var5 = JniUtils.gifGetImageHeight(var1);
      this.q = new int[2];

      for (int var3 = 0; var3 < 2; var3++) {
         int var6 = JniUtils.gifAllocBuffer(var5 * var4);
         if (var6 == 0) {
            throw new RuntimeException("alloc buffer failed");
         }

         this.q[var3] = var6;
      }

      this.j = var1;
      this.r = var2;
   }

   static boolean a(com.alensw.b.h.d var0, int var1, int var2, int var3) {
      return var0.b(var1, var2, var3);
   }

   private static int[] a(int var0) {
      int[] var1;
      try {
         var1 = new int[var0];
      } catch (Throwable var2) {
         var1 = null;
      }

      return var1;
   }

   static int b(com.alensw.b.h.d var0, int var1) {
      var0.k = var1;
      return var1;
   }

   static int b(com.alensw.b.h.d var0, int var1, int var2, int var3) {
      return var0.a(var1, var2, var3);
   }

   static com.alensw.b.h.g b(com.alensw.b.h.d var0) {
      return var0.s;
   }

   private boolean b(int param1, int param2, int param3) {
        boolean zGifDrawFrame2 = false;
        synchronized (this) {
            try {
                if (a) {
                    if (this.j != 0 && param2 != 0 && param3 == this.j && j()) {
                        zGifDrawFrame2 = JniUtils.gifDrawFrame(this.j, param1, param2, d());
                    }
                } else if (this.j != 0 && param2 != 0 && param3 == this.j && this.p != null) {
                    zGifDrawFrame2 = JniUtils.gifDrawFrame2(this.j, param1, param2, this.p, this.o);
                }
            } catch (Throwable th) {
                Log.e("GifMovie", "draw frame: ", th);
            }
        }
        return zGifDrawFrame2;
    }

   static int c(com.alensw.b.h.d var0) {
      return var0.k;
   }

   static int d(com.alensw.b.h.d var0) {
      int var1 = var0.k + 1;
      var0.k = var1;
      return var1;
   }

   static int e(com.alensw.b.h.d var0) {
      return var0.m;
   }

   static Handler f(com.alensw.b.h.d var0) {
      return var0.r;
   }

   @Override
   protected void a() {
        super.a();
        a(false);
        synchronized (this) {
            for (int i = 0; i < 2; i++) {
                if (this.q[i] != 0) {
                    JniUtils.gifFreeBuffer(this.q[i]);
                    this.q[i] = 0;
                }
            }
            if (this.j != 0) {
                JniUtils.gifClose(this.j);
                this.j = 0;
            }
        }
        this.m = 0;
        this.n = -1;
        this.p = null;
    }

   @Override
   public void a(Canvas var1, Matrix var2, com.alensw.b.h.j var3, Matrix var4, Paint var5, boolean var6) {
      if (this.n != -1) {
         if (a && this.j()) {
            var1.drawBitmap(this.d(), var2, var5);
            return;
         }

         if (this.p != null) {
            try {
               com.alensw.compat.LegacyPlatform.save(var1, 1);
               var1.concat(var2);
               var1.drawBitmap(this.p, 0, this.b.a, 0, 0, this.b.a, this.b.b, false, var5);
               var1.restore();
               return;
            } catch (Throwable var7) {
               Log.e("GifMovie", "draw: " + var7);
            }
         }
      }

      if (var3 != null && var3.j()) {
         var1.drawBitmap(var3.d(), var4, var5);
      }
   }

   @Override
   public void a(boolean var1) {
      this.t = null;
      if (this.s != null) {
         this.s.a();
         this.s = null;
      }
   }

   @Override
   public boolean a(View var1, Paint var2) {
      boolean var6 = false;
      int var3 = 0;
      if (this.j != 0) {
         this.t = var1;
         JniUtils.gifSetBkColor(this.j, var2.getColor());
         if (this.s != null) {
            this.s.a();
         }

         this.s = new com.alensw.b.h.g(this, null);
         c.a(this.s);
         int[] var7 = this.q;

         for (int var4 = var7.length; var3 < var4; var3++) {
            int var5 = var7[var3];
            this.s.a(new com.alensw.b.h.f(this, var5, this.j));
         }

         var6 = true;
      }

      return var6;
   }

   @Override
   public long b() {
      return this.l;
   }
}
