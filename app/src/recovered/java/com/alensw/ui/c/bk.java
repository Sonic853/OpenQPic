package com.alensw.ui.c;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
class bk extends com.alensw.ui.a.ao {
   final com.alensw.a.e a;
   final com.alensw.ui.c.bf b;

   bk(com.alensw.ui.c.bf var1, Activity var2, String var3, int var4, com.alensw.a.e var5) {
      super(var2, var3, var4);
      this.b = var1;
      this.a = var5;
   }

   @Override
   protected void a() {
      QuickApp.r.b(this.a.getPath());
   }

   @Override
   protected void b() {
      QuickApp.b(true);
      this.b.o();
   }
}
