package com.alensw.ui.c;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class bc implements AnimationListener {
   final com.alensw.ui.c.as a;

   bc(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      com.alensw.ui.c.as.f(this.a).setVisibility(4);
      com.alensw.ui.c.as.a(this.a, false);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
