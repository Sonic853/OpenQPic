package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.ScannedFolder;
import com.alensw.ui.dialog.BackgroundProgressDialog;
class ClearLocalFolderThumbnails extends BackgroundProgressDialog {
   final ScannedFolder a;
   final LocalFolderPage b;

   ClearLocalFolderThumbnails(LocalFolderPage var1, Activity var2, String var3, int var4, ScannedFolder var5) {
      super(var2, var3, var4);
      this.b = var1;
      this.a = var5;
   }

   @Override
   protected void a() {
      QuickApp.r.b(this.a.getPath());
   }

   @Override
   protected void b() {
      QuickApp.b(true);
      this.b.o();
   }
}
