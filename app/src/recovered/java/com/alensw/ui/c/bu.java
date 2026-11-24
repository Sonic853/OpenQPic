package com.alensw.ui.c;

class bu implements Runnable {
   final com.alensw.ui.c.bf a;

   bu(com.alensw.ui.c.bf var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.p();
   }
}
