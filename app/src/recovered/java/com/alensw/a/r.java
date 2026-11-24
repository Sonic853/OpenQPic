package com.alensw.a;

import java.io.DataInputStream;
import java.io.IOException;

class r extends com.alensw.b.c.d {
   final com.alensw.a.q a;

   r(com.alensw.a.q var1, int var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   protected Integer a(DataInputStream var1) {
      int var3;
      try {
         var3 = var1.readInt();
      } catch (IOException error) {
         throw com.alensw.compat.LegacyPlatform.propagate(error);
      }
      int var2 = var3;
      if ((var3 >> 16 & 0xFF) != 0) {
         int var4 = var3 & 15;
         if (var4 == 3) {
            var2 = 5;
         } else {
            var2 = var4;
            if (var4 == 1) {
               var2 = 6;
            }
         }

         var2 = com.alensw.a.e.a((var3 & 240) >> 4, var2);
      }

      return var2;
   }

   @Override
   protected Object b(DataInputStream var1) {
      return this.a(var1);
   }
}
