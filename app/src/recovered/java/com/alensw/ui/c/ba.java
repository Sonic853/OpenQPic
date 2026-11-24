package com.alensw.ui.c;

class ba implements Runnable {
   final com.alensw.ui.c.as a;

   ba(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.m();
   }
}
