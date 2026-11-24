package com.alensw.ui.c;

import android.content.SharedPreferences;
class bw implements com.alensw.ui.a.s {
   final SharedPreferences a;
   final com.alensw.ui.c.bf b;

   bw(com.alensw.ui.c.bf var1, SharedPreferences var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.a.edit().putString("name_format", var1).commit();
         this.b.g(var1);
      }
   }
}
