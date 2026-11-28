package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class FastScrollTouchHandler extends FadingOverlay {
   protected int f = 0;
   protected int g;
   protected boolean h;
   protected boolean i;
   protected Rect j = new Rect();

   public FastScrollTouchHandler(Context var1, View var2) {
      super(var1, var2);
   }

   @Override
   public void a(int var1) {
      super.a(var1);
   }

   @Override
   public void a(boolean var1) {
      super.a(var1);
   }

   @Override
   public boolean a() {
      return super.a();
   }

   protected boolean a(int var1, int var2) {
      boolean var4 = this.j.contains(var1, var2);
      boolean var3;
      if (var4) {
         if (this.f == 1) {
            this.g = var1 - this.j.left;
         } else if (this.f == 2) {
            this.g = var2 - this.j.top;
         }

         this.a(4);
         var3 = var4;
      } else {
         var3 = var4;
         if (this.h) {
            if (this.f == 1
               && var2 >= this.j.top
               && var2 < this.j.bottom
               && var1 >= this.a.getPaddingLeft()
               && var1 < this.a.getWidth() - this.a.getPaddingRight()) {
               this.i = true;
               var3 = true;
            } else {
               var3 = var4;
               if (this.f == 2) {
                  var3 = var4;
                  if (var1 >= this.j.left) {
                     var3 = var4;
                     if (var1 < this.j.right) {
                        var3 = var4;
                        if (var2 >= this.a.getPaddingTop()) {
                           var3 = var4;
                           if (var2 < this.a.getHeight() - this.a.getPaddingBottom()) {
                              this.i = true;
                              var3 = true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (var3) {
         this.a.removeCallbacks(this.e);
      }

      return var3;
   }

   public boolean a(MotionEvent var1) {
      boolean var5 = true;
      if (this.b != 0 && this.f != 0) {
         int var4 = var1.getAction();
         int var3 = (int)var1.getX();
         int var2 = (int)var1.getY();
         switch (var4 & 0xFF) {
            case 0:
               if (this.a(var3, var2)) {
                  return var5;
               }
               break;
            case 1:
               boolean var6;
               if (this.b == 4) {
                  this.c(var3, var2);
                  var6 = true;
               } else if (this.i) {
                  this.d(var3, var2);
                  this.i = false;
                  var6 = true;
               } else {
                  var6 = false;
               }

               if (var6) {
                  this.a(3);
                  this.a.removeCallbacks(this.e);
                  this.a.postDelayed(this.e, 1000L);
                  return var5;
               }
               break;
            case 2:
               if (this.b == 4) {
                  this.b(var3, var2);
                  return var5;
               }

               if (this.i) {
                  this.a(this.j.centerX(), this.j.centerY());
                  this.i = false;
               }
         }

         var5 = false;
      } else {
         var5 = false;
      }

      return var5;
   }

   @Override
   public void b(int var1) {
      super.b(var1);
   }

   protected void b(int var1, int var2) {
      int var7 = this.j.width();
      int var6 = this.j.height();
      int var5;
      int var15;
      if (this.f == 1) {
         var5 = this.j.left;
         int var3 = this.a.getPaddingLeft();
         var2 = this.g;
         var15 = this.a.getWidth();
         int var8 = this.a.getPaddingRight();
         var2 = var1 - var2;
         var1 = var3;
         var15 = var15 - var3 - var8 - var7;
      } else {
         if (this.f != 2) {
            return;
         }

         var5 = this.j.top;
         int var13 = this.a.getPaddingTop();
         var1 = this.g;
         var15 = this.a.getHeight();
         int var17 = this.a.getPaddingBottom();
         var2 -= var1;
         var1 = var13;
         var15 = var15 - var13 - var17 - var6;
      }

      int var14;
      if (var2 < var1) {
         var14 = var1;
      } else {
         var14 = var2;
         if (var2 > var15 + var1) {
            var14 = var15 + var1;
         }
      }

      if (Math.abs(var14 - var5) >= 2 && var15 >= 2) {
         if (this.f == 1) {
            this.j.set(var14, this.j.top, var7 + var14, this.j.bottom);
         } else if (this.f == 2) {
            this.j.set(this.j.left, var14, this.j.right, var6 + var14);
         }

         this.e(var14 - var1, var15);
      }
   }

   @Override
   public void b(boolean var1) {
      super.b(var1);
   }

   protected void c(int var1, int var2) {
   }

   public boolean c() {
      boolean var1;
      if (this.f == 4) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void d(int var1, int var2) {
      int var4 = this.j.width();
      int var3 = this.j.height();
      if (this.f == 1) {
         var3 = this.a.getPaddingLeft();
         var4 = this.a.getWidth() - var3 - this.a.getPaddingRight() - var4;
         var2 = var1;
         var1 = var3;
      } else {
         if (this.f != 2) {
            return;
         }

         var1 = this.a.getPaddingTop();
         var4 = this.a.getHeight() - var1 - this.a.getPaddingBottom() - var3;
      }

      if (var2 < var1) {
         var3 = var1;
      } else {
         var3 = var2;
         if (var2 > var4 + var1) {
            var3 = var4 + var1;
         }
      }

      this.e(var3 - var1, var4);
   }

   protected void e(int var1, int var2) {
   }
}
