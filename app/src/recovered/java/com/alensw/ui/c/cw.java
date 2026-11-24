package com.alensw.ui.c;

import android.view.animation.Animation;

class cw implements Runnable {
   final Animation a;
   final com.alensw.ui.c.cu b;

   cw(com.alensw.ui.c.cu var1, Animation var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (!com.alensw.ui.c.cx.G) {
         this.b.D.setVisibility(8);
      }

      this.b.D.startAnimation(this.a);
   }
}
