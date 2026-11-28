package com.alensw.ui.page;

import com.alensw.gallery.ScannedFolder;
import com.alensw.ui.dialog.TextInputCallback;
import com.alensw.utils.GalleryUtils;

class RenameAlbum implements TextInputCallback {
   final ScannedFolder a;
   final AlbumsPage b;

   RenameAlbum(AlbumsPage var1, ScannedFolder var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(String var1) {
      var1 = GalleryUtils.g(GalleryUtils.a(var1.trim(), '.'));
      if (var1.length() != 0 && !this.a.getName().equals(var1)) {
         int var2 = this.b.l.a(this.a);
         var2 = this.b.l.a(var2, var1);
         if (var2 != -1) {
            ScannedFolder var4 = this.b.l.b(var2);
            if (this.b.C.q()) {
               this.b.A.clear();
               this.b.A.add(var4);
            }

            this.b.j.requestLayout();
            this.b.d(var4);
            new ClearRenamedAlbumThumbnails(this, this.b.C, this.b.g(com.alensw.PicFolder.R.string.running_wait), 0).c();
         } else {
            this.b.a(this.a.getPath());
         }
      }
   }
}
