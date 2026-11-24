package com.alensw.ui.view;

class l implements Runnable {
   final int a;
   final com.alensw.ui.view.j b;

   l(com.alensw.ui.view.j var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (com.alensw.ui.view.j.a(this.b) != null) {
         com.alensw.ui.view.j.a(this.b).m();
         com.alensw.ui.view.j.a(this.b, null);
      }

      this.b.G = false;
      this.b.A = 0;
      this.b.invalidate();
      if (this.b.K != null) {
         this.b.K.b(this.a);
      }
   }
}
