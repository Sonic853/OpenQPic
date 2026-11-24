package com.alensw.ui.c;

class cj implements Runnable {
   final com.alensw.a.e a;
   final com.alensw.ui.c.bz b;

   cj(com.alensw.ui.c.bz var1, com.alensw.a.e var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.a(this.a, true, new com.alensw.ui.c.ck(this));
   }
}
