package com.alensw.ui.c;

class bx implements com.alensw.ui.a.s {
   final com.alensw.ui.c.bf a;

   bx(com.alensw.ui.c.bf var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = com.alensw.b.l.b.g(com.alensw.b.l.b.a(var1.trim(), '.'));
      if (var1.length() != 0 && !this.a.g.getName().equals(var1)) {
         this.a.f(var1);
      }
   }
}
