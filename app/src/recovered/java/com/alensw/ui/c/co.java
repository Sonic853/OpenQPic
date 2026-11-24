package com.alensw.ui.c;

import java.io.File;

class co implements com.alensw.ui.a.s {
   final com.alensw.ui.c.bz a;

   co(com.alensw.ui.c.bz var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = com.alensw.b.l.b.a(var1.trim(), '.');
      if (var1.length() != 0) {
         File var3 = new File(this.a.z, var1);
         this.a.b(var3);
      }
   }
}
