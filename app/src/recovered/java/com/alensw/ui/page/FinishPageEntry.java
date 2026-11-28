package com.alensw.ui.page;

class FinishPageEntry implements Runnable {
   final GalleryPage a;
   final GalleryPageActivity b;

   FinishPageEntry(GalleryPageActivity var1, GalleryPage var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.a != null) {
         this.a.i();
      }
   }
}
