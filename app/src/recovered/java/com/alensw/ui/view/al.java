package com.alensw.ui.view;

class al implements Runnable {
   final com.alensw.ui.view.ap a;
   final LockPatternView b;

   al(LockPatternView var1, com.alensw.ui.view.ap var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      LockPatternView.a(this.b, LockPatternView.a(this.b), 1.0F, 192L, LockPatternView.b(this.b), this.a, null);
   }
}
