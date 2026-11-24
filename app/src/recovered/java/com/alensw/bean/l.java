package com.alensw.bean;

import android.content.ContentResolver;
class l implements Runnable {
   final ContentResolver a;
   final MediaStoreFolder b;

   l(MediaStoreFolder var1, ContentResolver var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.a.notifyChange(com.alensw.a.ba.a('I'), null);
   }
}
