package com.alensw.ui.activity;

class RecreateGalleryActivity implements Runnable {
   final GalleryIntentActivity a;

   RecreateGalleryActivity(GalleryIntentActivity var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.recreate();
   }
}
