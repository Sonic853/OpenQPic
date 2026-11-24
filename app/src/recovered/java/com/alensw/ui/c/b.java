package com.alensw.ui.c;

import android.content.Intent;

class b implements Runnable {
   final com.alensw.ui.c.m a;
   final Intent b;
   final com.alensw.ui.c.a c;

   b(com.alensw.ui.c.a var1, com.alensw.ui.c.m var2, Intent var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      this.a.a(this.b);
   }
}
