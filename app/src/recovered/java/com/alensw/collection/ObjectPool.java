package com.alensw.collection;

import android.util.Log;

public class ObjectPool {
   private final Object[] a;
   private int b;

   public ObjectPool(int var1) {
      this.a = new Object[var1];
   }

   private boolean c(Object var1) {
      boolean var4 = false;
      int var2 = 0;

      boolean var3;
      while (true) {
         var3 = var4;
         if (var2 >= this.b) {
            break;
         }

         if (this.a[var2] == var1) {
            var3 = true;
            break;
         }

         var2++;
      }

      return var3;
   }

   public Object a() {
      Object var2;
      if (this.b > 0) {
         int var1 = this.b - 1;
         var2 = this.a[var1];
         this.a[var1] = null;
         this.b--;
      } else {
         var2 = null;
      }

      return var2;
   }

   protected void a(Object var1) {
   }

   public void b(Object var1) {
      if (this.c(var1)) {
         this.a(var1);
         Log.e("Pool", "already in pool" + var1);
      } else if (this.b < this.a.length) {
         Object[] var3 = this.a;
         int var2 = this.b++;
         var3[var2] = var1;
      } else {
         this.a(var1);
      }
   }
}
