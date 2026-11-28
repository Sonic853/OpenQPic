package com.alensw.ui.page;

import com.alensw.gallery.FolderScanFilters;
import com.alensw.gallery.ScannedFolder;
import com.alensw.ui.dialog.TextInputCallback;

class ExcludeAlbumPath implements TextInputCallback {
   final FolderScanFilters a;
   final Runnable b;
   final AlbumsPage c;

   ExcludeAlbumPath(AlbumsPage var1, FolderScanFilters var2, Runnable var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void a(String var1) {
      ScannedFolder var2 = new ScannedFolder(var1, -1);
      this.a.a(var2);
      this.c.l.f(var2);
      this.c.l.e(var2);
      this.b.run();
   }
}
