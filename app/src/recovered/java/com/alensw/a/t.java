package com.alensw.a;

import com.alensw.PicFolder.QuickApp;
import java.util.HashMap;
import java.util.concurrent.Callable;

class t implements Callable {
   final boolean a;
   final String b;
   final com.alensw.a.s c;

   t(com.alensw.a.s var1, boolean var2, String var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   public HashMap a() {
      String[] var1;
      if (this.a) {
         var1 = com.alensw.a.ba.m;
      } else {
         var1 = com.alensw.a.ba.l;
      }

      return QuickApp.s.a(this.b, var1);
   }

   @Override
   public Object call() {
      return this.a();
   }
}
