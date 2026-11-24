package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;

class br implements Runnable {
   final com.alensw.ui.c.bf a;

   br(com.alensw.ui.c.bf var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(this.a.g, true, null);
      if (!QuickApp.q.b) {
         QuickApp.o.f(this.a.g);
         QuickApp.o.a(true);
      }
   }
}
