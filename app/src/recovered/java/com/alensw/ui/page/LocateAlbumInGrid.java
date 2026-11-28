package com.alensw.ui.page;

import com.alensw.gallery.ScannedFolder;

class LocateAlbumInGrid implements Runnable {
   final ScannedFolder a;
   final AlbumsPage b;

   LocateAlbumInGrid(AlbumsPage var1, ScannedFolder var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      int var1 = this.b.l.a(this.a);
      if (var1 >= 0) {
         this.b.j.b(var1);
      } else {
         this.b.a(this.a, new PublishNewAlbum(this));
      }
   }
}
