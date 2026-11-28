package com.alensw.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import com.alensw.cache.ConcurrentLruCache;
import com.alensw.collection.BitmapPool;
import com.alensw.io.OpenFileHandle;
public class TiledPicture extends Picture {
   public static final boolean a;
   public static final boolean j;
   private static int w;
   private static BitmapPool x;
   private final BitmapRegionDecoder k;
   private TileDecodeTask l;
   private View m;
   private final ConcurrentLruCache n;
   private final Matrix o;
   private Paint p;
   private volatile int q;
   private int r;
   private final Rect s;
   private final RectF t;
   private final Matrix u;
   private final float[] v;

   static {
      boolean var1 = true;
      boolean var0;
      if (VERSION.SDK_INT >= 14) {
         var0 = true;
      } else {
         var0 = false;
      }

      a = var0;
      if (VERSION.SDK_INT >= 16) {
         var0 = var1;
      } else {
         var0 = false;
      }

      j = var0;
      w = 64;
   }

   @TargetApi(10)
   public TiledPicture(BitmapRegionDecoder var1, Uri var2, String var3, Handler var4) {
      super(var1.getWidth(), var1.getHeight(), 3, var2, var3);
      this.n = new TileCache(this, w);
      this.o = new Matrix();
      this.q = 1;
      this.r = 1;
      this.s = new Rect();
      this.t = new RectF();
      this.u = new Matrix();
      this.v = new float[9];
      this.k = var1;
   }

   public static int a(float var0, float var1) {
      int var2 = 0;

      while (var0 > (1 << var2 + 1) * var1 && var2 < 256) {
         var2++;
      }

      return var2;
   }

   static Rect a(TiledPicture var0) {
      return var0.s;
   }

   @TargetApi(10)
   public static TiledPicture a(OpenFileHandle var0, Uri var1, String var2, Handler var3) {
        BitmapRegionDecoder bitmapRegionDecoderNewInstance;
        BitmapRegionDecoder bitmapRegionDecoderNewInstance2;
        try {
            bitmapRegionDecoderNewInstance = BitmapRegionDecoder.newInstance(var0.b(), false);
        } catch (Throwable th) {
            Log.e("TilePicture", "load fd decoder: " + var0.g() + ", " + th);
            bitmapRegionDecoderNewInstance = null;
        }
        if (bitmapRegionDecoderNewInstance == null) {
            try {
                bitmapRegionDecoderNewInstance2 = BitmapRegionDecoder.newInstance(var0.d(), false);
            } catch (Throwable th2) {
                Log.e("TilePicture", "load fn decoder: " + var0.d() + ", " + th2);
                bitmapRegionDecoderNewInstance2 = bitmapRegionDecoderNewInstance;
            }
        } else {
            bitmapRegionDecoderNewInstance2 = bitmapRegionDecoderNewInstance;
        }
        if (bitmapRegionDecoderNewInstance2 == null) {
            return null;
        }
        return new TiledPicture(bitmapRegionDecoderNewInstance2, var1, var2, var3);
    }

   private PictureTile a(int var1, int var2) {
      int var4 = TileKey.c(var1);
      int var5 = TileKey.d(var1);
      int var3 = TileKey.a(var1);

      PictureTile var6;
      while (true) {
         if (var3 > var2) {
            var6 = null;
            break;
         }

         var6 = this.a(var1);
         if (var6 != null) {
            break;
         }

         var1 = TileKey.a + ++var3;
         var1 = TileKey.a(var4 >> var1 << var1, var5 >> var1 << var1, var3);
      }

      return var6;
   }

   public static void a(Context var0, int var1) {
      if (var1 >= 1440) {
         TileKey.a = 9;
      } else if (var1 >= 480) {
         TileKey.a = 8;
      } else {
         TileKey.a = 7;
      }

      TileKey.b = 1 << TileKey.a;
      DisplayMetrics var3 = var0.getResources().getDisplayMetrics();
      float var2 = var3.widthPixels;
      var1 = Math.round(var3.heightPixels * var2 / TileKey.b / TileKey.b);
      w = BitmapUtils.d / (TileKey.b * TileKey.b) - var1;
      if (!j) {
         var1 = 1;
      }

      x = new BitmapPool(var1, TileKey.b, TileKey.b, Config.ARGB_8888);
   }

   @TargetApi(12)
   private boolean a(int var1, Rect var2, BitmapDecodeOptions var3) {
      var3.inSampleSize = TileKey.b(var1);
      Bitmap var5 = this.a(var2, var3);
      if (var5 == null) {
         if (var3.mCancel) {
            return false;
         }

         if (var3.c()) {
            Log.e("TilePicture", "decoder tile: " + TileKey.e(var1));
         }
      }

      PictureTile var6;
      if (var5 != null) {
         var6 = new PictureTile(this, var1, var5);
      } else {
         var6 = new PictureTile(this, var1, TileKey.b, TileKey.b);
      }

      this.n.a(var1, var6);
      return true;
   }

   static boolean a(TiledPicture var0, int var1, Rect var2, BitmapDecodeOptions var3) {
      return var0.a(var1, var2, var3);
   }

   static int b(TiledPicture var0) {
      return var0.q;
   }

   @TargetApi(10)
   private Bitmap b(Rect param1, BitmapDecodeOptions param2) {
        try {
            param2.inPreferredConfig = Config.ARGB_8888;
            synchronized (this.k) {
                if (this.k.isRecycled()) {
                    return null;
                }
                return this.k.decodeRegion(param1, param2);
            }
        } catch (Throwable th) {
            Log.e("TilePicture", "decode region: " + param1 + ", scale=" + param2.inSampleSize + ", " + th);
            return null;
        }
    }

   public static void b(Bitmap var0) {
      if (j) {
         x.b(var0);
      } else if (var0 != null) {
         var0.recycle();
      }
   }

   private Bitmap c(Rect var1, BitmapDecodeOptions var2) {
      boolean var4 = true;
      boolean var3 = false;
      Bitmap var6 = o();
      Bitmap var8;
      if (var6 == null) {
         Log.e("TilePicture", "create bitmap out of memory");
         var8 = null;
      } else {
         if (var1.right > this.b.a) {
            var1.right = this.b.a;
            var3 = true;
         }

         if (var1.bottom > this.b.b) {
            var1.bottom = this.b.b;
            var3 = var4;
         }

         if (var3) {
            try {
               var6.eraseColor(0);
            } catch (Throwable var7) {
            }
         }

         var2.inBitmap = var6;
         Bitmap var5 = this.b(var1, var2);
         var2.inBitmap = null;
         var8 = var5;
         if (var5 != var6) {
            b(var6);
            var8 = var5;
            if (var5 != null) {
               var8 = BitmapUtils.a(var5);
               Log.w("TilePicture", "decoder create bitmap: " + var1);
            }
         }
      }

      return var8;
   }

   static View c(TiledPicture var0) {
      return var0.m;
   }

   private Bitmap d(Rect var1, BitmapDecodeOptions var2) {
      if (var1.right > this.b.a) {
         var1.right = this.b.a;
      }

      if (var1.bottom > this.b.b) {
         var1.bottom = this.b.b;
      }

      Bitmap var3 = this.b(var1, var2);
      Bitmap var4;
      if (var3 != null) {
         var4 = BitmapUtils.a(var3);
      } else {
         var4 = null;
      }

      return var4;
   }

   public static Bitmap o() {
      return x.a(TileKey.b, TileKey.b, Config.ARGB_8888);
   }

   public Bitmap a(Rect var1, BitmapDecodeOptions var2) {
      Bitmap var3;
      if (j) {
         var3 = this.c(var1, var2);
      } else {
         var3 = this.d(var1, var2);
      }

      return var3;
   }

   public PictureTile a(int var1) {
      PictureTile var2 = (PictureTile)this.n.b(Integer.valueOf(var1));
      if (var2 != null) {
         var2 = (PictureTile)var2.l();
      } else {
         var2 = null;
      }

      return var2;
   }

   @Override
   protected void a() {
        super.a();
        a(true);
        synchronized (this.k) {
            this.k.recycle();
        }
    }

   @Override
   public void a(int param1, int param2, Matrix param3, RectF param4) {
        RectF rectF2 = this.t;
        rectF2.set(0.0f, 0.0f, param1, param2);
        if (!rectF2.intersect(param4)) {
            synchronized (this.s) {
                this.s.setEmpty();
            }
            return;
        }
        param3.getValues(this.v);
        int iA = a(1.0f / Picture.a(this.v), 0.85f);
        if (this.r < iA) {
            this.r = iA;
        }
        param3.invert(this.u);
        this.u.mapRect(rectF2);
        int i3 = TileKey.a + iA;
        int iMax = (Math.max((int) Math.floor(rectF2.left), 0) >> i3) << i3;
        int iMax2 = (Math.max((int) Math.floor(rectF2.top), 0) >> i3) << i3;
        int iMin = Math.min((int) Math.ceil(rectF2.right), this.b.a);
        int iMin2 = Math.min((int) Math.ceil(rectF2.bottom), this.b.b);
        synchronized (this.s) {
            this.q = iA;
            this.s.left = iMax;
            this.s.top = iMax2;
            this.s.right = iMin;
            this.s.bottom = iMin2;
        }
    }

   @Override
   public void a(Canvas var1, Matrix var2, Picture var3, Matrix var4, Paint var5, boolean var6) {
      Paint var22 = this.p;
      int var7;
      if (var3 != null) {
         var7 = var3.b.c;
      } else {
         var7 = 3;
      }

      boolean var8;
      if (var7 != 3 && var7 != 1 && var22 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      if (var3 != null && var3.j()) {
         if (var8 && var3.i()) {
            com.alensw.compat.LegacyPlatform.save(var1, 1);
            var1.concat(var4);
            var1.drawRect(0.0F, 0.0F, var3.e(), var3.f(), var22);
            var1.restore();
         }

         var1.drawBitmap(var3.d(), var4, var5);
         int var17 = this.q;
         int var13 = Math.min(var17 + 1, this.r);
         int var15 = TileKey.b << var17;
         TileDecodeTask var26 = this.l;
         Matrix var25 = this.o;
         int var12 = this.s.left;
         int var9 = this.s.top;
         int var14 = this.s.right;
         int var16 = this.s.bottom;
         if (!var6 && this.m != null) {
            var7 = Math.max(var14 - var12, var16 - var9) >> TileKey.a + var17;
         } else {
            var7 = 1;
         }

         while (var9 < var16) {
            for (int var10 = var12; var10 < var14; var10 += var15) {
               int var18 = TileKey.a(var10, var9, var17);
               PictureTile var23 = this.a(var18, var13);
               int var11;
               if (var23 != null) {
                  var11 = var23.c;
               } else {
                  var11 = -1;
               }

               if (var23 != null) {
                  Bitmap var24 = var23.d();
                  if (var24 != null && !var24.isRecycled()) {
                     int var21 = TileKey.b(var11);
                     int var20 = TileKey.c(var11);
                     int var19 = TileKey.d(var11);
                     var25.setScale(var21, var21);
                     var25.postTranslate(var20, var19);
                     var25.postConcat(var2);
                     if (var8 && var23.a) {
                        com.alensw.compat.LegacyPlatform.save(var1, 1);
                        var1.concat(var25);
                        var1.drawRect(0.0F, 0.0F, Math.min(var14 - var20, TileKey.b), Math.min(var16 - var19, TileKey.b), var22);
                        var1.drawBitmap(var24, 0.0F, 0.0F, var5);
                        var1.restore();
                     } else {
                        var1.drawBitmap(var24, var25, var5);
                     }
                  }

                  var23.m();
               }

               if (var7 > 0 && var11 != var18 && var26 != null && var26.a(var18)) {
                  var7--;
               }
            }

            var9 += var15;
         }
      } else {
         Log.w("TilePicture", "draw without preview: " + this.i);
      }
   }

   @Override
   public void a(boolean param1) {
        synchronized (this.s) {
            this.s.setEmpty();
        }
        this.m = null;
        if (this.l != null) {
            this.l.a();
            this.l = null;
        }
        if (param1) {
            this.n.c();
        }
    }

   @Override
   public boolean a(View var1, Paint var2) {
      this.m = var1;
      this.p = var2;
      if (this.l != null) {
         this.l.a();
      }

      this.l = new TileDecodeTask(this, null);
      c.a(this.l);
      return true;
   }
}
