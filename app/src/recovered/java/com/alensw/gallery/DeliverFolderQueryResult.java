package com.alensw.gallery;

import android.database.Cursor;
import com.alensw.bean.CommonFolder;

class DeliverFolderQueryResult implements Runnable {
   final Cursor a;
   final boolean b;
   final QueryFolderTask c;

   DeliverFolderQueryResult(QueryFolderTask var1, Cursor var2, boolean var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      boolean var2 = true;
      boolean var1;
      if (this.a == null && QueryFolderTask.c(this.c).n()) {
         var1 = false;
      } else {
         var1 = true;
      }

      FolderQueryCallback var4 = QueryFolderTask.e(this.c);
      CommonFolder var3;
      if (var1) {
         var3 = QueryFolderTask.c(this.c);
      } else {
         var3 = null;
      }

      if (this.b) {
         var2 = false;
      }

      var4.b(var3, var2);
   }
}
