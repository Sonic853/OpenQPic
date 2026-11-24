package com.alensw.PicFolder;

class ab implements Runnable {
   final GalleryActivity a;

   ab(GalleryActivity var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.d() == 0) {
         this.a.finish();
      }
   }
}
