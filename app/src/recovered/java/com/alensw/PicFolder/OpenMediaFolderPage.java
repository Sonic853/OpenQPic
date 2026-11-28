package com.alensw.PicFolder;

import android.content.Intent;
import com.alensw.ui.page.MediaFolderPage;
class OpenMediaFolderPage implements Runnable {
   final Intent a;
   final GalleryActivity b;

   OpenMediaFolderPage(GalleryActivity var1, Intent var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.a(MediaFolderPage.class, this.a, null);
   }
}
