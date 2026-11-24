package com.alensw.ui.activity;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
class am extends com.alensw.ui.a.ao {
   final com.alensw.ui.activity.al a;

   am(com.alensw.ui.activity.al var1, Activity var2, String var3, int var4) {
      super(var2, var3, var4);
      this.a = var1;
   }

   @Override
   protected void a() {
      if (QuickApp.u != null) {
         QuickApp.u.c();
      }
   }
}
