package com.alensw.PicFolder;

class FinishEmptyGallery implements Runnable {
   final GalleryActivity a;

   FinishEmptyGallery(GalleryActivity var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.d() == 0) {
         this.a.finish();
      }
   }
}
