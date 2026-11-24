package com.alensw.ui.view;

import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;

class m extends com.alensw.ui.view.bt {
   final RectF a;
   final int b;
   final int c;
   final int d;
   final int e;
   final float f;
   final float g;
   final com.alensw.ui.view.j h;
   private final int i;
   private final int j;
   private float k;
   private float l;

   m(com.alensw.ui.view.j var1, View var2, Interpolator var3, RectF var4, int var5, int var6, int var7, int var8, float var9, float var10) {
      super(var2, var3);
      this.h = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
      this.f = var9;
      this.g = var10;
      this.i = this.h.ac * 4;
      this.j = this.h.ac * 4;
      this.k = -this.a.left;
      this.l = -this.a.top;
   }

   @Override
   public void a() {
      this.h.F = true;
      if (com.alensw.ui.view.j.b(this.h).a()) {
         com.alensw.ui.view.j.b(this.h).a((int)this.k, (int)this.l, this.b, this.c, 0, this.d, 0, this.e, this.i, this.j);
      } else {
         com.alensw.ui.view.j.b(this.h).a((int)this.k, (int)this.l, this.b, this.c, 0 - this.i * 2, this.d + this.i * 2, 0 - this.j * 2, this.e + this.j * 2, 0, 0);
      }
   }

   @Override
   public void a(float var1) {
      if (com.alensw.ui.view.j.b(this.h).b()) {
         float var6 = com.alensw.ui.view.j.b(this.h).c();
         float var5 = com.alensw.ui.view.j.b(this.h).d();
         float var2;
         if (com.alensw.ui.view.j.b(this.h).a()) {
            if (this.d > 0) {
               var1 = 0 - this.i;
            } else {
               var1 = this.k;
            }

            float var3;
            if (this.d > 0) {
               var3 = this.d + this.i;
            } else {
               var3 = this.k;
            }

            if (this.e > 0) {
               var2 = 0 - this.j;
            } else {
               var2 = this.l;
            }

            float var4;
            if (this.e > 0) {
               var4 = this.e + this.j;
            } else {
               var4 = this.l;
            }

            if (!(var6 < var1)) {
               if (var6 > var3) {
                  var1 = var3;
               } else {
                  var1 = var6;
               }
            }

            if (!(var5 < var2)) {
               if (var5 > var4) {
                  var2 = var4;
               } else {
                  var2 = var5;
               }
            }
         } else {
            var2 = var5;
            var1 = var6;
         }

         this.h.b(this.k - var1, this.l - var2);
         this.k = var1;
         this.l = var2;
         if (!com.alensw.ui.view.j.b(this.h).a()) {
            if (this.i < this.a.left - 0.0F || this.i < this.f - this.a.right) {
               com.alensw.ui.view.j.b(this.h).g();
            }

            if (this.a.top > this.j + 0 || this.j < this.g - this.a.bottom) {
               com.alensw.ui.view.j.b(this.h).g();
            }
         }
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
      com.alensw.ui.view.j.b(this.h).g();
   }
}
