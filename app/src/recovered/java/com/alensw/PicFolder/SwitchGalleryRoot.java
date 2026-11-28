package com.alensw.PicFolder;

import android.content.Intent;
import com.alensw.bean.CommonRoot;
import com.alensw.ui.page.AlbumsPage;
import com.alensw.ui.page.MediaFolderPage;
class SwitchGalleryRoot implements Runnable {
   final int a;
   final CommonRoot b;
   final DrawerItemClick c;

   SwitchGalleryRoot(DrawerItemClick var1, int var2, CommonRoot var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      GalleryActivity.e(this.c.a);
      if (GalleryActivity.a(this.c.a).c != this.a) {
         QuickApp.v.c();
         QuickApp.w.c();
      }

      if ("folders".equals(this.b.n)) {
         this.c.a.a(AlbumsPage.class, new Intent(), null);
      } else {
         this.c.a.a(MediaFolderPage.class, MediaFolderPage.a(this.b.k(), true, this.b.n), null);
      }

      GalleryActivity.a(this.c.a).a(this.a);
   }
}
