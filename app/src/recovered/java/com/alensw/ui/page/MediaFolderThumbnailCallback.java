package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.cache.LruEntry;
import com.alensw.gallery.ThumbnailCallback;
import com.alensw.image.PictureBitmap;
class MediaFolderThumbnailCallback implements ThumbnailCallback {
   final MediaFolderPage a;

   MediaFolderThumbnailCallback(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(Object var1, PictureBitmap var2) {
      if (this.a.H == 2) {
         QuickApp.w.a(var1, (LruEntry)var2.l());
         this.a.f.h();
      }
   }
}
