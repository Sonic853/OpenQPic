package com.alensw.ui.page;

import com.alensw.gallery.ScannedFolder;

class ConfirmHideAlbum implements Runnable {
   final ScannedFolder a;
   final AlbumsPage b;

   ConfirmHideAlbum(AlbumsPage var1, ScannedFolder var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.a(this.a, true, new RefreshHiddenAlbum(this));
   }
}
