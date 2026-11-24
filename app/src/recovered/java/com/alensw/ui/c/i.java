package com.alensw.ui.c;

class i implements Runnable {
   final com.alensw.ui.c.h a;

   i(com.alensw.ui.c.h var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.a(this.a.a, true);
      this.a.d.c(false);
      if (this.a.c != null) {
         this.a.c.run();
      }
   }
}
