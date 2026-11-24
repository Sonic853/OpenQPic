package com.b.a;

import android.content.res.Resources;
import android.graphics.Picture;
import android.graphics.RectF;
import java.util.concurrent.ConcurrentHashMap;

public class b {
   private static final ConcurrentHashMap a = new ConcurrentHashMap(64);
   private int b = 0;
   private Picture c;
   private RectF d;
   private RectF e = null;

   b(Picture var1, RectF var2) {
      this.c = var1;
      this.d = var2;
   }

   public static com.b.a.l a(Resources var0, int var1) {
      return b(var0, var1).a();
   }

   public static com.b.a.l a(Resources var0, int var1, int var2) {
      return b(var0, var1, var2).a();
   }

   public static com.b.a.l a(Resources var0, int var1, int var2, int var3) {
      return b(var0, var1, var2).a(var3);
   }

   private static void a(int param0, com.b.a.b param1) {
        synchronized (a) {
            a.put(Integer.valueOf(param0), new java.lang.ref.SoftReference(param1));
        }
    }

   private static com.b.a.b b(int param0) {
        com.b.a.b srcType0872;
        synchronized (a) {
            java.lang.ref.SoftReference softReference = (java.lang.ref.SoftReference) a.get(Integer.valueOf(param0));
            srcType0872 = softReference != null ? (com.b.a.b) softReference.get() : null;
        }
        return srcType0872;
    }

   public static com.b.a.b b(Resources var0, int var1) {
      com.b.a.b var3 = b(var1);
      com.b.a.b var2 = var3;
      if (var3 == null) {
         var2 = com.b.a.d.a(var0, var1);
         a(var1, var2);
      }

      return var2;
   }

   public static com.b.a.b b(Resources var0, int var1, int var2) {
      com.b.a.b var4 = b(var1);
      if (var4 != null && var4.b == var2) {
         return var4;
      } else {
         com.b.a.b var3 = com.b.a.d.a(var0, var1, var2);
         var3.b = var2;
         a(var1, var3);
         return var3;
      }
   }

   public com.b.a.l a() {
      return new com.b.a.l(this.c, -1, -1);
   }

   public com.b.a.l a(int var1) {
      return new com.b.a.l(this.c, var1, var1);
   }

   void a(RectF var1) {
      this.e = var1;
   }
}
