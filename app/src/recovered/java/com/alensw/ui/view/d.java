package com.alensw.ui.view;

import android.view.View;
import android.view.animation.Interpolator;

class d extends com.alensw.ui.view.bt {
   final com.alensw.ui.view.a a;

   d(com.alensw.ui.view.a var1, View var2, Interpolator var3) {
      super(var2, var3);
      this.a = var1;
   }

   @Override
   public void a(float var1) {
      int var3 = (int)(this.a.c * var1);
      int var2 = var3;
      if (!com.alensw.ui.view.a.a(this.a)) {
         var2 = this.a.c - var3;
      }

      this.a.b(var2);
   }

   @Override
   public void b() {
      com.alensw.ui.view.a.b(this.a).run();
   }
}
