package com.alensw.PicFolder;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
class aw implements OnMenuItemClickListener {
   final int a;
   final com.alensw.PicFolder.av b;

   aw(com.alensw.PicFolder.av var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      if (this.b.a.l != null) {
         try {
            this.b.a.l.selectTrack(this.a);
         } catch (Throwable var2) {
            com.alensw.b.l.b.a(this.b.a, var2);
         }
      }

      return true;
   }
}
