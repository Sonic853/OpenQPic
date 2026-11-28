package com.alensw.ui.page;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class HideLocalPictureView implements AnimationListener {
   final LocalViewerPage a;

   HideLocalPictureView(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      LocalViewerPage.e(this.a).setVisibility(8);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
