package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

class DeleteLocalFilesDialog extends BackgroundProgressDialog {
   final HashSet a;
   final GalleryMediaStore b;
   final ThumbnailDatabase c;
   final ArrayList e;
   final LocalFolderPage f;

   DeleteLocalFilesDialog(LocalFolderPage var1, Activity var2, String var3, int var4, HashSet var5, GalleryMediaStore var6, ThumbnailDatabase var7, ArrayList var8) {
      super(var2, var3, var4);
      this.f = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
   }

   @Override
   protected void a() {
      for (Object entry : this.a) {
         ScannedMediaFile var1 = (ScannedMediaFile) entry;
         File var4 = new File(this.f.g, var1.b);
         String var2 = var4.getPath();
         if (GalleryUtils.c(var4)) {
            this.b.a(var2, var1.c, false);
            this.c.a(var2);
            this.e.add(var1);
         }

         if (!this.d()) {
            break;
         }
      }
   }

   @Override
   protected void b() {
      if (!this.e.isEmpty()) {
         this.f.a(this.e);
         QuickApp.o.a(this.f.g, true);
      } else {
         this.f.a(GalleryUtils.e(this.f.g.getPath()));
      }

      this.f.o();
   }
}
