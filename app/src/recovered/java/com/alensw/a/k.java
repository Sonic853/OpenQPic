package com.alensw.a;

import java.util.Comparator;

final class k implements Comparator {
   public int a(com.alensw.a.d var1, com.alensw.a.d var2) {
      int var4 = var2.e - var1.e;
      int var3 = var4;
      if (var4 == 0) {
         var3 = com.alensw.b.b.b.a.a(var2.b, var1.b);
      }

      return var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((com.alensw.a.d)var1, (com.alensw.a.d)var2);
   }
}
