package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;

class ck implements Runnable {
   final com.alensw.ui.c.cj a;

   ck(com.alensw.ui.c.cj var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (!QuickApp.q.b) {
         this.a.b.l.f(this.a.a);
         this.a.b.l.a(true);
         if (this.a.b.C.q()) {
            this.a.b.C.d(false);
         }
      }

      this.a.b.j.requestLayout();
   }
}
