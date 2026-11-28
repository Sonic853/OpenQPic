package com.alensw.ui.page;

import com.alensw.gallery.ScannedMediaFile;

class LocalFolderRenameResult implements MediaRenameCallback {
   final LocalFolderPage a;

   LocalFolderRenameResult(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1, String var2, int var3) {
      this.a.g.c = var3;
      if (this.a.f.d) {
         this.a.k.clear();
         ScannedMediaFile var4 = this.a.g.e(var3);
         if (var4 != null) {
            this.a.k.add(var4);
         }
      }

      this.a.f.requestLayout();
      this.a.f.b(var3);
   }
}
