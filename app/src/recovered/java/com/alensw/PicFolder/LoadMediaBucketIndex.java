package com.alensw.PicFolder;

import android.content.ContentResolver;
import com.alensw.gallery.MediaBucketIndex;
class LoadMediaBucketIndex implements Runnable {
   final ContentResolver a;
   final QuickApp b;

   LoadMediaBucketIndex(QuickApp var1, ContentResolver var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      MediaBucketIndex.a(this.a);
   }
}
