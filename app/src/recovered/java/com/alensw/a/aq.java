package com.alensw.a;

class aq implements Runnable {
   final com.alensw.a.ao a;

   aq(com.alensw.a.ao var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      com.alensw.a.ao.a(this.a).c();
   }
}
