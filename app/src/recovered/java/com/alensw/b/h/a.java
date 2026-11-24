package com.alensw.b.h;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import java.lang.reflect.Field;

public class a extends Options {
   public static Config a = Config.ARGB_8888;
   private static final com.alensw.b.i.c f = new com.alensw.b.i.c(4);
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

   public static void a(com.alensw.b.h.a param0) {
        synchronized (f) {
            f.b(param0);
        }
    }

   public static com.alensw.b.h.a e() {
        com.alensw.b.h.a srcType0193;
        synchronized (f) {
            srcType0193 = (com.alensw.b.h.a) f.a();
        }
        if (srcType0193 == null) {
            return new com.alensw.b.h.a();
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
