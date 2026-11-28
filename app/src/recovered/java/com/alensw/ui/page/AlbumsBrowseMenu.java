package com.alensw.ui.page;

import android.view.Menu;
import android.view.View;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.menu.MenuBuilderCallback;
class AlbumsBrowseMenu implements MenuBuilderCallback {
   final AlbumsPage a;

   AlbumsBrowseMenu(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public boolean a(Menu var1, View var2) {
      GalleryMenu.a(this.a.C, com.alensw.PicFolder.R.menu.browse, var1);
      var1.setGroupCheckable(com.alensw.PicFolder.R.id.browse_mode, true, true);
      int var3 = 0;
      switch (this.a.s) {
         case 0:
            var3 = com.alensw.PicFolder.R.id.browse_default;
            break;
         case 1:
            var3 = com.alensw.PicFolder.R.id.browse_stack;
            break;
         case 2:
            var3 = com.alensw.PicFolder.R.id.browse_list;
            break;
         case 3:
            var3 = com.alensw.PicFolder.R.id.browse_grid;
      }

      if (var3 != 0) {
         var1.findItem(var3).setChecked(true);
      }

      var1.findItem(com.alensw.PicFolder.R.id.browse_explorer).setChecked(this.a.o);
      return true;
   }
}
