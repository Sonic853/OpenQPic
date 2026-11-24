package com.alensw.ui.c;

import android.content.Intent;
class e implements com.alensw.ui.a.s {
   final String a;
   final com.alensw.ui.c.a b;

   e(com.alensw.ui.c.a var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(String var1) {
      if (var1.length() > 0) {
         Intent var2 = com.alensw.ui.c.dc.a(this.b.C, var1, this.a);
         this.b.C.a(com.alensw.ui.c.dc.class, var2, null);
         com.alensw.ui.c.a.e(var1);
      }
   }
}
