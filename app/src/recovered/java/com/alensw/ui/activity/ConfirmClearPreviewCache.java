package com.alensw.ui.activity;

class ConfirmClearPreviewCache implements Runnable {
   final MainPreference a;

   ConfirmClearPreviewCache(MainPreference var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      new ClearPreviewCacheDialog(this, this.a, this.a.getString(com.alensw.PicFolder.R.string.running_wait), 0).c();
   }
}
