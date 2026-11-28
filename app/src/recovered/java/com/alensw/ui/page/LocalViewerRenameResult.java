package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.utils.GalleryUtils;
class LocalViewerRenameResult implements MediaRenameCallback {
   final LocalViewerPage a;

   LocalViewerRenameResult(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1, String var2, int var3) {
      QuickApp.u.a(LocalViewerPage.b(this.a), true);
      LocalViewerPage.a(this.a).c = var3;
      LocalViewerPage.a(this.a, LocalViewerPage.a(this.a).d(var3));
      LocalViewerPage.e(this.a).setPictureUri(LocalViewerPage.b(this.a));
      if (LocalViewerPage.i(this.a) != null) {
         LocalViewerPage.i(this.a).a(LocalViewerPage.b(this.a));
      }

      this.a.b((CharSequence) GalleryUtils.c(var2));
   }
}
