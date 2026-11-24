package com.alensw.ui.a;

import java.io.File;

class al implements com.alensw.ui.a.s {
   final com.alensw.ui.a.ak a;

   al(com.alensw.ui.a.ak var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = com.alensw.b.l.b.a(var1.trim(), '.');
      if (var1.length() != 0) {
         File var3 = this.a.c.a(var1);
         if (var3 != null) {
            this.a.b.a(var3);
         }
      }
   }
}
