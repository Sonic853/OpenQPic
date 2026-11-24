package com.alensw.ui.c;

import android.view.Menu;
import android.view.View;
class aa implements com.alensw.b.g.k {
   final com.alensw.ui.c.z a;

   aa(com.alensw.ui.c.z var1) {
      this.a = var1;
   }

   @Override
   public boolean a(Menu var1, View var2) {
      com.alensw.b.g.b.a(this.a.C, com.alensw.PicFolder.R.menu.browse, var1);
      var1.setGroupCheckable(com.alensw.PicFolder.R.id.browse_mode, true, true);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_default);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_stack);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_explorer);
      byte var4 = 0;
      int var3 = var4;
      switch (this.a.j) {
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
