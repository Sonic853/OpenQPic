package com.alensw.PicFolder;

class aq implements Runnable {
   final com.alensw.PicFolder.ao a;

   aq(com.alensw.PicFolder.ao var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.a(false);
      this.a.b.c();
   }
}
