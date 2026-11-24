package com.alensw.PicFolder;

class w implements Runnable {
   final com.alensw.PicFolder.q a;

   w(com.alensw.PicFolder.q var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(0, false);
      this.a.e = true;
      this.a.a();
   }
}
