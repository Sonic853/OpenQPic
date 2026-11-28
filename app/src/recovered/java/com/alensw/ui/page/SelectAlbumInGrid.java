package com.alensw.ui.page;

class SelectAlbumInGrid implements Runnable {
   final int a;
   final AlbumsPage b;

   SelectAlbumInGrid(AlbumsPage var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.v = this.a;
      this.b.j.b(this.b.v, true);
   }
}
