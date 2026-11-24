package com.alensw.ui.view;

class by implements Runnable {
   final TitleBar a;

   by(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      TitleBar.b(this.a).requestLayout();
      this.a.requestLayout();
      if (TitleBar.c(this.a) && TitleBar.d(this.a) != null) {
         TitleBar.d(this.a).requestLayout();
      }
   }
}
