package com.alensw.ui.page;

class ConfirmLocalFileDelete implements Runnable {
   final LocalFolderPage a;

   ConfirmLocalFileDelete(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.p();
   }
}
