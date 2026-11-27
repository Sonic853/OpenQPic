package com.alensw.ui.view;

import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;

class m extends com.alensw.ui.view.bt {
   final int b;
   final int c;
   final int d;
   final int e;
   final com.alensw.ui.view.j h;
   private final int i;
   private final int j;
   private float k;
   private float l;

   m(com.alensw.ui.view.j var1, View var2, Interpolator var3, RectF var4, int var5, int var6, int var7, int var8) {
      super(var2, var3);
      this.h = var1;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
      this.i = this.h.ac * 4;
      this.j = this.h.ac * 4;
      this.k = -var4.left;
      this.l = -var4.top;
   }

   @Override
   public void a() {
      this.h.F = true;
      com.alensw.ui.view.j.b(this.h).fling((int)this.k, (int)this.l, this.b, this.c, 0, this.d, 0, this.e, this.i, this.j);
   }

   @Override
   public void a(float var1) {
      android.widget.OverScroller scroller = com.alensw.ui.view.j.b(this.h);
      if (scroller.computeScrollOffset()) {
         float x = this.d > 0 ? Math.max(-this.i, Math.min(scroller.getCurrX(), this.d + this.i)) : this.k;
         float y = this.e > 0 ? Math.max(-this.j, Math.min(scroller.getCurrY(), this.e + this.j)) : this.l;
         this.h.b(this.k - x, this.l - y);
         this.k = x;
         this.l = y;
      } else {
         this.c();
         this.b();
      }
   }

   @Override
   public void b() {
      this.h.J = null;
      this.h.F = false;
      this.h.l();
   }

   @Override
   public void c() {
      super.c();
      this.h.J = null;
      this.h.F = false;
      com.alensw.ui.view.j.b(this.h).abortAnimation();
   }
}
