package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.j;

public class au {
   private final com.alensw.ui.view.bp a;
   private final j b;
   private final j c;
   private final j d;
   private final j e;
   private float f;
   private float g;
   private int h;
   private int i;

   public au(com.alensw.ui.view.bp var1) {
      Context var2 = var1.getContext();
      this.a = var1;
      this.b = new j(var2);
      this.c = new j(var2);
      this.d = new j(var2);
      this.e = new j(var2);
   }

   public void a(float var1, float var2) {
      this.f = var1;
      this.g = var2;
   }

   public void a(int var1, int var2) {
      this.h = var1;
      this.i = var2;
   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var1 == var3 && var2 == var4) {
         if (var5 > 0) {
            var1 = this.h + var3;
            if (var1 < 0) {
               this.d.a((float)this.h / this.a.getWidth(), this.g / this.a.getHeight());
               if (!this.e.a()) {
                  this.e.b();
               }
            } else if (var1 > var5) {
               this.e.a((float)this.h / this.a.getWidth(), 1.0F - this.g / this.a.getHeight());
               if (!this.d.a()) {
                  this.d.b();
               }
            }

            this.h = 0;
         }

         if (var6 > 0) {
            var1 = this.i + var4;
            if (var1 < 0) {
               this.b.a((float)this.i / this.a.getHeight(), this.f / this.a.getWidth());
               if (!this.c.a()) {
                  this.c.b();
               }
            } else if (var1 > var6) {
               this.c.a((float)this.i / this.a.getHeight(), 1.0F - this.f / this.a.getWidth());
               if (!this.b.a()) {
                  this.b.b();
               }
            }

            this.i = 0;
         }
      }
   }

   public boolean a() {
      boolean var1;
      if (this.b.a() && this.c.a() && this.d.a() && this.e.a()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean a(Canvas var1) {
      int var5 = this.a.getScrollX();
      int var3 = this.a.getScrollY();
      int var4 = this.a.getWidth();
      int var6 = this.a.getHeight();
      boolean var10;
      if (!this.b.a()) {
         int var7 = var1.save();
         var1.translate(var5, this.a.getPaddingTop() + Math.min(0, var3));
         this.b.a(var4, var6);
         var10 = this.b.a(var1) | false;
         var1.restoreToCount(var7);
      } else {
         var10 = false;
      }

      boolean var9 = var10;
      if (!this.c.a()) {
         int var11 = var1.save();
         var1.translate(-var4 + var5, Math.max(this.a.getScrollRangeY(), var3) + var6 - this.a.getPaddingBottom());
         var1.rotate(180.0F, var4, 0.0F);
         this.c.a(var4, var6);
         var9 = var10 | this.c.a(var1);
         var1.restoreToCount(var11);
      }

      var10 = var9;
      if (!this.d.a()) {
         int var12 = var1.save();
         var1.rotate(270.0F);
         float var2 = -var6 - var3;
         int var8 = this.a.getPaddingLeft();
         var1.translate(var2, Math.min(0, var5) + var8);
         this.d.a(var6, var4);
         var10 = var9 | this.d.a(var1);
         var1.restoreToCount(var12);
      }

      var9 = var10;
      if (!this.e.a()) {
         int var13 = var1.save();
         var1.rotate(90.0F);
         var1.translate(var3, -(Math.max(this.a.getScrollRangeX(), var5) + var4 - this.a.getPaddingRight()));
         this.e.a(var6, var4);
         var9 = var10 | this.e.a(var1);
         var1.restoreToCount(var13);
      }

      return var9;
   }

   public void b() {
      this.b.b();
      this.c.b();
      this.d.b();
      this.e.b();
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var6 > 0) {
         if (var2 < 0 && var4 >= 0) {
            this.b.a((int)this.a.p.e());
            if (!this.c.a()) {
               this.c.b();
            }
         } else if (var2 > var6 && var4 <= var6) {
            this.c.a((int)this.a.p.e());
            if (!this.b.a()) {
               this.b.b();
            }
         }
      }

      if (var5 > 0) {
         if (var1 < 0 && var3 >= 0) {
            this.d.a((int)this.a.p.e());
            if (!this.e.a()) {
               this.e.b();
            }
         } else if (var1 > var5 && var3 <= var5) {
            this.e.a((int)this.a.p.e());
            if (!this.d.a()) {
               this.d.b();
            }
         }
      }
   }
}
