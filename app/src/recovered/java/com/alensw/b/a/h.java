package com.alensw.b.a;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class h {
   private static Method c;
   private int a;
   private final LinkedHashMap b;

   static {
      try {
         c = LinkedHashMap.class.getMethod("eldest");
      } catch (Throwable var1) {
      }
   }

   public h(int var1) {
      this.a = var1;
      int var2 = var1;
      if (var1 <= 0) {
         var2 = 256;
      }

      this.b = new com.alensw.b.a.i(this, var2, 0.75F, true);
   }

   static int a(com.alensw.b.a.h var0) {
      return var0.a;
   }

   public final Object a(Object var1) {
      return this.b.get(var1);
   }

   public Object a(Object var1, Object var2) {
      var1 = this.b.put(var1, var2);
      if (var1 != null && var1 != var2) {
         this.b(var1);
      }

      return var1;
   }

   protected void b(Object var1) {
   }
}
