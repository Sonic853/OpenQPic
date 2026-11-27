package com.alensw.ui.view;

import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

class bs implements OnGestureListener {
   final com.alensw.ui.view.bp a;

   bs(com.alensw.ui.view.bp var1) {
      this.a = var1;
   }

   @Override
   public boolean onDown(MotionEvent var1) {
      if (!this.a.p.isFinished()) {
         this.a.p.forceFinished(true);
      } else {
         this.a.a(var1);
      }

      return true;
   }

   @Override
   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      int var12 = this.a.getScrollX();
      int var9 = this.a.getScrollY();
      int var8 = -((int)var3);
      int var11 = -((int)var4);
      int var7 = this.a.getScrollRangeX();
      int var10 = this.a.getScrollRangeY();
      int var5;
      if (var7 <= 0 || (var8 > 0 ? var12 >= var7 : var12 <= 0)) {
         var5 = 0;
      } else {
         var5 = this.a.getClientWidth() / 2;
      }

      int var6;
      if (var10 <= 0 || (var11 > 0 ? var9 >= var10 : var9 <= 0)) {
         var6 = 0;
      } else {
         var6 = this.a.getClientHeight() / 2;
      }

      if (var5 != 0 || var6 != 0) {
         this.a.p.fling(var12, var9, var8, var11, 0, var7, 0, var10, var5, var6);
         this.a.g();
         this.a.b(var1, var2, var3, var4);
      }

      return true;
   }

   @Override
   public void onLongPress(MotionEvent var1) {
      this.a.d(var1);
   }

   @Override
   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      this.a.a((int)var3, (int)var4, this.a.getScrollX(), this.a.getScrollY(), this.a.i, this.a.i, true);
      this.a.a(var1, var2, var3, var4);
      return true;
   }

   @Override
   public void onShowPress(MotionEvent var1) {
      this.a.a(true);
      this.a.b(var1);
   }

   @Override
   public boolean onSingleTapUp(MotionEvent var1) {
      this.a.c(var1);
      return true;
   }
}
