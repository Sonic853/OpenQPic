package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
import java.util.HashSet;

class dy implements com.alensw.ui.c.n {
   final com.alensw.ui.c.dp a;

   dy(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void a(boolean var1) {
      if (!var1) {
         com.alensw.ui.c.dp.f(this.a);
      }
   }

   @Override
   public void a(boolean var1, HashSet var2) {
      if (!var1 && var2.size() > 0) {
         QuickApp.u.a(com.alensw.ui.c.dp.b(this.a), true);
         com.alensw.ui.c.dp.g(this.a);
      }
   }
}
