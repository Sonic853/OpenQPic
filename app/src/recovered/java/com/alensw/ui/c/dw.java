package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
class dw implements com.alensw.ui.c.DateChangedCallback {
   final com.alensw.a.d a;
   final com.alensw.ui.c.dp b;

   dw(com.alensw.ui.c.dp var1, com.alensw.a.d var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      if (this.a != null) {
         this.a.d = (int)(var1 / 1000L);
         this.a.e = (int)(var3 / 1000L);
      }

      if (com.alensw.ui.c.dp.a(this.b) != null && com.alensw.ui.c.dp.a(this.b).e()) {
         com.alensw.ui.c.dp.a(this.b).l();
         com.alensw.a.x var6 = QuickApp.o;
         com.alensw.a.e var7 = com.alensw.ui.c.dp.a(this.b);
         boolean var5;
         if (com.alensw.ui.c.dp.a(this.b).c < 4) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.a(var7, var5);
      }
   }
}
