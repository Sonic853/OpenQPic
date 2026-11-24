package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;

class f implements Runnable {
   final String a;
   final com.alensw.ui.c.a b;

   f(com.alensw.ui.c.a var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      QuickApp.s.a(this.a);
   }
}
