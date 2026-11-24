package com.alensw.PicFolder;

class ap implements Runnable {
   final com.alensw.PicFolder.ao a;

   ap(com.alensw.PicFolder.ao var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.d();
   }
}
