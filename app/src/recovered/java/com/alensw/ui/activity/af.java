package com.alensw.ui.activity;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

class af implements OnMenuItemClickListener {
   final com.alensw.ui.activity.ae a;

   af(com.alensw.ui.activity.ae var1) {
      this.a = var1;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      MainPreference var3 = this.a.c;
      boolean var2;
      if (var1.getItemId() == 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.a(var2, this.a.a);
      return true;
   }
}
