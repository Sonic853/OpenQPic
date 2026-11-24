package com.alensw.b.h;

public class m {
   private m() {}

   public static int a = 8;
   public static int b = 256;

   public static final int a(int var0) {
      return var0 & 15;
   }

   public static final int a(int var0, int var1, int var2) {
      return var0 >> a + var2 << 4 | var2 | var1 >> a + var2 << 18;
   }

   public static final int b(int var0) {
      return 1 << (var0 & 15);
   }

   public static final int c(int var0) {
      return (var0 >> 4 & 16383) << (var0 & 15) + a;
   }

   public static final int d(int var0) {
      return (var0 >> 18 & 16383) << (var0 & 15) + a;
   }

   public static String e(int var0) {
      return "(" + c(var0) + "," + d(var0) + ")x" + b(var0);
   }
}
