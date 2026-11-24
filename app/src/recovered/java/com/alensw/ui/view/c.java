package com.alensw.ui.view;

class c implements Runnable {
   final com.alensw.ui.view.a a;

   c(com.alensw.ui.view.a var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      com.alensw.ui.view.a var2 = this.a;
      byte var1;
      if (com.alensw.ui.view.a.a(this.a)) {
         var1 = 3;
      } else {
         var1 = 0;
      }

      var2.a(var1);
   }
}
