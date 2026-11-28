package com.alensw.ui.page;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class HideLocalTransitionImage implements AnimationListener {
   final LocalViewerPage a;

   HideLocalTransitionImage(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      LocalViewerPage.h(this.a).setVisibility(4);
      LocalViewerPage.a(this.a, false);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
