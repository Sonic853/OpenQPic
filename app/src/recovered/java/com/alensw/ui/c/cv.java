package com.alensw.ui.c;

import android.view.ViewTreeObserver.OnGlobalLayoutListener;

class cv implements OnGlobalLayoutListener {
   final com.alensw.ui.c.cu a;
   private int b;
   private int c;
   private int d;
   private int e;

   cv(com.alensw.ui.c.cu var1) {
      this.a = var1;
   }

   @Override
   public void onGlobalLayout() {
      int var1 = this.a.D.getLeft();
      int var2 = this.a.D.getRight();
      int var3 = this.a.D.getTop();
      int var4 = this.a.D.getBottom();
      if (this.b != var1 || this.c != var2 || this.d != var3 || this.e != var4) {
         this.b = var1;
         this.c = var2;
         this.d = var3;
         this.e = var4;
         this.a.a(var1, var3, var2, var4);
      }
   }
}
