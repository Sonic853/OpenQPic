package com.alensw.ui.page;

import com.alensw.bean.CommonFile;

class MediaFolderDateChanged implements DateChangedCallback {
   final CommonFile a;
   final MediaFolderPage b;

   MediaFolderDateChanged(MediaFolderPage var1, CommonFile var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      this.a.a(var1 / 1000L, var3 / 1000L);
      if (this.b.y.m()) {
         this.b.y.s();
         this.b.f.requestLayout();
      }
   }
}
