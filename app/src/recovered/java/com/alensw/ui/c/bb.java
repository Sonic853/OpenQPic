package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
import java.util.HashSet;

class bb implements com.alensw.ui.c.n {
   final com.alensw.ui.c.as a;

   bb(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   @Override
   public void a(boolean var1) {
      if (!var1) {
         com.alensw.ui.c.as.c(this.a);
      }
   }

   @Override
   public void a(boolean var1, HashSet var2) {
      if (!var1 && var2.size() > 0) {
         QuickApp.u.a(com.alensw.ui.c.as.d(this.a).i(), true);
         com.alensw.ui.c.as.e(this.a);
      }
   }
}
