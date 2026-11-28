package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;

class RefreshHiddenAlbum implements Runnable {
   final ConfirmHideAlbum a;

   RefreshHiddenAlbum(ConfirmHideAlbum var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (!QuickApp.q.b) {
         this.a.b.l.f(this.a.a);
         this.a.b.l.a(true);
         if (this.a.b.C.q()) {
            this.a.b.C.d(false);
         }
      }

      this.a.b.j.requestLayout();
   }
}
