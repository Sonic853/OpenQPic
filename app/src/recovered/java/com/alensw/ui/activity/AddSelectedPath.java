package com.alensw.ui.activity;

import com.alensw.sort.NameSortOrder;
import com.alensw.ui.dialog.FolderSelectionCallback;
import com.alensw.utils.GalleryUtils;
import java.io.File;

class AddSelectedPath implements FolderSelectionCallback {
   final PathListActivity a;

   AddSelectedPath(PathListActivity var1) {
      this.a = var1;
   }

   @Override
   public void a(File var1) {
      String var3 = var1.getPath();
      int var2 = GalleryUtils.a(this.a.a, var3, NameSortOrder.a);
      if (var2 < 0) {
         var2 = -var2;
         this.a.a.add(var2 - 1, var3);
         this.a.d = true;
         this.a.b.notifyDataSetChanged();
      }
   }
}
