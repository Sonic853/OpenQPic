package com.alensw.image;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import com.alensw.collection.ObjectPool;
import java.lang.reflect.Field;

public class BitmapDecodeOptions extends Options {
   public static Config a = Config.ARGB_8888;
   private static final ObjectPool f = new ObjectPool(4);
   private static Field g;
   public int b;
   public int c;
   public boolean d;
   protected boolean e;

   static {
      try {
         g = Options.class.getField("inNativeAlloc");
      } catch (Throwable var1) {
      }
   }

   public static void a(BitmapDecodeOptions param0) {
        synchronized (f) {
            f.b(param0);
        }
    }

   public static BitmapDecodeOptions e() {
        BitmapDecodeOptions srcType0193;
        synchronized (f) {
            srcType0193 = (BitmapDecodeOptions) f.a();
        }
        if (srcType0193 == null) {
            return new BitmapDecodeOptions();
        }
        srcType0193.a();
        return srcType0193;
    }

   public void a() {
      this.mCancel = false;
      this.inDither = false;
      this.inJustDecodeBounds = false;
      this.inPreferredConfig = null;
      this.inSampleSize = 1;
      this.outWidth = 0;
      this.outHeight = 0;
      this.outMimeType = null;
      this.b = 0;
      this.c = 0;
      this.d = false;
      this.e = false;
   }

   public void a(boolean var1) {
      if (g != null) {
         try {
            g.setBoolean(this, var1);
         } catch (Throwable var3) {
         }
      }
   }

   public void b() {
      this.inJustDecodeBounds = false;
      this.inSampleSize = 1;
      this.inPreferredConfig = a;
   }

   public boolean c() {
      boolean var1;
      if (this.outWidth >= 0 && this.outHeight >= 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean d() {
      if (g != null) {
         try {
            return g.getBoolean(this);
         } catch (Throwable var3) {
         }
      }

      return false;
   }

   @Override
   public String toString() {
      return "("
         + this.outWidth
         + "x"
         + this.outHeight
         + "/"
         + this.inSampleSize
         + "x"
         + this.c
         + '°'
         + "), type="
         + this.outMimeType
         + ", cancel="
         + this.mCancel;
   }
}
