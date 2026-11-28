package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;

class DeleteAlbumsDialog extends BackgroundProgressDialog {
   final ArrayList a;
   final GalleryMediaStore b;
   final ThumbnailDatabase c;
   final AlbumsPage e;

   DeleteAlbumsDialog(AlbumsPage var1, Activity var2, String var3, int var4, ArrayList var5, GalleryMediaStore var6, ThumbnailDatabase var7) {
      super(var2, var3, var4);
      this.e = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
   }

   @Override
   protected void a() {
        java.util.Iterator iterator = this.a.iterator();
        while (iterator.hasNext()) {
            ScannedFolder srcType0111 = (ScannedFolder)iterator.next();
            this.b.a(srcType0111.getPath());
            for (int i = srcType0111.j() - 1; i >= 0; --i) {
                ScannedMediaFile srcType0110 = srcType0111.e(i);
                if (srcType0110 == null) continue;
                File file = new File(srcType0111, srcType0110.b);
                String string2 = file.getPath();
                this.b.a(string2, srcType0110.c, false);
                GalleryUtils.c(file);
                srcType0111.h(i);
                this.c.a(string2);
                if (!this.d()) break;
            }
            srcType0111.o();
            ScannedFolder.a(srcType0111);
            if (!srcType0111.h()) continue;
            srcType0111.delete();
        }
        return;
    }

   @Override
   protected void b() {
      for (Object entry : this.a) {
         ScannedFolder var2 = (ScannedFolder) entry;
         if (var2.h()) {
            this.e.l.f(var2);
         } else {
            this.e.l.a(var2, true);
         }
      }

      if (this.e.C.q()) {
         this.e.C.d(false);
      }

      this.e.j.requestLayout();
   }
}
