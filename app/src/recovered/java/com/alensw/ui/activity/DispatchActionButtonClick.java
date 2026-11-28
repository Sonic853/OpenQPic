package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;
import com.alensw.ui.menu.GalleryMenuItem;
class DispatchActionButtonClick implements OnClickListener {
   final GalleryChromeActivity a;

   DispatchActionButtonClick(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.onMenuItemSelected(0, GalleryMenuItem.a(var1));
   }
}
