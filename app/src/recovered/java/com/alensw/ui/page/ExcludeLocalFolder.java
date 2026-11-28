package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.FolderScanFilters;
import com.alensw.gallery.ScannedFolder;
import com.alensw.ui.dialog.GalleryDialogs;
import com.alensw.ui.dialog.TextInputCallback;
class ExcludeLocalFolder implements TextInputCallback {
   final LocalFolderPage a;

   ExcludeLocalFolder(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      ScannedFolder var2 = new ScannedFolder(var1, -1);
      FolderScanFilters.a(this.a.C).a(var2);
      QuickApp.o.e(var2);
      GalleryDialogs.a(this.a.C, 17039380, (CharSequence)this.a.g(com.alensw.PicFolder.R.string.excluded_tips));
      this.a.E();
   }
}
