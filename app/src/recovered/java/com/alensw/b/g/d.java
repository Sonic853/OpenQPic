package com.alensw.b.g;

import android.app.Activity;

class d implements com.alensw.b.g.j {
   final com.alensw.b.g.b a;

   d(com.alensw.b.g.b var1) {
      this.a = var1;
   }

   @Override
   public void a(com.alensw.b.g.l var1, int var2) {
      if (!var1.c() && this.a.d instanceof Activity) {
         ((Activity)this.a.d).onMenuItemSelected(0, var1);
      }
   }
}
