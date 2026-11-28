package com.alensw.bean;

import com.alensw.sort.NameSortOrder;
import java.util.Comparator;

final class FileDateComparator implements Comparator {
   public int a(CommonFile var1, CommonFile var2) {
      long var6 = var1.h - var2.h;
      long var4 = var6;
      if (var6 == 0L) {
         var4 = NameSortOrder.a.a(var1.c, var2.c);
      }

      byte var3;
      if (var4 > 0L) {
         var3 = 1;
      } else if (var4 < 0L) {
         var3 = -1;
      } else {
         var3 = 0;
      }

      return var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((CommonFile)var1, (CommonFile)var2);
   }
}
