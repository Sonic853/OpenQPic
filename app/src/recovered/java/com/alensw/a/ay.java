package com.alensw.a;

import android.database.Cursor;
import com.alensw.bean.CommonFolder;

class ay implements Runnable {
   final Cursor a;
   final boolean b;
   final com.alensw.a.av c;

   ay(com.alensw.a.av var1, Cursor var2, boolean var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      boolean var2 = true;
      boolean var1;
      if (this.a == null && com.alensw.a.av.c(this.c).n()) {
         var1 = false;
      } else {
         var1 = true;
      }

      com.alensw.a.az var4 = com.alensw.a.av.e(this.c);
      CommonFolder var3;
      if (var1) {
         var3 = com.alensw.a.av.c(this.c);
      } else {
         var3 = null;
      }

      if (this.b) {
         var2 = false;
      }

      var4.b(var3, var2);
   }
}
