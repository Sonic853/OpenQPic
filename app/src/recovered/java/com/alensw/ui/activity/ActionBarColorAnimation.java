package com.alensw.ui.activity;

import android.view.View;
import android.view.animation.Interpolator;
import com.alensw.ui.view.ColorTransition;
class ActionBarColorAnimation extends ColorTransition {
   final int a;
   final GalleryChromeActivity b;

   ActionBarColorAnimation(GalleryChromeActivity var1, View var2, Interpolator var3, int var4, int var5, int var6) {
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
