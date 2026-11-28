package com.alensw.ui.page;

import android.os.Process;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.alensw.utils.GalleryUtils;
class MediaSlideshowAnimationListener implements AnimationListener {
   final MediaViewerPage a;
   private int b;

   MediaSlideshowAnimationListener(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      Process.setThreadPriority(this.b);
      MediaViewerPage.a(this.a, false);
      MediaViewerPage.e(this.a, true);
      if (MediaViewerPage.m(this.a)) {
         int var5 = MediaViewerPage.a(this.a).getWidth();
         int var4 = MediaViewerPage.a(this.a).getHeight();
         float var3 = GalleryUtils.a(var5);
         float var2 = GalleryUtils.a(var4);
         MediaViewerPage.a(this.a).b(MediaViewerPage.a(this.a).getTransScale() * 2.0F, var3, var2, 45000);
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
