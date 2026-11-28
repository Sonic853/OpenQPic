package com.alensw.ui.page;

import android.database.ContentObserver;
import android.os.Handler;

class MediaFolderObserver extends ContentObserver {
   final MediaFolderPage a;

   MediaFolderObserver(MediaFolderPage var1, Handler var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   public void onChange(boolean var1) {
      super.onChange(var1);
      this.a.b.removeCallbacks(MediaFolderPage.c(this.a));
      this.a.b.postDelayed(MediaFolderPage.c(this.a), 300L);
   }
}
