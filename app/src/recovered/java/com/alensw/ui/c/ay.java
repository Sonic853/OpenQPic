package com.alensw.ui.c;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class ay implements AnimationListener {
   final com.alensw.ui.c.as a;

   ay(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      com.alensw.ui.c.as.a(this.a).setVisibility(8);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
