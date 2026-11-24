package com.alensw.ui.view;

import java.util.List;

class av implements com.alensw.ui.view.aq {
   final LockPatternView a;
   final PasswordView b;

   av(PasswordView var1, LockPatternView var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a() {
   }

   @Override
   public void a(List var1) {
   }

   @Override
   public void b() {
   }

   @Override
   public void b(List var1) {
      if (var1.size() <= 1) {
         this.a.a();
      } else if (PasswordView.a(this.b) != null) {
         String var2 = LockPatternView.a(var1);
         PasswordView.a(this.b).a(this.b, var2);
      }
   }
}
