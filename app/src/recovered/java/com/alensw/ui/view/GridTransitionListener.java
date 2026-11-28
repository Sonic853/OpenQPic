package com.alensw.ui.view;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class GridTransitionListener implements AnimationListener {
   final boolean a;
   final ImageGridView b;

   GridTransitionListener(ImageGridView var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      ImageGridView.a(this.b, null);
      if (this.a) {
         this.b.invalidate();
      } else {
         this.b.setVisibility(8);
      }
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
   }
}
