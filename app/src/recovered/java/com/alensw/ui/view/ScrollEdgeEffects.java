package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.EdgeEffect;

public class ScrollEdgeEffects {
   private final ScrollableGalleryView a;
   private final EdgeEffect top;
   private final EdgeEffect bottom;
   private final EdgeEffect left;
   private final EdgeEffect right;
   private float f;
   private float g;
   private int h;
   private int i;

   public ScrollEdgeEffects(ScrollableGalleryView var1) {
      Context var2 = var1.getContext();
      this.a = var1;
      this.top = new EdgeEffect(var2);
      this.bottom = new EdgeEffect(var2);
      this.left = new EdgeEffect(var2);
      this.right = new EdgeEffect(var2);
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
               this.left.onPull((float)this.h / this.a.getWidth(), this.g / this.a.getHeight());
               if (!this.right.isFinished()) {
                  this.right.onRelease();
               }
            } else if (var1 > var5) {
               this.right.onPull((float)this.h / this.a.getWidth(), 1.0F - this.g / this.a.getHeight());
               if (!this.left.isFinished()) {
                  this.left.onRelease();
               }
            }

            this.h = 0;
         }

         if (var6 > 0) {
            var1 = this.i + var4;
            if (var1 < 0) {
               this.top.onPull((float)this.i / this.a.getHeight(), this.f / this.a.getWidth());
               if (!this.bottom.isFinished()) {
                  this.bottom.onRelease();
               }
            } else if (var1 > var6) {
               this.bottom.onPull((float)this.i / this.a.getHeight(), 1.0F - this.f / this.a.getWidth());
               if (!this.top.isFinished()) {
                  this.top.onRelease();
               }
            }

            this.i = 0;
         }
      }
   }

   public boolean a() {
      return !top.isFinished() || !bottom.isFinished() || !left.isFinished() || !right.isFinished();
   }

   public boolean a(Canvas var1) {
      int var5 = this.a.getScrollX();
      int var3 = this.a.getScrollY();
      int var4 = this.a.getWidth();
      int var6 = this.a.getHeight();
      boolean var10;
      if (!this.top.isFinished()) {
         int var7 = var1.save();
         var1.translate(var5, this.a.getPaddingTop() + Math.min(0, var3));
         this.top.setSize(var4, var6);
         var10 = this.top.draw(var1);
         var1.restoreToCount(var7);
      } else {
         var10 = false;
      }

      boolean var9 = var10;
      if (!this.bottom.isFinished()) {
         int var11 = var1.save();
         var1.translate(-var4 + var5, Math.max(this.a.getScrollRangeY(), var3) + var6 - this.a.getPaddingBottom());
         var1.rotate(180.0F, var4, 0.0F);
         this.bottom.setSize(var4, var6);
         var9 = var10 | this.bottom.draw(var1);
         var1.restoreToCount(var11);
      }

      var10 = var9;
      if (!this.left.isFinished()) {
         int var12 = var1.save();
         var1.rotate(270.0F);
         float var2 = -var6 - var3;
         int var8 = this.a.getPaddingLeft();
         var1.translate(var2, Math.min(0, var5) + var8);
         this.left.setSize(var6, var4);
         var10 = var9 | this.left.draw(var1);
         var1.restoreToCount(var12);
      }

      var9 = var10;
      if (!this.right.isFinished()) {
         int var13 = var1.save();
         var1.rotate(90.0F);
         var1.translate(var3, -(Math.max(this.a.getScrollRangeX(), var5) + var4 - this.a.getPaddingRight()));
         this.right.setSize(var6, var4);
         var9 = var10 | this.right.draw(var1);
         var1.restoreToCount(var13);
      }

      return var9;
   }

   public void b() {
      this.top.onRelease();
      this.bottom.onRelease();
      this.left.onRelease();
      this.right.onRelease();
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var6 > 0) {
         if (var2 < 0 && var4 >= 0) {
            this.top.onAbsorb((int)this.a.p.getCurrVelocity());
            if (!this.bottom.isFinished()) {
               this.bottom.onRelease();
            }
         } else if (var2 > var6 && var4 <= var6) {
            this.bottom.onAbsorb((int)this.a.p.getCurrVelocity());
            if (!this.top.isFinished()) {
               this.top.onRelease();
            }
         }
      }

      if (var5 > 0) {
         if (var1 < 0 && var3 >= 0) {
            this.left.onAbsorb((int)this.a.p.getCurrVelocity());
            if (!this.right.isFinished()) {
               this.right.onRelease();
            }
         } else if (var1 > var5 && var3 <= var5) {
            this.right.onAbsorb((int)this.a.p.getCurrVelocity());
            if (!this.left.isFinished()) {
               this.left.onRelease();
            }
         }
      }
   }
}
