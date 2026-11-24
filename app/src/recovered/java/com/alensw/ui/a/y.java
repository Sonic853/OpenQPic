package com.alensw.ui.a;

import com.alensw.ui.view.PasswordView;
class y implements com.alensw.ui.view.ay {
   final com.alensw.ui.a.ad a;
   final com.alensw.ui.a.x b;

   y(com.alensw.ui.a.x var1, com.alensw.ui.a.ad var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(PasswordView var1, String var2) {
      if (this.a.a(var1, var2)) {
         this.b.dismiss();
      }
   }
}
