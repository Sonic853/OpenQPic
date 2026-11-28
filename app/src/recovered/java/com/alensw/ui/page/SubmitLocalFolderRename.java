package com.alensw.ui.page;

import com.alensw.ui.dialog.TextInputCallback;
import com.alensw.utils.GalleryUtils;

class SubmitLocalFolderRename implements TextInputCallback {
   final LocalFolderPage a;

   SubmitLocalFolderRename(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = GalleryUtils.g(GalleryUtils.a(var1.trim(), '.'));
      if (var1.length() != 0 && !this.a.g.getName().equals(var1)) {
         this.a.f(var1);
      }
   }
}
