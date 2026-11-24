package com.alensw.ui.activity;

class az implements Runnable {
   final com.alensw.ui.activity.ao a;

   az(com.alensw.ui.activity.ao var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(false);
      this.a.I.removeCallbacks(this);
   }
}
