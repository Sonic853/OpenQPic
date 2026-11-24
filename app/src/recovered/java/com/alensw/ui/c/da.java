package com.alensw.ui.c;

import android.graphics.Rect;
import android.view.View;
class da implements com.alensw.ui.view.u {
   final com.alensw.ui.c.cx a;

   da(com.alensw.ui.c.cx var1) {
      this.a = var1;
   }

   @Override
   public void a(Rect var1) {
      if (!this.a.d.equals(var1)) {
         this.a.d.set(var1);

         for (Object entry : com.alensw.ui.c.cx.b(this.a)) {
         com.alensw.ui.c.cu var6 = (com.alensw.ui.c.cu) entry;
            View var5 = var6.D;
            int var3 = var5.getLeft();
            int var2 = var5.getRight();
            var6.a(var3, var5.getTop(), var2, var5.getBottom());
         }
      }
   }
}
