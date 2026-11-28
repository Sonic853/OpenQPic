package com.alensw.ui.page;

class ConfirmMediaViewerDelete implements Runnable {
   final MediaViewerPage a;

   ConfirmMediaViewerDelete(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.m();
   }
}
