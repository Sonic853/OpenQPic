package com.alensw.ui.c;

import android.os.Process;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
class dr implements AnimationListener {
   final com.alensw.ui.c.dp a;
   private int b;

   dr(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void onAnimationEnd(Animation var1) {
      Process.setThreadPriority(this.b);
      com.alensw.ui.c.dp.a(this.a, false);
      com.alensw.ui.c.dp.e(this.a, true);
      if (com.alensw.ui.c.dp.q(this.a)) {
         int var4 = com.alensw.ui.c.dp.e(this.a).getWidth();
         int var5 = com.alensw.ui.c.dp.e(this.a).getHeight();
         float var2 = com.alensw.b.l.b.a(var4);
         float var3 = com.alensw.b.l.b.a(var5);
         com.alensw.ui.c.dp.e(this.a).b(com.alensw.ui.c.dp.e(this.a).getTransScale() * 2.0F, var2, var3, 45000);
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
