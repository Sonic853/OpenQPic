package com.alensw.PicFolder;

import android.content.ContentResolver;
class bb implements Runnable {
   final ContentResolver a;
   final QuickApp b;

   bb(QuickApp var1, ContentResolver var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      com.alensw.a.am.a(this.a);
   }
}
