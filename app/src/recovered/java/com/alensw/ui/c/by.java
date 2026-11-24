package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
class by implements com.alensw.ui.c.DateChangedCallback {
   final com.alensw.a.d a;
   final com.alensw.ui.c.bf b;

   by(com.alensw.ui.c.bf var1, com.alensw.a.d var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(long var1, long var3) {
      this.a.d = (int)(var1 / 1000L);
      this.a.e = (int)(var3 / 1000L);
      if (this.b.g.e()) {
         this.b.g.l();
         com.alensw.a.x var6 = QuickApp.o;
         com.alensw.a.e var7 = this.b.g;
         boolean var5;
         if (this.b.g.c < 4) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.a(var7, var5);
         this.b.f.requestLayout();
      }
   }
}
