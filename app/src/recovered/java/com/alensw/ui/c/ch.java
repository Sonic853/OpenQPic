package com.alensw.ui.c;

class ch implements com.alensw.ui.a.s {
   final com.alensw.a.o a;
   final Runnable b;
   final com.alensw.ui.c.bz c;

   ch(com.alensw.ui.c.bz var1, com.alensw.a.o var2, Runnable var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void a(String var1) {
      com.alensw.a.e var2 = new com.alensw.a.e(var1, -1);
      this.a.a(var2);
      this.c.l.f(var2);
      this.c.l.e(var2);
      this.b.run();
   }
}
