package com.alensw.ui.view;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
class bv implements OnClickListener {
   final TitleBar a;

   bv(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      Activity var2 = (Activity)var1.getContext();
      if (this.a.d()) {
         MenuItem var3 = TitleBar.a(this.a).findItem(var1.getId());
         if (var3 != null) {
            com.alensw.b.g.m var4 = (com.alensw.b.g.m)var3.getSubMenu();
            if (var4 != null && var4.size() != 0) {
               var4.a(var1);
               return;
            }
         }
      }

      var2.onOptionsItemSelected(com.alensw.b.g.l.a(var1));
   }
}
