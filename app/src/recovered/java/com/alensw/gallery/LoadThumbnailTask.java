package com.alensw.gallery;

import android.net.Uri;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.documents.DocumentProviderAccess;
import com.alensw.image.BitmapDecodeOptions;
import com.alensw.image.PictureBitmap;
import com.alensw.io.OpenFileHandle;
import com.alensw.jni.JniUtils;
import com.alensw.ui.shape.PlaybackIconShape;
import java.io.File;

class LoadThumbnailTask implements Runnable {
   final ThumbnailLoader a;
   private final boolean b;
   private final ScannedMediaFile c;
   private final CommonFile d;
   private final ThumbnailCallback e;
   private BitmapDecodeOptions f;
   private Uri g;

   public LoadThumbnailTask(ThumbnailLoader var1, ScannedMediaFile var2, boolean var3, ThumbnailCallback var4) {
      this.a = var1;
      this.b = var3;
      this.c = var2;
      this.d = null;
      this.e = var4;
   }

   public LoadThumbnailTask(ThumbnailLoader var1, CommonFile var2, boolean var3, ThumbnailCallback var4) {
      this.a = var1;
      this.b = var3;
      this.d = var2;
      this.c = null;
      this.e = var4;
   }

   private OpenFileHandle a(File file, Uri uri, int size) {
      if (file != null) {
         try {
            return OpenFileHandle.a(file, true);
         } catch (Throwable error) {
            if (!(error instanceof java.io.FileNotFoundException)) return null;
         }
      }
      try {
         if (DocumentProviderAccess.d(uri)) {
            synchronized (this) {
               this.g = uri;
            }
            return new OpenFileHandle(uri, DocumentProviderAccess.a(ThumbnailLoader.c(this.a), uri, size));
         }
         return OpenFileHandle.a(ThumbnailLoader.c(this.a), uri);
      } catch (Throwable ignored) {
         return null;
      }
   }

   protected void a() {
      ThumbnailDatabase var12 = QuickApp.r;
      BitmapDecodeOptions var13 = this.f;
      long var5 = 0L;
      char var1;
      Uri var8;
      File var9;
      if (this.c != null) {
         var9 = new File(this.c.a, this.c.b);
         var1 = this.c.c;
         if (this.c.d > 0) {
            var5 = this.c.d;
         } else {
            var5 = JniUtils.fuGetFileTime(var9.getPath());
         }

         var8 = null;
      } else if (this.d != null) {
         String var7 = this.d.j();
         File var16;
         if (var7 != null) {
            var16 = new File(var7);
         } else {
            var16 = null;
         }

         var8 = this.d.i();
         var1 = this.d.h();
         var5 = this.d.e();
         var9 = var16;
      } else {
         var8 = null;
         var9 = null;
         var1 = 0;
      }

      if (var9 != null || var8 != null) {
         var13.c = 0;
         var13.inPreferredConfig = ThumbnailDatabase.a;
         PictureBitmap var17;
         if (var9 != null) {
            var17 = var12.a(var9.getPath(), var5, var13);
         } else {
            var17 = null;
         }

         PictureBitmap var10 = var17;
         if (var17 == null) {
            var10 = var17;
            if (var8 != null) {
               var10 = var17;
               if (!var13.mCancel) {
                  var10 = var12.a(var8, var5, var13);
               }
            }
         }

         var17 = var10;
         if (var10 != null) {
            if (this.b && var10.g() <= var12.d && var10.h() <= var12.e) {
               var10.m();
               var17 = null;
            } else {
               var17 = var10;
               label103:
               if (!this.b) {
                  if (var10.g() < var12.f) {
                     var17 = var10;
                     if (var10.h() < var12.g) {
                        break label103;
                     }
                  }

                  PictureBitmap var11 = var10.a(var12.d, var12.e, var12.b, ThumbnailDatabase.a);
                  var17 = var10;
                  if (var11 != null) {
                     var10.m();
                     var17 = var11;
                  }
               }
            }
         }

         var10 = var17;
         if (var17 == null) {
            var10 = var17;
            if (!var13.mCancel) {
               int var3;
               if (this.b) {
                  var3 = var12.f;
               } else {
                  var3 = var12.d;
               }

               int var4;
               if (this.b) {
                  var4 = var12.g;
               } else {
                  var4 = var12.e;
               }

               OpenFileHandle var20 = this.a(var9, var8, var3 * 4 / 3);
               var10 = var17;
               if (var20 != null) {
                  char var2;
                  if (var8 != null && DocumentProviderAccess.d(var8)) {
                     var2 = 'I';
                  } else {
                     var2 = var1;
                  }

                  var10 = var12.a(var20, var2, var3, var4, var5, var13);
                  var20.h();
               }
            }
         }

         if (var10 != null) {
            if (!var13.mCancel) {
               if (var1 == 'V') {
                  PlaybackIconShape var19 = this.a.a();
                  int var14;
                  if (this.b) {
                     var14 = var12.f;
                  } else {
                     var14 = var12.d;
                  }

                  int var15;
                  if (this.b) {
                     var15 = var12.g;
                  } else {
                     var15 = var12.e;
                  }

                  var10.a(var14, var15, 0.33333334F, var19, ThumbnailLoader.b(this.a));
                  this.a.a(var19);
               }

               this.e.a(this.b(), var10);
            }

            var10.m();
         }
      }
   }

   public Object b() {
      Object var1;
      if (this.c != null) {
         var1 = this.c;
      } else {
         var1 = this.d;
      }

      return var1;
   }

   public void c() {
        BitmapDecodeOptions srcType0193;
        synchronized (this) {
            srcType0193 = this.f;
        }
        if (srcType0193 != null) {
            srcType0193.requestCancelDecode();
        }
    }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (var1 instanceof LoadThumbnailTask) {
            LoadThumbnailTask srcType0084 = (LoadThumbnailTask) var1;
            if (this.c != null) {
                return this.c.equals(srcType0084.c);
            }
            if (this.d != null) {
                return this.d.equals(srcType0084.d);
            }
        }
        return false;
    }

   @Override
   public int hashCode() {
      int var1;
      if (this.c != null) {
         var1 = this.c.hashCode();
      } else if (this.d != null) {
         var1 = this.d.hashCode();
      } else {
         var1 = super.hashCode();
      }

      return var1;
   }

   @Override
   public void run() {
        BitmapDecodeOptions srcType0193E = BitmapDecodeOptions.e();
        synchronized (this) {
            this.f = srcType0193E;
        }
        ThumbnailLoader.d(this.a).add(this);
        a();
        ThumbnailLoader.d(this.a).remove(this);
        synchronized (this) {
            this.f = null;
        }
        BitmapDecodeOptions.a(srcType0193E);
    }

   @Override
   public String toString() {
      StringBuilder var2 = new StringBuilder().append("Name=");
      String var1;
      if (this.c != null) {
         var1 = this.c.b;
      } else if (this.d != null) {
         var1 = this.d.a();
      } else {
         var1 = null;
      }

      return var2.append(var1).toString();
   }
}
