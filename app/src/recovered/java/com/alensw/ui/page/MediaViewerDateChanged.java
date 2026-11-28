package com.alensw.ui.page;

import com.alensw.bean.CommonFile;

class MediaViewerDateChanged implements DateChangedCallback {
   final CommonFile a;
   final MediaViewerPage b;

   MediaViewerDateChanged(MediaViewerPage var1, CommonFile var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      this.a.a(var1 / 1000L, var3 / 1000L);
      if (MediaViewerPage.b(this.b).m()) {
         MediaViewerPage.b(this.b).s();
      }
   }
}
