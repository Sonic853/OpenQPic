package com.alensw.bean;

import android.content.ContentResolver;
import com.alensw.gallery.GalleryMediaStore;
class NotifyMediaStoreChange implements Runnable {
   final ContentResolver a;
   final MediaStoreFolder b;

   NotifyMediaStoreChange(MediaStoreFolder var1, ContentResolver var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.a.notifyChange(GalleryMediaStore.a('I'), null);
   }
}
