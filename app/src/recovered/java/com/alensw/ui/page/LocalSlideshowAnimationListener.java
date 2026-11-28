package com.alensw.ui.page;

import android.os.Process;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.alensw.utils.GalleryUtils;
class LocalSlideshowAnimationListener implements AnimationListener {
   final LocalViewerPage a;
   private int b;

   LocalSlideshowAnimationListener(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      Process.setThreadPriority(this.b);
      LocalViewerPage.a(this.a, false);
      LocalViewerPage.e(this.a, true);
      if (LocalViewerPage.q(this.a)) {
         int var4 = LocalViewerPage.e(this.a).getWidth();
         int var5 = LocalViewerPage.e(this.a).getHeight();
         float var2 = GalleryUtils.a(var4);
         float var3 = GalleryUtils.a(var5);
         LocalViewerPage.e(this.a).b(LocalViewerPage.e(this.a).getTransScale() * 2.0F, var2, var3, 45000);
      }
   }

   @Override
   public void onAnimationRepeat(Animation var1) {
   }

   @Override
   public void onAnimationStart(Animation var1) {
      this.b = Process.getThreadPriority(Process.myTid());
      Process.setThreadPriority(-4);
   }
}
