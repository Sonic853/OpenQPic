package com.alensw.PicFolder;

class v implements Runnable {
   final com.alensw.PicFolder.q a;

   v(com.alensw.PicFolder.q var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.e = true;
      this.a.a();
   }
}
