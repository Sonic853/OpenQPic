package com.alensw.ui.page;

class ConfirmMediaFolderDelete implements Runnable {
   final MediaFolderPage a;

   ConfirmMediaFolderDelete(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.o();
   }
}
