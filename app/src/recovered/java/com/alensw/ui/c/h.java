package com.alensw.ui.c;

class h implements Runnable {
   final com.alensw.a.e a;
   final com.alensw.a.e b;
   final Runnable c;
   final com.alensw.ui.c.a d;

   h(com.alensw.ui.c.a var1, com.alensw.a.e var2, com.alensw.a.e var3, Runnable var4) {
      this.d = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   @Override
   public void run() {
      this.a.b(false);
      this.d.b.post(new com.alensw.ui.c.i(this));
   }
}
