package com.alensw.image;

import com.alensw.cache.ConcurrentLruCache;
import com.alensw.cache.LruEntry;

class TileCache extends ConcurrentLruCache {
   final TiledPicture a;

   TileCache(TiledPicture var1, int var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   protected void a(LruEntry var1) {
      this.a((PictureTile)var1);
   }

   protected void a(PictureTile var1) {
      var1.m();
   }
}
