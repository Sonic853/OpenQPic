package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryFolders;
import com.alensw.gallery.ScannedFolder;
import com.alensw.ui.dialog.TextInputCallback;
import com.alensw.utils.GalleryUtils;
class RenameLocalMediaFile implements TextInputCallback {
   final String a;
   final ScannedFolder b;
   final int c;
   final MediaRenameCallback d;
   final MediaActionPage e;

   RenameLocalMediaFile(MediaActionPage var1, String var2, ScannedFolder var3, int var4, MediaRenameCallback var5) {
      this.e = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   @Override
   public void a(String var1) {
      boolean var4 = false;
      String var5 = GalleryUtils.g(GalleryUtils.a(var1.trim(), '.'));
      if (var5.length() != 0 && !this.a.equals(var5)) {
         var1 = this.b.b(this.c);
         char var2 = this.b.f(this.c);
         int var3 = this.b.a(this.c, var5);
         if (var3 != -1) {
            String var7 = this.b.b(var3);
            QuickApp.s.a(var1, var2, false);
            QuickApp.s.f(var7);
            GalleryFolders var9 = QuickApp.o;
            ScannedFolder var6 = this.b;
            if (var3 < 4) {
               var4 = true;
            }

            var9.a(var6, var4);
            QuickApp.r.a(var1);
            this.d.a(var1, var7, var3);
         } else {
            this.e.a(var1);
         }
      }
   }
}
