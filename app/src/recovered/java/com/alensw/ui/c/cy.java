package com.alensw.ui.c;

class cy implements Runnable {
   final com.alensw.ui.c.cu a;
   final com.alensw.ui.c.cx b;

   cy(com.alensw.ui.c.cx var1, com.alensw.ui.c.cu var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.a != null) {
         this.a.i();
      }
   }
}
