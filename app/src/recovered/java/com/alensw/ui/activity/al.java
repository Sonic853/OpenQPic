package com.alensw.ui.activity;

class al implements Runnable {
   final MainPreference a;

   al(MainPreference var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      new com.alensw.ui.activity.am(this, this.a, this.a.getString(com.alensw.PicFolder.R.string.running_wait), 0).c();
   }
}
