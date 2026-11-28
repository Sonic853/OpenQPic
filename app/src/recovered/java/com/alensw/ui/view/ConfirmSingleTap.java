package com.alensw.ui.view;

class ConfirmSingleTap implements Runnable {
   final PictureGestureDetector a;

   ConfirmSingleTap(PictureGestureDetector var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      PictureGestureDetector.a(this.a, false);
      if (!PictureGestureDetector.a(this.a) && PictureGestureDetector.b(this.a) == 1) {
         PictureGestureDetector.d(this.a).b(PictureGestureDetector.c(this.a).x, PictureGestureDetector.c(this.a).y);
      }
   }
}
