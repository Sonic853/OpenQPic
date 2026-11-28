package com.alensw.ui.view;

class ResetScrollDrawableState implements Runnable {
   final ScrollableGalleryView a;

   ResetScrollDrawableState(ScrollableGalleryView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.m.isStateful()) {
         this.a.m.setState(ScrollableGalleryView.f);
      }
   }
}
