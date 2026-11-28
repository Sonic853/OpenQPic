package com.alensw.ui.page;

class ConfirmFixFileDates implements Runnable {
   final LocalFolderPage a;

   ConfirmFixFileDates(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.n();
   }
}
