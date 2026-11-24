package com.alensw.bean;

import java.util.Comparator;

final class a implements Comparator {
   public int a(CommonFile var1, CommonFile var2) {
        int i = var1.b == 'D' ? 0 : 1;
        int i2 = var2.b != 'D' ? 1 : 0;
        return i != i2 ? i - i2 : com.alensw.b.b.b.a.a(var1.c, var2.c);
    }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((CommonFile)var1, (CommonFile)var2);
   }
}
