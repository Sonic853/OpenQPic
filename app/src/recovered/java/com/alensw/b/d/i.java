package com.alensw.b.d;

class i implements Runnable {
   final com.alensw.b.d.c a;
   final com.alensw.b.d.h b;

   i(com.alensw.b.d.h var1, com.alensw.b.d.c var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.d.a(this.a);
   }
}
