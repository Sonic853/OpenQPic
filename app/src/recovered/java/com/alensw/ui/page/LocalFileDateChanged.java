package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryFolders;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ScannedMediaFile;
class LocalFileDateChanged implements DateChangedCallback {
   final ScannedMediaFile a;
   final LocalFolderPage b;

   LocalFileDateChanged(LocalFolderPage var1, ScannedMediaFile var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      this.a.d = (int)(var1 / 1000L);
      this.a.e = (int)(var3 / 1000L);
      if (this.b.g.e()) {
         this.b.g.l();
         GalleryFolders var6 = QuickApp.o;
         ScannedFolder var7 = this.b.g;
         boolean var5;
         if (this.b.g.c < 4) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.a(var7, var5);
         this.b.f.requestLayout();
      }
   }
}
