package com.alensw.ui.c;

class af implements com.alensw.ui.a.s {
   final com.alensw.ui.c.z a;

   af(com.alensw.ui.c.z var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = com.alensw.b.l.b.a(var1.trim(), '.');
      if (var1.length() != 0) {
         com.alensw.ui.c.z.a(this.a, var1);
      }
   }
}
