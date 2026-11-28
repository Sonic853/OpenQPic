package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.dialog.BackgroundProgressDialog;
class ClearRenamedAlbumThumbnails extends BackgroundProgressDialog {
   final RenameAlbum a;

   ClearRenamedAlbumThumbnails(RenameAlbum var1, Activity var2, String var3, int var4) {
      super(var2, var3, var4);
      this.a = var1;
   }

   @Override
   protected void a() {
      QuickApp.r.b(this.a.a.getPath());
   }

   @Override
   protected void b() {
      QuickApp.b(true);
   }
}
