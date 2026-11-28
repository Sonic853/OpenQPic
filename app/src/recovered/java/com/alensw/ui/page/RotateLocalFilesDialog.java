package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import com.alensw.utils.GalleryUtils;
import java.util.HashSet;

class RotateLocalFilesDialog extends BackgroundProgressDialog {
   final HashSet a;
   final int b;
   final LocalFolderPage c;

   RotateLocalFilesDialog(LocalFolderPage var1, Activity var2, String var3, int var4, HashSet var5, int var6) {
      super(var2, var3, var4);
      this.c = var1;
      this.a = var5;
      this.b = var6;
   }

   @Override
   protected void a() {
        int iA;
        for (Object entry : this.a) {
         ScannedMediaFile srcType0110 = (ScannedMediaFile) entry;
            String strA = srcType0110.a();
            if ((srcType0110.c == 'I' && strA.length() > 0) && (iA = GalleryUtils.a(strA, this.b, false)) != -1) {
                QuickApp.w.c(srcType0110);
                QuickApp.s.a(strA, iA);
                QuickApp.r.a(strA);
            }
            if (!d()) {
                return;
            }
        }
    }

   @Override
   protected void b() {
      this.c.f.requestLayout();
      QuickApp.o.a(this.c.g, true);
      this.c.o();
   }
}
