package com.alensw.ui.dialog;

class FinishProgressDialog implements Runnable {
   final ProgressDialogTask a;

   FinishProgressDialog(ProgressDialogTask var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      GalleryDialogs.b(this.a.a);
      this.a.a.b();
   }
}
