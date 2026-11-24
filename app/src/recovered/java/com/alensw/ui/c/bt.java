package com.alensw.ui.c;

class bt implements Runnable {
   final com.alensw.ui.c.bf a;

   bt(com.alensw.ui.c.bf var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.n();
   }
}
