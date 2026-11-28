package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;

class SetHiddenAlbumsVisible implements Runnable {
   final boolean a;
   final AlbumsPage b;

   SetHiddenAlbumsVisible(AlbumsPage var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      QuickApp.q.b = this.a;
      this.b.a(false);
   }
}
