package com.alensw.ui.page;

import com.alensw.ui.dialog.TextInputCallback;
import com.alensw.utils.GalleryUtils;
import java.io.File;

class SubmitNewAlbumName implements TextInputCallback {
   final AlbumsPage a;

   SubmitNewAlbumName(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = GalleryUtils.a(var1.trim(), '.');
      if (var1.length() != 0) {
         File var3 = new File(this.a.z, var1);
         this.a.b(var3);
      }
   }
}
