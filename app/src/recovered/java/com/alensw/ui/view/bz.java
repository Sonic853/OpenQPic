package com.alensw.ui.view;

class bz implements Runnable {
   final TitleBar a;

   bz(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.e();
   }
}
