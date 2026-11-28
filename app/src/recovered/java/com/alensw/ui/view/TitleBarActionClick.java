package com.alensw.ui.view;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.alensw.ui.menu.GalleryMenuItem;
import com.alensw.ui.menu.GallerySubMenu;
class TitleBarActionClick implements OnClickListener {
   final TitleBar a;

   TitleBarActionClick(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      Activity var2 = (Activity)var1.getContext();
      if (this.a.d()) {
         MenuItem var3 = TitleBar.a(this.a).findItem(var1.getId());
         if (var3 != null) {
            GallerySubMenu var4 = (GallerySubMenu)var3.getSubMenu();
            if (var4 != null && var4.size() != 0) {
               var4.a(var1);
               return;
            }
         }
      }

      var2.onOptionsItemSelected(GalleryMenuItem.a(var1));
   }
}
