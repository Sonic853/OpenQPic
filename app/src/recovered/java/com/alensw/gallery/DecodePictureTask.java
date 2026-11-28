package com.alensw.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.concurrent.CancellableCallable;
import com.alensw.image.AnimatedPng;
import com.alensw.image.BitmapDecodeOptions;
import com.alensw.image.BitmapUtils;
import com.alensw.image.ExifData;
import com.alensw.image.GifMovie;
import com.alensw.image.Picture;
import com.alensw.image.PictureMetadata;
import com.alensw.image.SystemAnimation;
import com.alensw.bean.CommonFile;
import com.alensw.image.TiledPicture;
import com.alensw.io.FileOpenCancelledException;
import com.alensw.io.OpenFileHandle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class DecodePictureTask implements CancellableCallable {
   public final CommonFile a;
   public final int b;
   final PictureLoader c;
   private final Handler d;
   private final Uri e;
   private final String f;
   private boolean g;
   private long h;
   private BitmapDecodeOptions i;
   private ExifData j;
   private final AtomicReference k;
   private final AtomicInteger l;
   private boolean m;

   public DecodePictureTask(PictureLoader var1, CommonFile var2, int var3, Handler var4) {
      this.c = var1;
      this.k = new AtomicReference();
      this.l = new AtomicInteger(1);
      this.m = false;
      this.a = var2;
      this.b = var3;
      this.d = var4;
      String var5 = this.a.j();
      this.e = this.a.i();
      if (var5 == null) {
         if (OpenFileHandle.a(this.e)) {
            var5 = this.e.getPath();
         } else {
            var5 = null;
         }
      }

      this.f = var5;
   }

   private Picture a(OpenFileHandle var1, Uri var2) {
        Bitmap bitmapA;
        boolean z;
        Bitmap bitmapA2;
        boolean z2;
        int i;
        Picture srcType0202 = (Picture) PictureLoader.f(this.c).b(var2);
        if (srcType0202 != null) {
            return srcType0202.c();
        }
        PictureLoader.f(this.c).b(2);
        PictureLoader.g(this.c);
        PictureLoader.h(this.c);
        char cH = this.a.h();
        boolean z3 = cH == 'V';
        String str = this.f;
        BitmapDecodeOptions srcType0193 = this.i;
        String str2 = srcType0193.outMimeType;
        boolean animated = mayAnimateWebp(var1, str2)
                || (AnimatedPng.isPng(str2) && !srcType0193.mCancel && AnimatedPng.mayAnimate(var1));
        int i2 = srcType0193.c;
        int i3 = srcType0193.outWidth;
        int i4 = srcType0193.outHeight;
        int iMin = z3 ? 0 : Math.min(i3 * i4, (PictureLoader.i(this.c) * 2) / 3);
        Bitmap bitmapA3 = null;
        boolean z4 = false;
        if (!z3 && PictureLoader.i(this.c) <= 196608 && this.j.b() && !srcType0193.mCancel) {
            bitmapA3 = BitmapUtils.a(this.j, PictureLoader.i(this.c), srcType0193);
        }
        if (this.c.a && bitmapA3 == null) {
            boolean zA = PictureLoader.a(this.c, var2);
            if (zA || srcType0193.mCancel) {
                z4 = zA;
                bitmapA = bitmapA3;
            } else {
                z4 = zA;
                bitmapA = PictureLoader.a(this.c, var2, str, this.h, srcType0193, iMin);
            }
        } else {
            bitmapA = bitmapA3;
        }
        srcType0193.outWidth = i3;
        srcType0193.outHeight = i4;
        srcType0193.c = i2;
        srcType0193.outMimeType = str2;
        if (bitmapA != null || srcType0193.mCancel) {
            z = false;
            bitmapA2 = bitmapA;
            z2 = false;
        } else {
            z = true;
            a((Picture) this.k.get());
            if (cH == 'I') {
                bitmapA2 = BitmapUtils.a(var1, false, PictureLoader.d(this.c), srcType0193);
                z2 = srcType0193.inSampleSize > 1 || "image/gif".equals(srcType0193.outMimeType) || animated;
            } else {
                bitmapA2 = (cH == 'D' || cH == 0) ? BitmapUtils.a(var1, false, PictureLoader.d(this.c), srcType0193) : bitmapA;
                if (bitmapA2 == null) {
                    bitmapA2 = BitmapUtils.a(var1);
                }
                z2 = true;
            }
        }
        if (bitmapA2 == null) {
            return null;
        }
        Bitmap bitmapA4 = PictureLoader.a(this.c, bitmapA2, PictureLoader.i(this.c), PictureLoader.j(this.c));
        boolean z5 = z2 | (bitmapA4 != bitmapA2);
        if (z3) {
            i = 4;
        } else {
            i = (!z || z5) ? 1 : 2;
        }
        Picture srcType0203 = new Picture(bitmapA4, i, var2, srcType0193);
        srcType0203.animatedPreview = animated;
        this.c.a(srcType0203);
        a(srcType0203);
        if (this.c.a && z && z5 && !z4) {
            PictureLoader.a(this.c, var2, str, this.h);
        }
        return srcType0203;
    }

   static AtomicReference a(DecodePictureTask var0) {
      return var0.k;
   }

   private void a(Picture var1) {
      if (this.d != null && var1 != null) {
         this.d.obtainMessage(30, 0, 0, var1.c()).sendToTarget();
      }
   }

   private void a(boolean var1) {
      Picture var2;
      if (var1 && this.i.outWidth != 0 && this.i.outHeight != 0) {
         var2 = new Picture(this.a.i(), this.i);
      } else {
         var2 = null;
      }

      var2 = (Picture)this.k.getAndSet(var2);
      if (var2 != null) {
         var2.m();
      }
   }

   private static boolean mayAnimateWebp(OpenFileHandle file, String mime) {
      return Build.VERSION.SDK_INT >= 28 && "image/webp".equals(mime) && SystemAnimation.mayAnimateWebp(file);
   }

   private Picture animation(OpenFileHandle file, Uri uri) {
      if (this.i.mCancel) return null;
      String mime = this.i.outMimeType;
      if ("image/gif".equals(mime)) {
         if (Build.VERSION.SDK_INT >= 28
               && PreferenceManager.getDefaultSharedPreferences(QuickApp.a()).getBoolean("gif_system_decoder", true)) {
            Picture system = SystemAnimation.open(PictureLoader.c(this.c), file, uri, mime);
            if (system != null) return system;
         }
         return GifMovie.a(file, uri, this.d);
      }
      if (this.b != 4 && mayAnimateWebp(file, mime)) {
         return SystemAnimation.open(PictureLoader.c(this.c), file, uri, mime);
      }
      if (AnimatedPng.isPng(mime)) return AnimatedPng.open(file, uri);
      return null;
   }

   private Picture b(OpenFileHandle var1, Uri var2) {
      BitmapDecodeOptions var4 = this.i;
      Picture var3 = animation(var1, var2);

      Object var5;
      if (var3 != null) {
         var3.b.d = var4.c;
         var5 = var3;
         if (!var4.mCancel) {
            this.a(var3);
            var5 = var3;
         }
      } else if (var4.mCancel) {
         var5 = null;
      } else {
         PictureLoader.k(this.c);
         Bitmap var6 = BitmapUtils.a(var1, false, PictureLoader.l(this.c), var4);
         PictureLoader.h(this.c);
         if (var6 == null) {
            var5 = null;
         } else {
            var5 = new Picture(PictureLoader.a(this.c, var6, this.c.b, PictureLoader.m(this.c)), 1, var2, var4);
            this.a((Picture)var5);
         }
      }

      return (Picture)var5;
   }

   private Picture c(OpenFileHandle var1, Uri var2) {
        Picture srcType0202A;
        Bitmap bitmap;
        BitmapDecodeOptions srcType0193 = this.i;
        srcType0202A = animation(var1, var2);
        if (srcType0202A == null && this.g && !srcType0193.mCancel) {
            srcType0202A = TiledPicture.a(var1, var2, srcType0193.outMimeType, this.d);
        }
        if (srcType0202A != null) {
            srcType0202A.b.d = srcType0193.c;
            if (srcType0193.mCancel) {
                return srcType0202A;
            }
            a(srcType0202A);
            return srcType0202A;
        }
        if (srcType0193.mCancel) {
            return null;
        }
        PictureLoader.k(this.c);
        Bitmap bitmapA = BitmapUtils.a(var1, 0, srcType0193);
        if (bitmapA == null && !srcType0193.mCancel && srcType0193.d()) {
            srcType0193.a(false);
            bitmapA = BitmapUtils.a(var1, 3145728, srcType0193);
        }
        PictureLoader.h(this.c);
        if (bitmapA == null || !srcType0193.mCancel) {
            bitmap = bitmapA;
        } else {
            bitmapA.recycle();
            bitmap = null;
        }
        if (bitmap == null) {
            return null;
        }
        Picture srcType0202 = new Picture(bitmap, 2, var2, srcType0193);
        a(srcType0202);
        return srcType0202;
    }

   private OpenFileHandle g() {
      if (this.f != null) {
         try {
            return OpenFileHandle.a(new File(this.f), true);
         } catch (Throwable error) {
            if (error instanceof FileNotFoundException) {
               Log.w("PictureLoader", "open failed: " + this.e);
            } else {
               this.i.outWidth = this.i.outHeight = -1;
               Log.e("PictureLoader", "open failed: " + this.e, error);
               return null;
            }
         }
      }
      try {
         return OpenFileHandle.a(PictureLoader.c(this.c), this.e);
      } catch (Throwable error) {
         if (error instanceof FileOpenCancelledException) {
            Log.w("PictureLoader", "open cancelled: " + this.e);
         } else {
            this.i.outWidth = this.i.outHeight = -1;
            this.a(true);
            Log.e("PictureLoader", "open failed: " + this.e, error);
         }
         return null;
      }
   }

   private OpenFileHandle h() {
      boolean var2 = false;
      BitmapDecodeOptions var5 = this.i;
      PictureMetadata var3 = (PictureMetadata)PictureLoader.e(this.c).b(this.e);
      boolean var1;
      if (var3 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var3 != null) {
         var5.outWidth = var3.a;
         var5.outHeight = var3.b;
         var5.b = var3.c;
         var5.c = var3.d;
         var5.d = var3.i;
         var5.outMimeType = var3.o;
         this.a(true);
      } else {
         var3 = new PictureMetadata();
         var5.c = 360;
      }

      OpenFileHandle var4 = this.g();
      OpenFileHandle var6;
      if (var4 == null) {
         var6 = null;
      } else {
         this.j = new ExifData();
         this.h = this.a.e();
         if (this.h == 0L && this.f != null) {
            this.h = var4.f() / 1000L;
         }

         if (this.a.h() == 'I') {
            if (var5.c == 360 && !var5.mCancel) {
               if (this.j.b(var4, true)) {
                  this.j.a(var3, this.h * 1000L);
                  var5.outWidth = var3.a;
                  var5.outHeight = var3.b;
                  var5.b = var3.c;
                  var5.c = var3.d;
                  var5.d = var3.i;
                  var5.outMimeType = var3.o;
                  this.a(true);
               } else {
                  var5.c = 0;
               }
            }

            if ((var5.outWidth <= 0 || var5.outHeight <= 0) && !var5.mCancel) {
               if (!BitmapUtils.b(var4, var5)) {
                  var5.outHeight = -1;
                  var5.outWidth = -1;
                  this.a(true);
                  var4.h();
                  return null;
               }

               var3.a = var5.outWidth;
               var3.b = var5.outHeight;
               var3.d = 0;
               if ("image/jpeg".equals(var5.outMimeType) || "image/png".equals(var5.outMimeType)) {
                  var2 = true;
               }

               var3.i = var2;
               var3.o = var5.outMimeType;
               this.a(true);
            }
         } else {
            var5.outWidth = 512;
            var5.outHeight = 384;
            var5.c = 0;
            this.a(true);
         }

         if (!var1) {
            PictureLoader.e(this.c).a(this.e, var3);
         }

         if (this.b == 3 || this.b == 4) {
            this.g = PictureLoader.a(var3);
         }

         if (this.b == 4) {
            var5.a(true);
         }

         var6 = var4;
      }

      return var6;
   }

   @Override
   public void a() {
        synchronized (this) {
            if (this.i != null) {
                this.i.requestCancelDecode();
            }
        }
    }

   @Override
   public void b() {
      this.e();
   }

   public void c() {
      if (this.m) {
         this.a(false);
      }
   }

   @Override
   public Object call() {
      return this.f();
   }

   public int d() {
      return this.l.incrementAndGet();
   }

   public int e() {
      int var1 = this.l.decrementAndGet();
      if (var1 == 0) {
         this.c();
      }

      return var1;
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof DecodePictureTask)) {
            return false;
        }
        DecodePictureTask srcType0093 = (DecodePictureTask) var1;
        return this.b == srcType0093.b && this.d == srcType0093.d && this.a.equals(srcType0093.a);
    }

   public Void f() {
        Picture srcType0202A;
        boolean z = this.b < 3;
        if (!z || !PictureLoader.n(this.c).contains(this)) {
            d();
            if (z) {
                PictureLoader.n(this.c).add(this);
            }
            synchronized (this) {
                this.i = BitmapDecodeOptions.e();
            }
            Uri uri = this.e;
            OpenFileHandle srcType0150H = h();
            if (srcType0150H == null) {
                if (!this.i.mCancel) {
                    a((Picture) this.k.get());
                }
            } else if (!this.i.mCancel) {
                if (this.b <= 1 || this.a.h() == 'V') {
                    srcType0202A = a(srcType0150H, uri);
                } else {
                    srcType0202A = this.b == 2 ? b(srcType0150H, uri) : c(srcType0150H, uri);
                }
                if (srcType0202A != null) {
                    srcType0202A.m();
                    this.m = true;
                }
            }
            if (this.j != null) {
                this.j.a();
            }
            this.j = null;
            if (srcType0150H != null) {
                srcType0150H.h();
            }
            synchronized (this) {
                BitmapDecodeOptions.a(this.i);
                this.i = null;
            }
            if (z) {
                PictureLoader.n(this.c).remove(this);
            }
            e();
        }
        return null;
    }

   @Override
   public int hashCode() {
      return this.a.hashCode() + this.b;
   }

   @Override
   public String toString() {
      return "uri=" + this.a.a() + ", req=" + this.b;
   }
}
