package com.alensw.ui.c;

class dt implements Runnable {
   final com.alensw.ui.c.dp a;

   dt(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void run() {
        if (this.a.H == 2) {
            if (com.alensw.ui.c.dp.a(this.a).m()) {
                this.a.a(com.alensw.ui.c.dp.a(this.a), (Runnable) null);
            }
            this.a.b.postDelayed(this, 60000L);
        }
    }
}
