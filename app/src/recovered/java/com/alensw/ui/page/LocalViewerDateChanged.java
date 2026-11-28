package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryFolders;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ScannedMediaFile;
class LocalViewerDateChanged implements DateChangedCallback {
   final ScannedMediaFile a;
   final LocalViewerPage b;

   LocalViewerDateChanged(LocalViewerPage var1, ScannedMediaFile var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      if (this.a != null) {
         this.a.d = (int)(var1 / 1000L);
         this.a.e = (int)(var3 / 1000L);
      }

      if (LocalViewerPage.a(this.b) != null && LocalViewerPage.a(this.b).e()) {
         LocalViewerPage.a(this.b).l();
         GalleryFolders var6 = QuickApp.o;
         ScannedFolder var7 = LocalViewerPage.a(this.b);
         boolean var5;
         if (LocalViewerPage.a(this.b).c < 4) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.a(var7, var5);
      }
   }
}
