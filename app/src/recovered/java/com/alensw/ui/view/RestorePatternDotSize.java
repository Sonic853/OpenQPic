package com.alensw.ui.view;

class RestorePatternDotSize implements Runnable {
   final PatternCellState a;
   final LockPatternView b;

   RestorePatternDotSize(LockPatternView var1, PatternCellState var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      LockPatternView.a(this.b, LockPatternView.a(this.b), 1.0F, 192L, LockPatternView.b(this.b), this.a, null);
   }
}
