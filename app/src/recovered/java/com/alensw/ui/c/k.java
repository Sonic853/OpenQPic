package com.alensw.ui.c;

import android.widget.Toast;

class k implements Runnable {
   final Object a;
   final com.alensw.ui.c.j b;

   k(com.alensw.ui.c.j var1, Object var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.a instanceof String) {
         Toast.makeText(this.b.f.C, (String)this.a, 1).show();
      } else {
         Toast.makeText(this.b.f.C, (Integer)this.a, 1).show();
      }
   }
}
