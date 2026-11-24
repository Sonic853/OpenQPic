package com.alensw.ui.activity;

import android.view.View;
import android.view.animation.Interpolator;
class aq extends com.alensw.ui.view.r {
   final int a;
   final com.alensw.ui.activity.ao b;

   aq(com.alensw.ui.activity.ao var1, View var2, Interpolator var3, int var4, int var5, int var6) {
      super(var2, var3, var4, var5);
      this.b = var1;
      this.a = var6;
   }

   @Override
   public void a(int var1) {
      this.b.g(var1);
   }

   @Override
   public void b() {
      this.b.g(this.a);
   }
}
