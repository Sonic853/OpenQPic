package com.alensw.ui.activity;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

final class k implements OnMenuItemClickListener {
   final Runnable a;

   k(Runnable var1) {
      this.a = var1;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      if (var1.getItemId() == com.alensw.PicFolder.R.id.delete) {
         this.a.run();
      }

      return true;
   }
}
