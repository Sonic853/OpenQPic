package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.cache.LruEntry;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ThumbnailCallback;
import com.alensw.image.PictureBitmap;
class BindAlbumCover implements ThumbnailCallback {
   final ScannedFolder a;
   final AlbumsPage b;

   BindAlbumCover(AlbumsPage var1, ScannedFolder var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(Object var1, PictureBitmap var2) {
       PictureBitmap cover = (PictureBitmap) var2.l();
       this.b.b.post(() -> {
          ScannedFolder current = this.b.l.b(this.b.l.a(this.a));
          if (this.b.H == 2 && current != null && current.a(this.a, 1)) {
             QuickApp.v.a(current, (LruEntry) cover);
             this.b.j.h();
          } else {
             cover.m();
             if (this.b.H == 2 && current != null) this.b.d(current);
          }
       });
   }
}
