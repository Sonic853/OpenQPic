package com.alensw.a;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;
import java.util.Arrays;

class aw extends ContentObserver {
   final Object[] a;
   final com.alensw.a.av b;

   aw(com.alensw.a.av var1, Handler var2, Object[] var3) {
      super(var2);
      this.b = var1;
      this.a = var3;
   }

   @Override
   public void onChange(boolean var1) {
      boolean var3 = true;
      boolean var2 = true;
      super.onChange(var1);
      com.alensw.a.av.a(this.b).unregisterContentObserver(this);
      var1 = var3;
      if (com.alensw.a.av.b(this.b)) {
         Object[] var4 = com.alensw.a.av.c(this.b).a(com.alensw.a.av.a(this.b));
         if (!Arrays.equals(this.a, var4)) {
            var1 = var2;
         } else {
            var1 = false;
         }

         Log.d(com.alensw.a.av.d(this.b), "changed=" + var1);
      }

      com.alensw.a.av.f(this.b).post(new com.alensw.a.ax(this, var1));
   }
}
