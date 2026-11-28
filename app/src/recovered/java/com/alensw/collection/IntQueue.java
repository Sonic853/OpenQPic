package com.alensw.collection;

import android.util.Log;

public class IntQueue {
   private final int[] a;
   private int b;
   private int c;
   private int d;

   public IntQueue(int var1) {
      this.a = new int[var1];
   }

   public void a() {
      this.d = 0;
      this.c = 0;
      this.b = 0;
   }

   public boolean a(int var1) {
      boolean var6 = false;
      int var2 = this.c;
      int var3 = 0;

      boolean var5;
      while (true) {
         var5 = var6;
         if (var3 >= this.b) {
            break;
         }

         if (this.a[var2] == var1) {
            var5 = true;
            break;
         }

         int var4 = var2 + 1;
         var2 = var4;
         if (var4 >= this.a.length) {
            var2 = 0;
         }

         var3++;
      }

      return var5;
   }

   public int b() {
      int var1;
      if (this.b > 0) {
         var1 = this.a[this.c];
         this.a[this.c] = 0;
         this.c = (this.c + 1) % this.a.length;
         this.b--;
      } else {
         Log.e("IntQueue", "empty!");
         var1 = 0;
      }

      return var1;
   }

   public void b(int var1) {
      if (this.b < this.a.length) {
         this.a[this.d] = var1;
         this.d = (this.d + 1) % this.a.length;
         this.b++;
      } else {
         Log.e("IntQueue", "full!");
      }
   }

   public final int c() {
      return this.b;
   }
}
