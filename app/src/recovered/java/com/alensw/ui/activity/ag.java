package com.alensw.ui.activity;

class ag implements Runnable {
   final com.alensw.ui.activity.ae a;

   ag(com.alensw.ui.activity.ae var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.edit().remove("password").commit();
      this.a.a.setChecked(false);
      this.a.a.setSummary(null);
      this.a.c.a(com.alensw.PicFolder.R.string.password_cleared);
   }
}
