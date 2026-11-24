package com.alensw.ui.view;

class br implements Runnable {
   final com.alensw.ui.view.bp a;

   br(com.alensw.ui.view.bp var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.m.isStateful()) {
         this.a.m.setState(com.alensw.ui.view.bp.f);
      }
   }
}
