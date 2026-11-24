package com.alensw.a;

import java.util.Comparator;

final class ad implements Comparator {
   public int a(com.alensw.a.e var1, com.alensw.a.e var2) {
      int var3 = com.alensw.a.x.e(var1, var2);
      if (var3 == 0) {
         var3 = com.alensw.a.x.a(var2, var1);
      }

      return var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((com.alensw.a.e)var1, (com.alensw.a.e)var2);
   }
}
