package com.alensw.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.UriFile;
import com.alensw.cache.ConcurrentLruCache;
import com.alensw.cache.DiskFileCache;
import com.alensw.concurrent.AsyncExecutor;
import com.alensw.image.BitmapDecodeOptions;
import com.alensw.image.BitmapUtils;
import com.alensw.image.Picture;
import com.alensw.image.PictureMetadata;
import com.alensw.image.TiledPicture;
import com.alensw.io.OpenFileHandle;
import com.alensw.io.StoragePaths;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PictureLoader {
   public boolean a;
   protected int b;
   private int c;
   private int d;
   private int e;
   private int f;
   private int g;
   private final Context h;
   private final ContentResolver i;
   private final DiskFileCache j;
   private final AsyncExecutor k;
   private final AsyncExecutor l;
   private final ConcurrentLinkedQueue m;
   private final ConcurrentLruCache n;
   private final ConcurrentLruCache o;
   private Picture p;
   private final Object q;
   private Bitmap r;
   private final ConcurrentHashMap s;

   public PictureLoader(Context var1) {
      super();
      int var2 = 320;
      this.a = true;
      this.k = new AsyncExecutor(3, 4, 8, 5);
      this.l = new AsyncExecutor(1, 1, 8, 7);
      this.m = new ConcurrentLinkedQueue();
      this.n = new ConcurrentLruCache(3);
      this.o = new PictureMemoryCache(this, 3);
      this.q = new Object();
      this.s = new ConcurrentHashMap(8);
      this.h = var1;
      this.i = var1.getContentResolver();
      this.j = new DiskFileCache();
      this.j.a(0L);
      this.b = QuickApp.f * QuickApp.g;
      int var3 = QuickApp.f;
      if (var3 > 320) {
         if (var3 <= 480) {
            var2 = 480;
         } else if (var3 <= 960) {
            var2 = 512;
         } else {
            var2 = Math.min(var3 / 2, 1024);
         }
      }

      this.c = var2;
      this.d = this.c * (this.c * 3 / 4);
      this.e = this.d * 9 / 8;
      this.f = Math.max(QuickApp.f, QuickApp.g);
      this.g = this.b * 9 / 8;
   }

   private Bitmap a(Bitmap var1, int var2, int var3) {
      Bitmap var11;
      if (var1 == null) {
         var11 = null;
      } else {
         int var10 = var1.getWidth();
         int var9 = var1.getHeight();
         int var8 = var10 * var9;
         var11 = var1;
         if (var8 > var3) {
            float var4;
            label17: {
               float var5 = (float)Math.sqrt((float)var2 / var8);
               float var7 = Math.round(var10 * var5) / 4096.0F;
               float var6 = Math.round(var9 * var5) / 4096.0F;
               if (!(var7 > 1.0F)) {
                  var4 = var5;
                  if (!(var6 > 1.0F)) {
                     break label17;
                  }
               }

               var4 = var5 / Math.max(var7, var6);
            }

            Bitmap var12 = BitmapUtils.a(var1, var4);
            var11 = var1;
            if (var12 != null) {
               var1.recycle();
               var11 = var12;
            }
         }
      }

      return var11;
   }

   private Bitmap a(Uri var1, String var2, long var3, BitmapDecodeOptions var5, int var6) {
      String var7;
      if (var2 != null) {
         var7 = "file://" + var2;
      } else {
         var7 = var1.toString();
      }

      File var8 = this.j.a(var7, previewStamp(var3));
      Bitmap var9;
      if (var8 != null && !var5.mCancel) {
         var9 = a(var8.getPath(), var5, var6);
      } else {
         var9 = null;
      }

      return var9;
   }

   static Bitmap a(PictureLoader var0, Bitmap var1, int var2, int var3) {
      return var0.a(var1, var2, var3);
   }

   static Bitmap a(PictureLoader var0, Uri var1, String var2, long var3, BitmapDecodeOptions var5, int var6) {
      return var0.a(var1, var2, var3, var5, var6);
   }

   private static Bitmap a(String path, BitmapDecodeOptions options, int minimumArea) {
      Bitmap bitmap = null;
      try {
         OpenFileHandle file = OpenFileHandle.a(new File(path), true);
         options.b();
         bitmap = BitmapFactory.decodeFileDescriptor(file.b(), null, options);
         file.h();
      } catch (Throwable error) {
         if (!(error instanceof FileNotFoundException)) Log.e("PictureLoader", "load thumbnail: " + path, error);
      }
      if (bitmap != null && minimumArea > 0 && options.outWidth * options.outHeight < minimumArea) {
         bitmap.recycle();
         return null;
      }
      return bitmap;
   }

   static DiskFileCache a(PictureLoader var0) {
      return var0.j;
   }

   private void a(Uri var1, String var2, long var3) {
      PreviewWriteState var5 = new PreviewWriteState(this, null);
      if (this.s.putIfAbsent(var1, var5) != null) {
         Log.w("PictureLoader", "saving thumbnail: " + var1);
      } else {
         SavePreviewTask var6 = new SavePreviewTask(this, var1, var5, var2, var3);
         this.l.a(var6);
      }
   }

   static void a(PictureLoader var0, Uri var1, String var2, long var3) {
      var0.a(var1, var2, var3);
   }

   static boolean a(PictureLoader var0, Uri var1) {
      return var0.d(var1);
   }

   public static boolean a(PictureMetadata var0) {
      boolean var3 = true;
      boolean var5 = true;
      boolean var4 = false;
      boolean var2;
      if (!TiledPicture.a) {
         var2 = var4;
      } else {
         var2 = var4;
         if (var0 != null) {
            int var1 = var0.a * var0.b;
            if ("image/jpeg".equals(var0.o)) {
               if (var0.i && var1 > 1228800) {
                  var2 = var5;
               } else {
                  var2 = false;
               }
            } else {
               var2 = var4;
               if ("image/png".equals(var0.o)) {
                  if (var1 > 4096000) {
                     var2 = var3;
                  } else {
                     var2 = false;
                  }
               }
            }
         }
      }

      return var2;
   }

   static boolean a(String var0, Picture var1) {
      return b(var0, var1);
   }

   static ConcurrentHashMap b(PictureLoader var0) {
      return var0.s;
   }

   private static boolean b(String path, Picture image) {
      try (BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(path))) {
         return image.d().compress(image.i() ? CompressFormat.PNG : CompressFormat.JPEG, 80, buffer);
      } catch (Throwable error) {
         Log.e("PictureLoader", "save thumbnail: " + path + ", " + error);
         return false;
      }
   }

   private static long previewStamp(long modified) {
      // Old previews may be JPEGs with lost alpha. The cache removes a stale file on lookup.
      return ~modified;
   }

   void savePreview(String key, long modified, File file) {
      this.j.a(key, previewStamp(modified), file);
   }

   static ContentResolver c(PictureLoader var0) {
      return var0.i;
   }

   static int d(PictureLoader var0) {
      return var0.c;
   }

   private boolean d(Uri var1) {
      PreviewWriteState var3 = (PreviewWriteState)this.s.get(var1);
      boolean var2;
      if (var3 != null && var3.a) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   static ConcurrentLruCache e(PictureLoader var0) {
      return var0.n;
   }

   private synchronized boolean e() {
      return this.j.d() || this.j.a(this.h, StoragePaths.b(this.h, "preview"), "preview", 1);
   }

   static ConcurrentLruCache f(PictureLoader var0) {
      return var0.o;
   }

   private void f() {
        synchronized (this.q) {
            if (this.r == null) {
                this.r = BitmapUtils.a(this.b / 16, 32, android.graphics.Bitmap.Config.ARGB_8888);
            }
        }
    }

   private void g() {
        synchronized (this.q) {
            if (this.r != null) {
                this.r.recycle();
                this.r = null;
            }
        }
    }

   static boolean g(PictureLoader var0) {
      return var0.e();
   }

   static void h(PictureLoader var0) {
      var0.g();
   }

   static int i(PictureLoader var0) {
      return var0.d;
   }

   static int j(PictureLoader var0) {
      return var0.e;
   }

   static void k(PictureLoader var0) {
      var0.f();
   }

   static int l(PictureLoader var0) {
      return var0.f;
   }

   static int m(PictureLoader var0) {
      return var0.g;
   }

   static ConcurrentLinkedQueue n(PictureLoader var0) {
      return var0.m;
   }

   public PictureLoadFuture a(Uri var1, char var2, int var3, Handler var4) {
      return this.a(UriFile.a(var2, var1, null), var3, var4);
   }

   public PictureLoadFuture a(CommonFile var1, int var2, Handler var3) {
      PictureLoadFuture var4 = new PictureLoadFuture(this, new DecodePictureTask(this, var1, var2, var3));
      this.k.a(var4);
      return var4;
   }

   public Picture a(Uri var1) {
      Picture var2 = (Picture)this.o.b(var1);
      Picture var3;
      if (var2 != null) {
         var3 = var2.c();
      } else {
         var3 = null;
      }

      return var3;
   }

   public void a() {
      if (this.j.d()) {
         this.l.submit(new ClosePreviewCache(this));
      }
   }

   public void a(Uri var1, boolean var2) {
      this.o.c(var1);
      this.n.c(var1);
      if (var2 && this.e()) {
         this.j.c(var1.toString());
      }
   }

   public void a(Picture var1) {
      this.o.a(var1.i, var1.c());
   }

   public void a(Runnable var1) {
      this.k.execute(var1);
   }

   public PictureMetadata b(Uri var1) {
      return (PictureMetadata)this.n.b(var1);
   }

   public void b() {
      this.o.c();
      this.n.c();
   }

   public void b(Picture var1) {
      if (var1 != null) {
         var1.c();
      }

      if (this.p != null) {
         this.p.m();
      }

      this.p = var1;
   }

   public void c() {
      QuickApp.s.b();
      if (this.e()) {
         this.j.e();
      }
   }

   public boolean c(Uri var1) {
      boolean var2;
      if (this.o.b(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Picture d() {
      Picture var1 = this.p;
      this.p = null;
      return var1;
   }
}
