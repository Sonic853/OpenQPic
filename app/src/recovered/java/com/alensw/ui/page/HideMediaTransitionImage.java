package com.alensw.ui.page;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class HideMediaTransitionImage implements AnimationListener {
   final MediaViewerPage a;

   HideMediaTransitionImage(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      MediaViewerPage.f(this.a).setVisibility(4);
      MediaViewerPage.a(this.a, false);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
