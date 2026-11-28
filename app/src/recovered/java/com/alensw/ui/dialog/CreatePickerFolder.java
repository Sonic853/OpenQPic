package com.alensw.ui.dialog;

import com.alensw.utils.GalleryUtils;
import java.io.File;

class CreatePickerFolder implements TextInputCallback {
   final PromptNewFolderName a;

   CreatePickerFolder(PromptNewFolderName var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      var1 = GalleryUtils.a(var1.trim(), '.');
      if (var1.length() != 0) {
         File var3 = this.a.c.a(var1);
         if (var3 != null) {
            this.a.b.a(var3);
         }
      }
   }
}
