package com.alensw.ui.c;

class cl implements Runnable {
   final com.alensw.ui.c.bz a;

   cl(com.alensw.ui.c.bz var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.y();
   }
}
