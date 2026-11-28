package com.alensw.ui.activity;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

class SelectPasswordMode implements OnMenuItemClickListener {
   final PasswordModePreference a;

   SelectPasswordMode(PasswordModePreference var1) {
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
