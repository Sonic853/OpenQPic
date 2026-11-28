package com.alensw.ui.page;

import android.widget.Toast;
import com.alensw.gallery.ScannedFolder;
import com.alensw.media.MediaFileTypes;
import com.alensw.utils.GalleryUtils;
import java.io.File;

class OpenLoadedLocalPicture implements Runnable {
   final boolean a;
   final LocalViewerPage b;

   OpenLoadedLocalPicture(LocalViewerPage var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (LocalViewerPage.a(this.b).j() == 0) {
         String var3 = LocalViewerPage.b(this.b).getPath();
         if (var3 == null) {
            Toast.makeText(this.b.C, com.alensw.PicFolder.R.string.no_picture_in_album, 1).show();
            this.b.E();
         }

         File var2 = new File(var3);
         char var1 = MediaFileTypes.a(var3);
         ScannedFolder var4 = LocalViewerPage.a(this.b);
         if (var1 == 0) {
            var1 = 'I';
         }

         var4.a(var1, (int)(var2.lastModified() / 1000L), var2.length(), GalleryUtils.c(var3));
      }

      if (this.b.H == 2) {
         if (this.a) {
            LocalViewerPage.c(this.b);
         } else {
            LocalViewerPage.d(this.b);
         }
      }
   }
}
