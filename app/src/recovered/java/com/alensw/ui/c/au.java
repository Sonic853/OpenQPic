package com.alensw.ui.c;

import android.os.Process;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
class au implements AnimationListener {
   final com.alensw.ui.c.as a;
   private int b;

   au(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      Process.setThreadPriority(this.b);
      com.alensw.ui.c.as.a(this.a, false);
      com.alensw.ui.c.as.e(this.a, true);
      if (com.alensw.ui.c.as.m(this.a)) {
         int var5 = com.alensw.ui.c.as.a(this.a).getWidth();
         int var4 = com.alensw.ui.c.as.a(this.a).getHeight();
         float var3 = com.alensw.b.l.b.a(var5);
         float var2 = com.alensw.b.l.b.a(var4);
         com.alensw.ui.c.as.a(this.a).b(com.alensw.ui.c.as.a(this.a).getTransScale() * 2.0F, var3, var2, 45000);
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
