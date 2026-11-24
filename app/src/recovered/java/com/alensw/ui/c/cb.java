package com.alensw.ui.c;

class cb implements Runnable {
   final com.alensw.a.e a;
   final com.alensw.ui.c.bz b;

   cb(com.alensw.ui.c.bz var1, com.alensw.a.e var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      int var1 = this.b.l.a(this.a);
      if (var1 >= 0) {
         this.b.j.b(var1);
      } else {
         this.b.a(this.a, new com.alensw.ui.c.cc(this));
      }
   }
}
