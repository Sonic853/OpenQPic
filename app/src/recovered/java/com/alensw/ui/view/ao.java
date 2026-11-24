package com.alensw.ui.view;

public class ao {
   static com.alensw.ui.view.ao[][] c = new com.alensw.ui.view.ao[3][3];
   int a;
   int b;

   static {
      for (int var0 = 0; var0 < 3; var0++) {
         for (int var1 = 0; var1 < 3; var1++) {
            c[var0][var1] = new com.alensw.ui.view.ao(var0, var1);
         }
      }
   }

   private ao(int var1, int var2) {
      b(var1, var2);
      this.a = var1;
      this.b = var2;
   }

   public static synchronized ao a(int row, int column) {
      b(row, column);
      return c[row][column];
   }

   private static void b(int var0, int var1) {
      if (var0 < 0 || var0 > 2) {
         throw new IllegalArgumentException("row must be in range 0-2");
      } else if (var1 < 0 || var1 > 2) {
         throw new IllegalArgumentException("column must be in range 0-2");
      }
   }

   public int a() {
      return this.a;
   }

   public int b() {
      return this.b;
   }

   @Override
   public String toString() {
      return "(row=" + this.a + ",clmn=" + this.b + ")";
   }
}
