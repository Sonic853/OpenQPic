package com.alensw.svg;

import android.content.res.Resources;
import android.graphics.Picture;
import android.graphics.RectF;
import java.util.concurrent.ConcurrentHashMap;

public class SvgPicture {
   private static final ConcurrentHashMap a = new ConcurrentHashMap(64);
   private int b = 0;
   private Picture c;
   private RectF d;
   private RectF e = null;

   SvgPicture(Picture var1, RectF var2) {
      this.c = var1;
      this.d = var2;
   }

   public static SvgDrawable a(Resources var0, int var1) {
      return b(var0, var1).a();
   }

   public static SvgDrawable a(Resources var0, int var1, int var2) {
      return b(var0, var1, var2).a();
   }

   public static SvgDrawable a(Resources var0, int var1, int var2, int var3) {
      return b(var0, var1, var2).a(var3);
   }

   private static void a(int param0, SvgPicture param1) {
        synchronized (a) {
            a.put(Integer.valueOf(param0), new java.lang.ref.SoftReference(param1));
        }
    }

   private static SvgPicture b(int param0) {
        SvgPicture srcType0872;
        synchronized (a) {
            java.lang.ref.SoftReference softReference = (java.lang.ref.SoftReference) a.get(Integer.valueOf(param0));
            srcType0872 = softReference != null ? (SvgPicture) softReference.get() : null;
        }
        return srcType0872;
    }

   public static SvgPicture b(Resources var0, int var1) {
      SvgPicture var3 = b(var1);
      SvgPicture var2 = var3;
      if (var3 == null) {
         var2 = SvgParser.a(var0, var1);
         a(var1, var2);
      }

      return var2;
   }

   public static SvgPicture b(Resources var0, int var1, int var2) {
      SvgPicture var4 = b(var1);
      if (var4 != null && var4.b == var2) {
         return var4;
      } else {
         SvgPicture var3 = SvgParser.a(var0, var1, var2);
         var3.b = var2;
         a(var1, var3);
         return var3;
      }
   }

   public SvgDrawable a() {
      return new SvgDrawable(this.c, -1, -1);
   }

   public SvgDrawable a(int var1) {
      return new SvgDrawable(this.c, var1, var1);
   }

   void a(RectF var1) {
      this.e = var1;
   }
}
