package com.alensw.ui.view;

import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Interpolator;

class p extends com.alensw.ui.view.bt {
   final boolean a;
   final boolean b;
   final boolean c;
   final Matrix d;
   final float e;
   final float f;
   final float g;
   final float h;
   final float i;
   final float j;
   final float k;
   final int l;
   final Runnable m;
   final float n;
   final com.alensw.ui.view.j o;
   private final Matrix p;
   private final Matrix q;
   private final Matrix r;

   p(
      com.alensw.ui.view.j var1,
      View var2,
      Interpolator var3,
      boolean var4,
      boolean var5,
      boolean var6,
      Matrix var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      int var15,
      Runnable var16,
      float var17
   ) {
      super(var2, var3);
      this.o = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
      this.f = var9;
      this.g = var10;
      this.h = var11;
      this.i = var12;
      this.j = var13;
      this.k = var14;
      this.l = var15;
      this.m = var16;
      this.n = var17;
      Matrix var18;
      if (this.a) {
         var18 = new Matrix();
      } else {
         var18 = null;
      }

      this.p = var18;
      Matrix var19;
      if (this.b) {
         var19 = new Matrix();
      } else {
         var19 = null;
      }

      this.q = var19;
      Matrix var20;
      if (this.c) {
         var20 = new Matrix();
      } else {
         var20 = null;
      }

      this.r = var20;
   }

   @Override
   public void a() {
      com.alensw.ui.view.j var2 = this.o;
      boolean var1;
      if (!this.a && !this.b) {
         var1 = false;
      } else {
         var1 = true;
      }

      var2.I = var1;
   }

   @Override
   public void a(float var1) {
      this.o.S.set(this.d);
      if (this.a) {
         float var2 = (this.e + this.f * var1) / this.e;
         this.p.setScale(var2, var2, this.g, this.h);
         this.o.S.postConcat(this.p);
      }

      if (this.b) {
         float var4 = this.i;
         this.q.setRotate(var4 * var1, this.g, this.h);
         this.o.S.postConcat(this.q);
         this.o.H = true;
      }

      if (this.c) {
         float var3 = this.j;
         float var5 = this.k;
         this.r.setTranslate(var3 * var1, var5 * var1);
         this.o.S.postConcat(this.r);
      }

      this.o.j();
      this.o.invalidate();
   }

   @Override
   public void b() {
      this.o.J = null;
      this.o.H = false;
      this.o.I = false;
      if (this.l != 0) {
         Matrix var1;
         if (com.alensw.ui.view.j.a(this.o) != null) {
            var1 = this.o.U;
         } else {
            var1 = this.o.R;
         }

         Matrix var2;
         if (com.alensw.ui.view.j.a(this.o) != null) {
            var2 = this.o.V;
         } else {
            var2 = this.o.T;
         }

         if (com.alensw.ui.view.j.c(this.o) != null) {
            com.alensw.ui.view.j.c(this.o).c(this.l);
         }

         if (com.alensw.ui.view.j.a(this.o) != null) {
            com.alensw.ui.view.j.a(this.o).c(this.l);
         }

         this.o.i();
         var1.invert(this.o.S);
         this.o.S.postConcat(var2);
         this.o.j();
      }

      this.o.invalidate();
      if (this.m != null) {
         this.m.run();
      }

      if (!this.o.I && this.o.K != null) {
         this.o.K.a(this.n / this.e, false);
      }
   }
}
