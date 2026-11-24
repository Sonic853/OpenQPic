package com.alensw.ui.activity;

class q implements Runnable {
   final com.alensw.ui.activity.p a;

   q(com.alensw.ui.activity.p var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.recreate();
   }
}
