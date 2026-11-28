package com.alensw.sort;

import java.text.Collator;
import java.util.Comparator;

public class CollatedNameComparator implements Comparator {
   private final Collator a = Collator.getInstance();

   public int a(String var1, String var2) {
      int var4 = this.a.compare(var1, var2);
      int var3 = var4;
      if (var4 == 0) {
         var3 = var4;
         if (var1 != null) {
            var3 = var1.compareTo(var2);
         }
      }

      return var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((String)var1, (String)var2);
   }
}
