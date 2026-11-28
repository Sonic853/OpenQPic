package com.alensw.PicFolder;

import android.view.ViewTreeObserver.OnGlobalLayoutListener;

class PlayerControlsLayoutListener implements OnGlobalLayoutListener {
   final PlayerActivity a;
   private int b;
   private int c;
   private int d;
   private int e;

   PlayerControlsLayoutListener(PlayerActivity var1) {
      this.a = var1;
   }

   @Override
   public void onGlobalLayout() {
      int var2 = PlayerActivity.a(this.a).getLeft();
      int var3 = PlayerActivity.b(this.a).getRight();
      int var4 = PlayerActivity.c(this.a).getTop();
      int var1 = PlayerActivity.d(this.a).getBottom();
      if (this.b != var2 || this.c != var3 || this.d != var4 || this.e != var1) {
         this.b = var2;
         this.c = var3;
         this.d = var4;
         this.e = var1;
         this.a.f();
      }
   }
}
