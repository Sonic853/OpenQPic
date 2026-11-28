package com.alensw.ui.page;

class ShowMediaSlideshowFrame implements Runnable {
   final MediaViewerPage a;

   ShowMediaSlideshowFrame(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      MediaViewerPage.n(this.a);
   }
}
