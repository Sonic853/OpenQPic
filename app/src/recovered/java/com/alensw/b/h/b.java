package com.alensw.b.h;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build.VERSION;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import java.lang.reflect.Method;

public class b {
   private b() {}

   public static boolean a = false;
   public static int b = 0;
   public static int c = 0;
   public static int d = 0;
   private static Object e;
   private static Method f;

   static {
      if (VERSION.SDK_INT < 8) {
         try {
            Class var0 = Class.forName("android.media.ThumbnailUtil");
            e = var0.newInstance();
            f = var0.getMethod("createVideoThumbnail", String.class);
         } catch (Throwable var1) {
         }
      }
   }

   public static int a(int var0, int var1) {
      int var2 = 1;

      for (int var3 = var0; var3 > var1; var3 = var0 / (var2 * var2)) {
         if (var2 < 8) {
            var2 *= 2;
         } else {
            var2 += 8;
         }
      }

      return var2;
   }

   public static int a(int var0, int var1, boolean var2, int var3) {
      if (var2) {
         var0 = Math.min(var0, var1);
      } else {
         var0 = Math.max(var0, var1);
      }

      var0 /= var3;
      if (var0 <= 1) {
         var0 = 1;
      } else if (var0 <= 8) {
         var0 = Integer.highestOneBit(var0);
      } else {
         var0 = var0 / 8 * 8;
      }

      return var0;
   }

   public static Bitmap a(int var0, int var1, Config var2) {
      OutOfMemoryError var9;
      label38: {
         label37: {
            Config var3;
            if (var2 != null) {
               var3 = var2;
            } else {
               try {
                  var3 = com.alensw.b.h.a.a;
               } catch (OutOfMemoryError var6) {
                  var9 = var6;
                  if (var2 != null) {
                     break label38;
                  }
                  break label37;
               } catch (Throwable var7) {
                  Log.e("BitmapUtils", "create bitmap: " + var7);
                  return null;
               }
            }

            try {
               return Bitmap.createBitmap(var0, var1, var3);
            } catch (OutOfMemoryError var4) {
               var9 = var4;
               if (var2 != null) {
                  break label38;
               }
            } catch (Throwable var5) {
               Log.e("BitmapUtils", "create bitmap: " + var5);
               return null;
            }
         }

         if (com.alensw.b.h.a.a == Config.ARGB_8888) {
            return a(var0, var1, Config.RGB_565);
         }
      }

      Log.e("BitmapUtils", "create bitmap: " + var9);
      return null;
   }

   public static Bitmap a(Bitmap var0) {
      Bitmap var1 = var0;
      if (var0 != null) {
         if (var0.getConfig() != null) {
            var1 = var0;
         } else {
            try {
               var1 = var0.copy(Config.ARGB_8888, false);
               var0.recycle();
            } catch (Throwable var2) {
               Log.e("BitmapUtils", "create GLCompatible: " + var2);
               var1 = var0;
            }
         }
      }

      return var1;
   }

   public static Bitmap a(Bitmap var0, float var1) {
       boolean alpha = var0.hasAlpha();
       Config config = alpha ? Config.ARGB_8888 : com.alensw.b.h.a.a;
      Bitmap result = null;
      while (result == null) {
         try {
            result = Bitmap.createBitmap(Math.round(var0.getWidth() * var1), Math.round(var0.getHeight() * var1), config);
            Canvas canvas = new Canvas(result);
            canvas.scale(var1, var1);
            canvas.drawBitmap(var0, 0.0F, 0.0F, new Paint(6));
            result.setHasAlpha(alpha);
            return result;
         } catch (OutOfMemoryError failure) {
            if (alpha || config != Config.ARGB_8888) return null;
            config = Config.RGB_565;
         } catch (Throwable failure) {
            Log.e("BitmapUtils", "create scaled: " + failure);
            return null;
         }
      }
      return null;
   }

   public static Bitmap a(com.alensw.b.c.f var0) {
      if (var0.a()) {
         try {
            String path = var0.d();
            if (VERSION.SDK_INT >= 8) return ThumbnailUtils.createVideoThumbnail(path, 1);
            return e == null ? null : (Bitmap) f.invoke(e, path);
         } catch (Throwable failure) {
            Log.e("BitmapUtils", "video thumb:" + failure);
            return null;
         }
      }
      if (VERSION.SDK_INT < 10) return null;
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      try {
         retriever.setDataSource(var0.b());
         return retriever.getFrameAtTime(-1L);
      } catch (Throwable failure) {
         Log.e("BitmapUtils", "video thumb:" + failure);
         return null;
      } finally {
         try { retriever.release(); } catch (Throwable ignored) {}
      }
   }

   public static Bitmap a(com.alensw.b.c.f var0, int var1, com.alensw.b.h.a var2) {
        int i2;
        int i3 = var2.outHeight * var2.outWidth;
        if (var2.d()) {
            var2.inSampleSize = a(i3, b * 1000000);
            var2.inPreferredConfig = Config.ARGB_8888;
            i2 = i3;
        } else {
            if (var1 > 0) {
                var2.inSampleSize = a(i3, var1);
            } else {
                var2.inSampleSize = i3 > c ? 2 : 1;
            }
            int i4 = i3 / (var2.inSampleSize * var2.inSampleSize);
            var2.inPreferredConfig = (a || i4 <= d) ? Config.ARGB_8888 : Config.RGB_565;
            i2 = i4;
        }
        while (!var2.mCancel) {
            try {
                return a(var0, var2);
            } catch (OutOfMemoryError e2) {
                if (i2 >= c) {
                    var2.inSampleSize *= 2;
                    int i5 = i3 / (var2.inSampleSize * var2.inSampleSize);
                    var2.inPreferredConfig = (a || i5 <= d) ? Config.ARGB_8888 : Config.RGB_565;
                    i2 = i5;
                } else if (!a && var2.inPreferredConfig == Config.ARGB_8888) {
                    var2.inPreferredConfig = Config.RGB_565;
                } else {
                    if (var2.inSampleSize >= 32) {
                        return null;
                    }
                    var2.inSampleSize *= 2;
                }
            } catch (Throwable th) {
                Log.e("BitmapUtils", "load bitmap: " + th);
                return null;
            }
        }
        return null;
    }

   public static Bitmap a(com.alensw.b.c.f file, com.alensw.b.h.a options) {
      if (VERSION.SDK_INT >= 28 && !options.inJustDecodeBounds && !options.mCancel
               && "image/webp".equals(options.outMimeType) && SystemAnimation.mayAnimateWebp(file)) {
          Bitmap frame = SystemAnimation.firstFrame(QuickApp.a().getContentResolver(), file.g(), options.inSampleSize);
         if (frame != null) {
            options.outWidth = frame.getWidth();
            options.outHeight = frame.getHeight();
            return frame;
         }
      }

      Bitmap bitmap = options.e ? null : BitmapFactory.decodeFileDescriptor(file.b(), null, options);
      if (bitmap != null || options.inJustDecodeBounds || options.mCancel) return bitmap;
      if (!options.e) {
         options.e = true;
         Log.e("BitmapUtils", "decode fd error!");
      }
      return file.a() ? BitmapFactory.decodeFile(file.d(), options) : null;
   }

   public static Bitmap a(com.alensw.b.c.f var0, boolean var1, int var2, com.alensw.b.h.a var3) {
      int var7 = var3.outWidth;
      int var6 = var3.outHeight;
      float var4 = (float)var3.outWidth / var3.outHeight;
      if (var4 > 0.5F && var4 < 2.0F) {
         var3.inSampleSize = a(var3.outWidth, var3.outHeight, var1, var2);
      } else {
         int var9 = var3.outWidth;
         int var8 = var3.outHeight;
         int var5;
         if (var1) {
            var5 = var2 * var2 * 4 / 3;
         } else {
            var5 = var2 * var2 * 3 / 4;
         }

         var3.inSampleSize = a(var8 * var9, var5 * 3);
      }

      var3.inJustDecodeBounds = false;
      var3.inPreferredConfig = com.alensw.b.h.a.a;
      Object var11 = null;

      Bitmap var10;
      while (true) {
         var10 = (Bitmap)var11;
         if (var3.mCancel) {
            break;
         }

         try {
            var10 = a(var0, var3);
            break;
         } catch (OutOfMemoryError var12) {
            if (var3.inPreferredConfig == Config.ARGB_8888) {
               var3.inPreferredConfig = Config.RGB_565;
            } else {
               var10 = (Bitmap)var11;
               if (var2 < 1920) {
                  break;
               }

               var2 /= 2;
               var3.inSampleSize *= 2;
               var3.inPreferredConfig = com.alensw.b.h.a.a;
            }
         } catch (Throwable var13) {
            Log.e("BitmapUtils", "load thumbnail: " + var0.g() + ", " + var7 + "x" + var6 + "/" + var3.inSampleSize + ", " + var13);
            var10 = (Bitmap)var11;
            break;
         }
      }

      return var10;
   }

   public static Bitmap a(com.alensw.b.h.c var0, int var1, com.alensw.b.h.a var2) {
        try {
            byte[] bArrG = var0.g();
            if (bArrG == null || var2.mCancel) {
                return null;
            }
            var2.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArrG, 0, bArrG.length, var2);
            var2.inJustDecodeBounds = false;
            if (var2.mCancel || var2.c() || var2.outWidth * var2.outHeight < var1) {
                return null;
            }
            long jE = var0.e();
            if (!a((int) (jE & 2147483647L), (int) ((jE >> 32) & 2147483647L), var2.outWidth, var2.outHeight, var0.f())) {
                return null;
            }
            var2.b();
            return BitmapFactory.decodeByteArray(bArrG, 0, bArrG.length, var2);
        } catch (Throwable th) {
            Log.e("BitmapUtils", "load exif: " + th);
            return null;
        }
    }

   public static void a(Context var0) {
      b = ((ActivityManager)var0.getSystemService("activity")).getMemoryClass();
      c = (int)(Math.max(b / 3.0F, 4.0F) * 1000000.0F);
      float var2 = b / 8.0F;
      float var1;
      if (b < 24) {
         var1 = 2.0F;
      } else {
         var1 = 3.2F;
      }

      d = (int)(Math.max(var2, var1) * 1000000.0F);
      Config var3;
      if (b >= 32) {
         var3 = Config.ARGB_8888;
      } else {
         var3 = Config.RGB_565;
      }

      com.alensw.b.h.a.a = var3;
   }

   public static boolean a(int var0, int var1, int var2, int var3, int var4) {
      boolean var5;
      if (var4 == 0 && var0 * var3 == var1 * var2) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public static boolean b(com.alensw.b.c.f file, com.alensw.b.h.a options) {
      try {
         options.inJustDecodeBounds = true;
         a(file, options);
      } catch (Throwable error) {
         Log.e("BitmapUtils", "get size: " + error);
         options.outHeight = -1;
         options.outWidth = -1;
      } finally {
         options.inJustDecodeBounds = false;
      }
      return (options.outWidth > 0 && options.outHeight > 0)
               || (VERSION.SDK_INT >= 28 && !options.mCancel && SystemAnimation.mayAnimateWebp(file)
                   && SystemAnimation.readBounds(QuickApp.a().getContentResolver(), file.g(), options));
   }
}
