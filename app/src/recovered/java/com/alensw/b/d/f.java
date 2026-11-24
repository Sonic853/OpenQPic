package com.alensw.b.d;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class f {
   private static com.alensw.b.d.f b;
   private static final com.alensw.b.k.a c = new com.alensw.b.k.a(2, 4, Integer.MAX_VALUE, 10);
   private Handler a = new Handler(Looper.getMainLooper());
   private final com.alensw.b.d.e f;
   private final com.alensw.b.d.d g = new com.alensw.b.d.d();
   private final ConcurrentHashMap h = new ConcurrentHashMap(256);
   private final ConcurrentHashMap i = new ConcurrentHashMap(256);
   private int j;
   private final AtomicReference k = new AtomicReference();

   private f(Context var1) {
      this.f = new com.alensw.b.d.e(var1);
   }

   public static long a(float var0, float var1) {
      return (long)(((int)((var0 + 90.0F) * 500.0F) / 500.0F * 2.0F * 90.0F + (int)((180.0F + var1) * 1000.0F) / 1000.0F) * 6378137.0F);
   }

   public static synchronized com.alensw.b.d.f a(Context var0) {
      if (b == null) b = new com.alensw.b.d.f(var0);
      if (!b.g.a()) b.g.a(var0);
      return b;
   }

   public static String a(String var0) {
      String var1 = var0;
      if (var0 != null) {
         var1 = var0.replace("自治", "");
      }

      return var1;
   }

   private void a() {
      Runnable var1 = (Runnable)this.k.getAndSet(null);
      if (var1 != null) {
         this.a.post(var1);
      }
   }

   public static boolean b(float var0, float var1) {
      boolean var2;
      if (!(var0 >= 3.86F)
         || !(var0 <= 53.55F)
         || !(var1 >= 73.66F)
         || !(var1 <= 135.05F)
         || var0 >= 21.88F && var0 <= 25.3F && var1 >= 120.13F && var1 <= 122.02F) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   static com.alensw.b.d.e c(com.alensw.b.d.f var0) {
      return var0.f;
   }

   static ConcurrentHashMap d(com.alensw.b.d.f var0) {
      return var0.h;
   }

   static com.alensw.b.d.d e(com.alensw.b.d.f var0) {
      return var0.g;
   }

   static int f(com.alensw.b.d.f var0) {
      return var0.j++;
   }

   static Handler g(com.alensw.b.d.f var0) {
      return var0.a;
   }

   static ConcurrentHashMap h(com.alensw.b.d.f var0) {
      return var0.i;
   }

   static int i(com.alensw.b.d.f var0) {
      return var0.j;
   }

   static void j(com.alensw.b.d.f var0) {
      var0.a();
   }

   public String a(float var1, float var2, long var3) {
      String var7;
      if (!(var1 < -90.0F) && !(var1 > 90.0F) && !(var2 < -180.0F) && !(var2 > 180.0F)) {
         long var5 = var3;
         if (var3 == 0L) {
            var5 = a(var1, var2);
         }

         String var8 = (String)this.h.get(var5);
         var7 = var8;
         if (var8 == null) {
            var8 = (String)this.g.a(var5);
            var7 = var8;
            if (var8 != null) {
               this.h.put(var5, var8);
               var7 = var8;
            }
         }
      } else {
         var7 = null;
      }

      return var7;
   }

   public void a(Runnable var1) {
      if (this.k.compareAndSet(null, var1)) {
         this.j = 0;
      }
   }

   public boolean a(float var1, float var2, long var3, com.alensw.b.d.g var5) {
      boolean var6;
      if (!(var1 < -90.0F) && !(var1 > 90.0F) && !(var2 < -180.0F) && !(var2 > 180.0F)) {
         if (var3 == 0L) {
            var3 = a(var1, var2);
         }

         com.alensw.b.d.h var7 = new com.alensw.b.d.h(this, var1, var2, var3, var5);
         if (this.i.putIfAbsent(var3, var7) == null) {
            c.execute(var7);
         }

         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }
}
