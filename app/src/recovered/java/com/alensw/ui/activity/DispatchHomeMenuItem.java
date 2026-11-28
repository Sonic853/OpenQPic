package com.alensw.ui.activity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import com.alensw.ui.menu.GalleryMenuItem;
final class DispatchHomeMenuItem implements OnClickListener {
   final Activity a;

   DispatchHomeMenuItem(Activity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.onMenuItemSelected(0, new GalleryMenuItem(null, var1.getContext(), 0, 16908332, null));
   }
}
