package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.FolderCoverBitmap;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ThumbnailCallback;
import com.alensw.gallery.ThumbnailLoader;
import com.alensw.image.PictureBitmap;
import java.util.concurrent.atomic.AtomicReference;

class LoadStackedAlbumCover implements ThumbnailCallback, Runnable {
   final AlbumsPage a;
   private final AtomicReference b;
   private final FolderCoverBitmap c;
   private final ScannedFolder d;
   private final int e;
   private int f;

   public LoadStackedAlbumCover(AlbumsPage var1, ScannedFolder var2, FolderCoverBitmap var3, int var4, int var5) {
      this.a = var1;
      this.b = new AtomicReference();
      this.c = var3;
      this.d = var2;
      this.e = var4;
      this.f = var5;
   }

   @Override
   public void a(Object var1, PictureBitmap var2) {
      this.b.set(var2.l());
      this.a.j.a(this);
   }

   @Override
   public void run() {
      PictureBitmap var2 = (PictureBitmap)this.b.getAndSet(null);
      if (this.a.H == 2 && this.c.c()) {
         this.c.a(this.f, var2);
         this.a.j.d(this.a.l.b(this.d));
         if (this.f + 1 < Math.min(this.e, this.d.j())) {
            ThumbnailLoader var4 = QuickApp.t;
            ScannedFolder var3 = this.d;
            int var1 = this.f + 1;
            this.f = var1;
            var4.a(var3.e(var1), false, this);
         }
      }

      if (var2 != null) {
         var2.m();
      }
   }
}
