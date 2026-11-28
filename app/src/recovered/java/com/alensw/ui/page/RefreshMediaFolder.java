package com.alensw.ui.page;

class RefreshMediaFolder implements Runnable {
   final MediaFolderPage a;

   RefreshMediaFolder(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      MediaFolderPage.b(this.a);
      if (this.a.r ? this.a.H == 0 : this.a.H != 2) {
         this.a.q = true;
      } else {
         this.a.a(false);
         this.a.n();
      }
   }
}
