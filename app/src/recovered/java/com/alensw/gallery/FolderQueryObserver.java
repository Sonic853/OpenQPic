package com.alensw.gallery;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;
import java.util.Arrays;

class FolderQueryObserver extends ContentObserver {
   final Object[] a;
   final QueryFolderTask b;

   FolderQueryObserver(QueryFolderTask var1, Handler var2, Object[] var3) {
      super(var2);
      this.b = var1;
      this.a = var3;
   }

   @Override
   public void onChange(boolean var1) {
      boolean var3 = true;
      boolean var2 = true;
      super.onChange(var1);
      QueryFolderTask.a(this.b).unregisterContentObserver(this);
      var1 = var3;
      if (QueryFolderTask.b(this.b)) {
         Object[] var4 = QueryFolderTask.c(this.b).a(QueryFolderTask.a(this.b));
         if (!Arrays.equals(this.a, var4)) {
            var1 = var2;
         } else {
            var1 = false;
         }

         Log.d(QueryFolderTask.d(this.b), "changed=" + var1);
      }

      QueryFolderTask.f(this.b).post(new DeliverFolderChange(this, var1));
   }
}
