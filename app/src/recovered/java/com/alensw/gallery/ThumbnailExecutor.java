package com.alensw.gallery;

import com.alensw.concurrent.AsyncExecutor;

class ThumbnailExecutor extends AsyncExecutor {
   final ThumbnailLoader a;

   ThumbnailExecutor(ThumbnailLoader var1, int var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.a = var1;
   }

   @Override
   protected void a(Runnable var1) {
      super.a(var1);
      if (var1 instanceof LoadThumbnailTask) {
         ThumbnailLoader.a(this.a).remove(((LoadThumbnailTask)var1).b());
      }
   }

   @Override
   protected void afterExecute(Runnable var1, Throwable var2) {
      super.afterExecute(var1, var2);
      if (var1 instanceof LoadThumbnailTask) {
         ThumbnailLoader.a(this.a).remove(((LoadThumbnailTask)var1).b());
      }
   }
}
