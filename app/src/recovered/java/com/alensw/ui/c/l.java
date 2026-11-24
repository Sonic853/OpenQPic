package com.alensw.ui.c;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
class l extends com.alensw.ui.a.ao {
   final String a;
   final com.alensw.ui.c.a b;

   l(com.alensw.ui.c.a var1, Activity var2, String var3, int var4, String var5) {
      super(var2, var3, var4);
      this.b = var1;
      this.a = var5;
   }

   @Override
   protected void a() {
      if (QuickApp.s.e(this.a) == 0) {
         QuickApp.b(true);
      }
   }
}
