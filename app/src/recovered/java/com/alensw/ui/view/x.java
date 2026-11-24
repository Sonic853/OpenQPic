package com.alensw.ui.view;

class x implements Runnable {
   final com.alensw.ui.view.w a;

   x(com.alensw.ui.view.w var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      com.alensw.ui.view.w.a(this.a, false);
      if (!com.alensw.ui.view.w.a(this.a) && com.alensw.ui.view.w.b(this.a) == 1) {
         com.alensw.ui.view.w.d(this.a).b(com.alensw.ui.view.w.c(this.a).x, com.alensw.ui.view.w.c(this.a).y);
      }
   }
}
