package com.alensw.ui.page;

import android.view.Menu;
import android.view.View;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.menu.MenuBuilderCallback;
class LocalFolderBrowseMenu implements MenuBuilderCallback {
   final LocalFolderPage a;

   LocalFolderBrowseMenu(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public boolean a(Menu var1, View var2) {
      GalleryMenu.a(this.a.C, com.alensw.PicFolder.R.menu.browse, var1);
      var1.setGroupCheckable(com.alensw.PicFolder.R.id.browse_mode, true, true);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_default);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_stack);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_explorer);
      byte var4 = 0;
      int var3 = var4;
      switch (this.a.i) {
         case 0:
            var3 = com.alensw.PicFolder.R.id.browse_grid;
         case 1:
            break;
         case 2:
            var3 = com.alensw.PicFolder.R.id.browse_list;
            break;
         default:
            var3 = var4;
      }

      if (var3 != 0) {
         var1.findItem(var3).setChecked(true);
      }

      return true;
   }
}
