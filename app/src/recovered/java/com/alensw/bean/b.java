package com.alensw.bean;

import java.util.Comparator;

final class b implements Comparator {
   public int a(CommonFile var1, CommonFile var2) {
        int i = var1.b == 'D' ? 0 : 1;
        int i2 = var2.b != 'D' ? 1 : 0;
        return i != i2 ? i2 - i : com.alensw.b.b.b.a.a(var2.c, var1.c);
    }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((CommonFile)var1, (CommonFile)var2);
   }
}
