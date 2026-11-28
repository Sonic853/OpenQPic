package com.alensw.ui.page;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class HideMediaPictureView implements AnimationListener {
   final MediaViewerPage a;

   HideMediaPictureView(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      MediaViewerPage.a(this.a).setVisibility(8);
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
