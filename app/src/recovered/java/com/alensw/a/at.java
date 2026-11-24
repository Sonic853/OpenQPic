package com.alensw.a;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.alensw.bean.CommonFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class at implements com.alensw.b.k.k {
   public final CommonFile a;
   public final int b;
   final com.alensw.a.ao c;
   private final Handler d;
   private final Uri e;
   private final String f;
   private boolean g;
   private long h;
   private com.alensw.b.h.a i;
   private com.alensw.b.h.c j;
   private final AtomicReference k;
   private final AtomicInteger l;
   private boolean m;

   public at(com.alensw.a.ao var1, CommonFile var2, int var3, Handler var4) {
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
         if (com.alensw.b.c.f.a(this.e)) {
            var5 = this.e.getPath();
         } else {
            var5 = null;
         }
      }

      this.f = var5;
   }

   private com.alensw.b.h.j a(com.alensw.b.c.f var1, Uri var2) {
        Bitmap bitmapA;
        boolean z;
        Bitmap bitmapA2;
        boolean z2;
        int i;
        com.alensw.b.h.j srcType0202 = (com.alensw.b.h.j) com.alensw.a.ao.f(this.c).b(var2);
        if (srcType0202 != null) {
            return srcType0202.c();
        }
        com.alensw.a.ao.f(this.c).b(2);
        com.alensw.a.ao.g(this.c);
        com.alensw.a.ao.h(this.c);
        char cH = this.a.h();
        boolean z3 = cH == 'V';
        String str = this.f;
        com.alensw.b.h.a srcType0193 = this.i;
        String str2 = srcType0193.outMimeType;
        int i2 = srcType0193.c;
        int i3 = srcType0193.outWidth;
        int i4 = srcType0193.outHeight;
        int iMin = z3 ? 0 : Math.min(i3 * i4, (com.alensw.a.ao.i(this.c) * 2) / 3);
        Bitmap bitmapA3 = null;
        boolean z4 = false;
        if (!z3 && com.alensw.a.ao.i(this.c) <= 196608 && this.j.b() && !srcType0193.mCancel) {
            bitmapA3 = com.alensw.b.h.b.a(this.j, com.alensw.a.ao.i(this.c), srcType0193);
        }
        if (this.c.a && bitmapA3 == null) {
            boolean zA = com.alensw.a.ao.a(this.c, var2);
            if (zA || srcType0193.mCancel) {
                z4 = zA;
                bitmapA = bitmapA3;
            } else {
                z4 = zA;
                bitmapA = com.alensw.a.ao.a(this.c, var2, str, this.h, srcType0193, iMin);
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
            a((com.alensw.b.h.j) this.k.get());
            if (cH == 'I') {
                bitmapA2 = com.alensw.b.h.b.a(var1, false, com.alensw.a.ao.d(this.c), srcType0193);
                z2 = srcType0193.inSampleSize > 1 || "image/gif".equals(srcType0193.outMimeType);
            } else {
                bitmapA2 = (cH == 'D' || cH == 0) ? com.alensw.b.h.b.a(var1, false, com.alensw.a.ao.d(this.c), srcType0193) : bitmapA;
                if (bitmapA2 == null) {
                    bitmapA2 = com.alensw.b.h.b.a(var1);
                }
                z2 = true;
            }
        }
        if (bitmapA2 == null) {
            return null;
        }
        Bitmap bitmapA4 = com.alensw.a.ao.a(this.c, bitmapA2, com.alensw.a.ao.i(this.c), com.alensw.a.ao.j(this.c));
        boolean z5 = z2 | (bitmapA4 != bitmapA2);
        if (z3) {
            i = 4;
        } else {
            i = (!z || z5) ? 1 : 2;
        }
        com.alensw.b.h.j srcType0203 = new com.alensw.b.h.j(bitmapA4, i, var2, srcType0193);
        this.c.a(srcType0203);
        a(srcType0203);
        if (this.c.a && z && z5 && !z4) {
            com.alensw.a.ao.a(this.c, var2, str, this.h);
        }
        return srcType0203;
    }

   static AtomicReference a(com.alensw.a.at var0) {
      return var0.k;
   }

   private void a(com.alensw.b.h.j var1) {
      if (this.d != null && var1 != null) {
         this.d.obtainMessage(30, 0, 0, var1.c()).sendToTarget();
      }
   }

   private void a(boolean var1) {
      com.alensw.b.h.j var2;
      if (var1 && this.i.outWidth != 0 && this.i.outHeight != 0) {
         var2 = new com.alensw.b.h.j(this.a.i(), this.i);
      } else {
         var2 = null;
      }

      var2 = (com.alensw.b.h.j)this.k.getAndSet(var2);
      if (var2 != null) {
         var2.m();
      }
   }

   private com.alensw.b.h.j b(com.alensw.b.c.f var1, Uri var2) {
      com.alensw.b.h.a var4 = this.i;
      com.alensw.b.h.j var3;
      if ("image/gif".equals(var4.outMimeType) && !var4.mCancel) {
         var3 = com.alensw.b.h.d.a(var1, var2, this.d);
      } else {
         var3 = null;
      }

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
         com.alensw.a.ao.k(this.c);
         Bitmap var6 = com.alensw.b.h.b.a(var1, false, com.alensw.a.ao.l(this.c), var4);
         com.alensw.a.ao.h(this.c);
         if (var6 == null) {
            var5 = null;
         } else {
            var5 = new com.alensw.b.h.j(com.alensw.a.ao.a(this.c, var6, this.c.b, com.alensw.a.ao.m(this.c)), 1, var2, var4);
            this.a((com.alensw.b.h.j)var5);
         }
      }

      return (com.alensw.b.h.j)var5;
   }

   private com.alensw.b.h.j c(com.alensw.b.c.f var1, Uri var2) {
        com.alensw.b.h.j srcType0202A;
        Bitmap bitmap;
        com.alensw.b.h.a srcType0193 = this.i;
        if (!this.g || srcType0193.mCancel) {
            srcType0202A = (!"image/gif".equals(srcType0193.outMimeType) || srcType0193.mCancel) ? null : com.alensw.b.h.d.a(var1, var2, this.d);
        } else {
            srcType0202A = com.alensw.b.h.n.a(var1, var2, srcType0193.outMimeType, this.d);
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
        com.alensw.a.ao.k(this.c);
        Bitmap bitmapA = com.alensw.b.h.b.a(var1, 0, srcType0193);
        if (bitmapA == null && !srcType0193.mCancel && srcType0193.d()) {
            srcType0193.a(false);
            bitmapA = com.alensw.b.h.b.a(var1, 3145728, srcType0193);
        }
        com.alensw.a.ao.h(this.c);
        if (bitmapA == null || !srcType0193.mCancel) {
            bitmap = bitmapA;
        } else {
            bitmapA.recycle();
            bitmap = null;
        }
        if (bitmap == null) {
            return null;
        }
        com.alensw.b.h.j srcType0202 = new com.alensw.b.h.j(bitmap, 2, var2, srcType0193);
        a(srcType0202);
        return srcType0202;
    }

   private com.alensw.b.c.f g() {
      if (this.f != null) {
         try {
            return com.alensw.b.c.f.a(new File(this.f), true);
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
         return com.alensw.b.c.f.a(com.alensw.a.ao.c(this.c), this.e);
      } catch (Throwable error) {
         if (error instanceof com.alensw.b.e.a) {
            Log.w("PictureLoader", "open cancelled: " + this.e);
         } else {
            this.i.outWidth = this.i.outHeight = -1;
            this.a(true);
            Log.e("PictureLoader", "open failed: " + this.e, error);
         }
         return null;
      }
   }

   private com.alensw.b.c.f h() {
      boolean var2 = false;
      com.alensw.b.h.a var5 = this.i;
      com.alensw.b.h.k var3 = (com.alensw.b.h.k)com.alensw.a.ao.e(this.c).b(this.e);
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
         var3 = new com.alensw.b.h.k();
         var5.c = 360;
      }

      com.alensw.b.c.f var4 = this.g();
      com.alensw.b.c.f var6;
      if (var4 == null) {
         var6 = null;
      } else {
         this.j = new com.alensw.b.h.c();
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
               if (!com.alensw.b.h.b.b(var4, var5)) {
                  var5.outHeight = -1;
                  var5.outWidth = -1;
                  this.a(true);
                  Object var7 = null;
                  return (com.alensw.b.c.f)var7;
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
            com.alensw.a.ao.e(this.c).a(this.e, var3);
         }

         if (this.b == 3 || this.b == 4) {
            this.g = com.alensw.a.ao.a(var3);
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
        if (!(var1 instanceof com.alensw.a.at)) {
            return false;
        }
        com.alensw.a.at srcType0093 = (com.alensw.a.at) var1;
        return this.b == srcType0093.b && this.d == srcType0093.d && this.a.equals(srcType0093.a);
    }

   public Void f() {
        com.alensw.b.h.j srcType0202A;
        boolean z = this.b < 3;
        if (!z || !com.alensw.a.ao.n(this.c).contains(this)) {
            d();
            if (z) {
                com.alensw.a.ao.n(this.c).add(this);
            }
            synchronized (this) {
                this.i = com.alensw.b.h.a.e();
            }
            Uri uri = this.e;
            com.alensw.b.c.f srcType0150H = h();
            if (srcType0150H == null) {
                if (!this.i.mCancel) {
                    a((com.alensw.b.h.j) this.k.get());
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
                com.alensw.b.h.a.a(this.i);
                this.i = null;
            }
            if (z) {
                com.alensw.a.ao.n(this.c).remove(this);
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
