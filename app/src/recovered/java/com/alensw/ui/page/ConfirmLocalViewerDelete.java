package com.alensw.ui.page;

class ConfirmLocalViewerDelete implements Runnable {
   final LocalViewerPage a;

   ConfirmLocalViewerDelete(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.o();
   }
}
