package com.alensw.ui.c;

class ds implements Runnable {
   final com.alensw.ui.c.dp a;

   ds(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      com.alensw.ui.c.dp.r(this.a);
   }
}
