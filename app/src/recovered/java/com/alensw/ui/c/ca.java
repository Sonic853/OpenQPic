package com.alensw.ui.c;

class ca implements Runnable {
   final int a;
   final com.alensw.ui.c.bz b;

   ca(com.alensw.ui.c.bz var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.v = this.a;
      this.b.j.b(this.b.v, true);
   }
}
