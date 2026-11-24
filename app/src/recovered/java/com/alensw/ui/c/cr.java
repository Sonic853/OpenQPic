package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;

class cr implements Runnable {
   final boolean a;
   final com.alensw.ui.c.bz b;

   cr(com.alensw.ui.c.bz var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      QuickApp.q.b = this.a;
      this.b.a(false);
   }
}
