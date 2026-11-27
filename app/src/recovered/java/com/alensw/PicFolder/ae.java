package com.alensw.PicFolder;

import android.view.Gravity;

class ae implements Runnable {
   final GalleryActivity a;

   ae(GalleryActivity var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (!this.a.isDestroyed() && !this.a.isFinishing()
            && GalleryActivity.b(this.a).getDrawerLockMode(Gravity.LEFT) == androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED) {
         GalleryActivity.b(this.a).openDrawer(Gravity.LEFT);
      }
   }
}
