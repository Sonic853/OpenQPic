package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
class ea implements com.alensw.ui.c.o {
   final com.alensw.ui.c.dp a;

   ea(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1, String var2, int var3) {
      QuickApp.u.a(com.alensw.ui.c.dp.b(this.a), true);
      com.alensw.ui.c.dp.a(this.a).c = var3;
      com.alensw.ui.c.dp.a(this.a, com.alensw.ui.c.dp.a(this.a).d(var3));
      com.alensw.ui.c.dp.e(this.a).setPictureUri(com.alensw.ui.c.dp.b(this.a));
      if (com.alensw.ui.c.dp.i(this.a) != null) {
         com.alensw.ui.c.dp.i(this.a).a(com.alensw.ui.c.dp.b(this.a));
      }

      this.a.b((CharSequence) com.alensw.b.l.b.c(var2));
   }
}
