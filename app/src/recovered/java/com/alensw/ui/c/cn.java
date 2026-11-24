package com.alensw.ui.c;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
class cn extends com.alensw.ui.a.ao {
   final com.alensw.ui.c.cm a;

   cn(com.alensw.ui.c.cm var1, Activity var2, String var3, int var4) {
      super(var2, var3, var4);
      this.a = var1;
   }

   @Override
   protected void a() {
      QuickApp.r.b(this.a.a.getPath());
   }

   @Override
   protected void b() {
      QuickApp.b(true);
   }
}
