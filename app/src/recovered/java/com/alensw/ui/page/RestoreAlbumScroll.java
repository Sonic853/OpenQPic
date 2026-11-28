package com.alensw.ui.page;

class RestoreAlbumScroll implements Runnable {
   final AlbumsPage a;

   RestoreAlbumScroll(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.j.c(this.a.v);
   }
}
