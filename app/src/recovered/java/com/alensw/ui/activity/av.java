package com.alensw.ui.activity;

import android.view.Menu;
import com.alensw.ui.view.TitleBar;

class av implements com.alensw.ui.view.cb {
   final com.alensw.ui.activity.ao a;

   av(com.alensw.ui.activity.ao var1) {
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
