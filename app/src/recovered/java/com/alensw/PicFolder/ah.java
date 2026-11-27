package com.alensw.PicFolder;

import android.view.View;
import android.view.Gravity;
import android.view.View.OnClickListener;

class ah implements OnClickListener {
   final GalleryActivity a;

   ah(GalleryActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      if (GalleryActivity.b(this.a).isDrawerOpen(Gravity.LEFT)) {
         GalleryActivity.b(this.a).closeDrawer(Gravity.LEFT);
      }
   }
}
