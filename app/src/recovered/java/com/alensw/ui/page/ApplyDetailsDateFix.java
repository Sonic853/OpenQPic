package com.alensw.ui.page;

class ApplyDetailsDateFix implements Runnable {
   final ConfirmDetailsDateFix a;

   ApplyDetailsDateFix(ConfirmDetailsDateFix var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      long var1 = MediaDetails.b(this.a.b);
      if (this.a.a != null) {
         this.a.a.a(var1, MediaDetails.c(this.a.b).j);
      }
   }
}
