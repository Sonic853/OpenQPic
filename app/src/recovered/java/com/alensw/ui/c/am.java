package com.alensw.ui.c;

import com.alensw.bean.CommonFile;

class am implements com.alensw.ui.c.DateChangedCallback {
   final CommonFile a;
   final com.alensw.ui.c.z b;

   am(com.alensw.ui.c.z var1, CommonFile var2) {
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
