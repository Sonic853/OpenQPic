package com.alensw.ui.activity;

class ak implements com.alensw.ui.a.s {
   final com.alensw.ui.activity.aj a;

   ak(com.alensw.ui.activity.aj var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      this.a.b.edit().putString("slideshow_interval", var1).commit();
      this.a.d.setSummary(var1 + ' ' + this.a.e);
   }
}
