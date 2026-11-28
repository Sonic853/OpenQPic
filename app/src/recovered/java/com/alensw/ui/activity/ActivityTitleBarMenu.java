package com.alensw.ui.activity;

import android.view.Menu;
import com.alensw.ui.view.TitleBar;
import com.alensw.ui.view.TitleBarMenuCallback;

class ActivityTitleBarMenu implements TitleBarMenuCallback {
   final GalleryChromeActivity a;

   ActivityTitleBarMenu(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public boolean a(TitleBar var1, Menu var2) {
      return this.a.c(var2);
   }

   @Override
   public boolean b(TitleBar var1, Menu var2) {
      var1.a(var2);
      return this.a.d(var2);
   }
}
