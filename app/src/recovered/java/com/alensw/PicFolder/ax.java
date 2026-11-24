package com.alensw.PicFolder;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

class ax extends SimpleOnGestureListener {
   final PlayerView a;
   private int b;

   ax(PlayerView var1) {
      this.a = var1;
   }

   @Override
   public boolean onDoubleTap(MotionEvent var1) {
      PlayerView.b(this.a);
      return true;
   }

   @Override
   public boolean onDown(MotionEvent var1) {
      float var2 = var1.getY();
      int var3 = this.a.getHeight();
      boolean var4;
      if (var2 > PlayerView.a(this.a) && var2 < var3 - PlayerView.a(this.a)) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   @Override
   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      boolean var11 = false;
      boolean var10;
      if (PlayerView.c(this.a) == null) {
         var10 = var11;
      } else {
         var3 = var2.getX() - var1.getX();
         var4 = var1.getY() - var2.getY();
         int var8 = PlayerView.d(this.a);
         int var7 = var8;
         if (var8 == 0) {
            float var6 = Math.abs(var3);
            float var5 = Math.abs(var4);
            if (var6 >= var5 * 3.0F) {
               var7 = 1;
            } else {
               var7 = var8;
               if (var5 >= var6 * 3.0F) {
                  var7 = 2;
               }
            }
         }

         if (var7 == 1 && PlayerView.e(this.a) != null) {
            int var21 = PlayerView.f(this.a).getDuration();
            if (var21 <= 0) {
               return var11;
            }

            var7 = PlayerView.e(this.a).getWidth() - PlayerView.e(this.a).getPaddingLeft() - PlayerView.e(this.a).getPaddingRight();
            if (var7 <= 0) {
               return var11;
            }

            if (PlayerView.d(this.a) == 0) {
               PlayerView.a(this.a, 1);
               this.b = PlayerView.e(this.a).getProgress();
               PlayerView.c(this.a).onStartTrackingTouch(PlayerView.e(this.a));
            } else if (PlayerView.d(this.a) == 1) {
               var8 = PlayerView.e(this.a).getMax();
               var4 = (float)var8 * Math.min(var21, 120000) / var21;
               var21 = this.b;
               var7 = Math.max(Math.min(Math.round(var4 * (var3 / var7)) + var21, var8), 0);
               PlayerView.e(this.a).setProgress(var7);
               PlayerView.c(this.a).onProgressChanged(PlayerView.e(this.a), var7, true);
            }
         } else if (var7 == 2 && PlayerView.g(this.a) != null) {
            int var9 = PlayerView.g(this.a).getHeight() - PlayerView.g(this.a).getPaddingTop() - PlayerView.g(this.a).getPaddingBottom();
            if (var9 <= 0) {
               return var11;
            }

            if (PlayerView.d(this.a) == 0) {
               PlayerView.a(this.a, 2);
               this.b = PlayerView.g(this.a).getProgress();
               PlayerView.c(this.a).onStartTrackingTouch(PlayerView.g(this.a));
            } else if (PlayerView.d(this.a) == 2) {
               var7 = PlayerView.g(this.a).getMax();
               var8 = this.b;
               var7 = Math.max(Math.min(Math.round(var4 / var9 * var7) + var8, var7), 0);
               PlayerView.g(this.a).setProgress(var7);
               PlayerView.c(this.a).onProgressChanged(PlayerView.g(this.a), var7, true);
            }
         }

         var10 = true;
      }

      return var10;
   }

   @Override
   public boolean onSingleTapConfirmed(MotionEvent var1) {
      ((PlayerActivity)this.a.getContext()).v();
      return true;
   }
}
