package com.alensw.b.k;

class h extends com.alensw.b.k.c {
   final com.alensw.b.k.g a;

   h(com.alensw.b.k.g var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a();
      int delay = 0;
      while (!a(delay)) {
         synchronized (com.alensw.b.k.g.a(this.a)) {
            com.alensw.b.k.g.a(this.a, com.alensw.b.k.g.a(this.a).b());
            if (com.alensw.b.k.g.b(this.a) == null) {
               try {
                  com.alensw.b.k.g.a(this.a).wait();
               } catch (Throwable ignored) {
                  // Continue through the stop check after a wakeup or an interrupt.
               }
               continue;
            }
         }
         delay = this.a.b(com.alensw.b.k.g.b(this.a));
         synchronized (com.alensw.b.k.g.a(this.a)) {
            com.alensw.b.k.g.b(this.a).a();
            com.alensw.b.k.g.a(this.a, null);
         }
      }
      this.a.d();
   }
}
