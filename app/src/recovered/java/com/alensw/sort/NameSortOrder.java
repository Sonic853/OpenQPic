package com.alensw.sort;

public class NameSortOrder {
   private NameSortOrder() {}

   private static CollatedNameComparator b = new CollatedNameComparator();
   private static CollatedNameComparator c = new NaturalNameComparator();
   public static CollatedNameComparator a = b;

   public static boolean a(boolean var0) {
      CollatedNameComparator var1;
      if (var0) {
         var1 = c;
      } else {
         var1 = b;
      }

      if (a != var1) {
         a = var1;
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }
}
