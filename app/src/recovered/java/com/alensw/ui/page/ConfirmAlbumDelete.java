package com.alensw.ui.page;

class ConfirmAlbumDelete implements Runnable {
   final AlbumsPage a;

   ConfirmAlbumDelete(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.y();
   }
}
