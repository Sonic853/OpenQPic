package com.alensw.ui.view;

class FinishPictureSwipe implements Runnable {
   final int a;
   final ZoomablePictureView b;

   FinishPictureSwipe(ZoomablePictureView var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (ZoomablePictureView.a(this.b) != null) {
         ZoomablePictureView.a(this.b).m();
         ZoomablePictureView.a(this.b, null);
      }

      this.b.G = false;
      this.b.A = 0;
      this.b.invalidate();
      if (this.b.K != null) {
         this.b.K.b(this.a);
      }
   }
}
