package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;
import com.alensw.ui.menu.GalleryMenuItem;
class DispatchTitleClick implements OnClickListener {
   final GalleryChromeActivity a;

   DispatchTitleClick(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      int var2 = var1.getId();
      var1.setId(16908319);
      this.a.onMenuItemSelected(0, GalleryMenuItem.a(var1));
      var1.setId(var2);
   }
}
