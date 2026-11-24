package com.alensw.PicFolder;

class ae implements Runnable {
   final GalleryActivity a;

   ae(GalleryActivity var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (GalleryActivity.a(this.a).isEmpty()) {
         GalleryActivity.a(this.a).a();
         GalleryActivity.b(this.a).postDelayed(this, 700L);
      } else {
         GalleryActivity.b(this.a).c(3);
      }
   }
}
