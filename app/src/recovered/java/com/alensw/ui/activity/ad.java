package com.alensw.ui.activity;

class ad implements Runnable {
   final com.alensw.ui.activity.ac a;

   ad(com.alensw.ui.activity.ac var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a.setChecked(false);
   }
}
