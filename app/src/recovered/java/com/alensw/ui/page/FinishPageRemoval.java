package com.alensw.ui.page;

class FinishPageRemoval implements Runnable {
   final GalleryPage a;
   final GalleryPage b;
   final GalleryPageActivity c;

   FinishPageRemoval(GalleryPageActivity var1, GalleryPage var2, GalleryPage var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      this.a.I = false;
      this.a.e(0);
      GalleryPageActivity.a(this.c).removeView(this.a.D);
      if (this.b != null) {
         this.b.i();
      }
   }
}
