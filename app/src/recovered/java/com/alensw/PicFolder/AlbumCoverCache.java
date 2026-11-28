package com.alensw.PicFolder;

import com.alensw.cache.ConcurrentLruCache;
import com.alensw.cache.LruEntry;
import com.alensw.image.PictureBitmap;

class AlbumCoverCache extends ConcurrentLruCache {
   final QuickApp a;

   AlbumCoverCache(QuickApp var1, int var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   protected void a(LruEntry var1) {
      this.a((PictureBitmap)var1);
   }

   protected void a(PictureBitmap var1) {
      var1.m();
   }
}
