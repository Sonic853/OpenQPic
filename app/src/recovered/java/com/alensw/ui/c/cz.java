package com.alensw.ui.c;

class cz implements Runnable {
   final com.alensw.ui.c.cu a;
   final com.alensw.ui.c.cu b;
   final com.alensw.ui.c.cx c;

   cz(com.alensw.ui.c.cx var1, com.alensw.ui.c.cu var2, com.alensw.ui.c.cu var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      this.a.I = false;
      this.a.e(0);
      com.alensw.ui.c.cx.a(this.c).removeView(this.a.D);
      if (this.b != null) {
         this.b.i();
      }
   }
}
