package com.alensw.b.b;

public class b {
   private b() {}

   private static com.alensw.b.b.c b = new com.alensw.b.b.c();
   private static com.alensw.b.b.c c = new com.alensw.b.b.a();
   public static com.alensw.b.b.c a = b;

   public static boolean a(boolean var0) {
      com.alensw.b.b.c var1;
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
