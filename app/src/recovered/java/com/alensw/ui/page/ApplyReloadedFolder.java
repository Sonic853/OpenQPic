package com.alensw.ui.page;

class ApplyReloadedFolder implements Runnable {
   final ReloadFolderContents a;

   ApplyReloadedFolder(ReloadFolderContents var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.a(this.a.a, true);
      this.a.d.c(false);
      if (this.a.c != null) {
         this.a.c.run();
      }
   }
}
