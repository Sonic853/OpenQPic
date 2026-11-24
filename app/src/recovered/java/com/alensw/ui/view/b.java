package com.alensw.ui.view;

class b implements Runnable {
   final com.alensw.ui.view.a a;

   b(com.alensw.ui.view.a var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(false);
      this.a.a.removeCallbacks(this);
   }
}
