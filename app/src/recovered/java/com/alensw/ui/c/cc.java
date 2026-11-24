package com.alensw.ui.c;

import android.widget.Toast;
import com.alensw.PicFolder.QuickApp;

class cc implements Runnable {
   final com.alensw.ui.c.cb a;

   cc(com.alensw.ui.c.cb var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.a.j() > 0) {
         QuickApp.s.f(this.a.a.b(0));
      } else if (!this.a.a.p()) {
         Toast.makeText(this.a.b.C, "Create blank picture failed!", 1).show();
         return;
      }

      int var1 = this.a.b.l.d(this.a.a);
      if (this.a.b.H == 2) {
         this.a.b.j.requestLayout();
         this.a.b.j.b(var1);
      }
   }
}
