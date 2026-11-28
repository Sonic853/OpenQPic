package com.alensw.ui.page;

class ShowLocalSlideshowFrame implements Runnable {
   final LocalViewerPage a;

   ShowLocalSlideshowFrame(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      LocalViewerPage.r(this.a);
   }
}
