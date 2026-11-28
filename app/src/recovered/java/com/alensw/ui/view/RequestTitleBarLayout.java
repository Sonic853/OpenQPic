package com.alensw.ui.view;

class RequestTitleBarLayout implements Runnable {
   final TitleBar a;

   RequestTitleBarLayout(TitleBar var1) {
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
