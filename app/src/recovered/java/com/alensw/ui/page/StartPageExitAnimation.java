package com.alensw.ui.page;

import android.view.animation.Animation;

class StartPageExitAnimation implements Runnable {
   final Animation a;
   final GalleryPage b;

   StartPageExitAnimation(GalleryPage var1, Animation var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (!GalleryPageActivity.G) {
         this.b.D.setVisibility(8);
      }

      this.b.D.startAnimation(this.a);
   }
}
