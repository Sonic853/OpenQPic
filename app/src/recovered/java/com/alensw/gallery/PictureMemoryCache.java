package com.alensw.gallery;

import com.alensw.cache.ConcurrentLruCache;
import com.alensw.cache.LruEntry;
import com.alensw.image.Picture;

class PictureMemoryCache extends ConcurrentLruCache {
   final PictureLoader a;

   PictureMemoryCache(PictureLoader var1, int var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   protected void a(LruEntry var1) {
      this.a((Picture)var1);
   }

   protected void a(Picture var1) {
      var1.m();
   }
}
