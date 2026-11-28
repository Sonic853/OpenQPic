package com.alensw.ui.page;

import com.alensw.ui.dialog.TextInputCallback;
import com.alensw.utils.GalleryUtils;

class SubmitDocumentFolderName implements TextInputCallback {
   final MediaFolderPage a;

   SubmitDocumentFolderName(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = GalleryUtils.a(var1.trim(), '.');
      if (var1.length() != 0) {
         MediaFolderPage.a(this.a, var1);
      }
   }
}
