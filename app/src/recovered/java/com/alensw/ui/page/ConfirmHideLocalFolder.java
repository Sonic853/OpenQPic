package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;

class ConfirmHideLocalFolder implements Runnable {
   final LocalFolderPage a;

   ConfirmHideLocalFolder(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(this.a.g, true, null);
      if (!QuickApp.q.b) {
         QuickApp.o.f(this.a.g);
         QuickApp.o.a(true);
      }
   }
}
