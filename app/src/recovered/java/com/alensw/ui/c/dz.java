package com.alensw.ui.c;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class dz implements AnimationListener {
   final com.alensw.ui.c.dp a;

   dz(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      com.alensw.ui.c.dp.h(this.a).setVisibility(4);
      com.alensw.ui.c.dp.a(this.a, false);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
