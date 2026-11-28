package com.alensw.ui.view;

class UpdateTitleBarActions implements Runnable {
   final TitleBar a;

   UpdateTitleBarActions(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.e();
   }
}
