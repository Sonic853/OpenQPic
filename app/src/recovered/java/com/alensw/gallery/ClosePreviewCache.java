package com.alensw.gallery;

class ClosePreviewCache implements Runnable {
   final PictureLoader a;

   ClosePreviewCache(PictureLoader var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      PictureLoader.a(this.a).c();
   }
}
