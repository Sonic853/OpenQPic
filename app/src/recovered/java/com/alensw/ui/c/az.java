package com.alensw.ui.c;

import com.alensw.bean.CommonFile;

class az implements com.alensw.ui.c.DateChangedCallback {
   final CommonFile a;
   final com.alensw.ui.c.as b;

   az(com.alensw.ui.c.as var1, CommonFile var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      this.a.a(var1 / 1000L, var3 / 1000L);
      if (com.alensw.ui.c.as.b(this.b).m()) {
         com.alensw.ui.c.as.b(this.b).s();
      }
   }
}
