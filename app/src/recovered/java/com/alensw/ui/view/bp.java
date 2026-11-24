package com.alensw.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.support.v4.widget.k;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.GestureDetector.OnGestureListener;
public class bp extends View {
   public static final int[] f = new int[0];
   public static final int[] g = new int[]{16842910, 16842919};
   private final Callback a = new com.alensw.ui.view.bq(this);
   private boolean b;
   private final Runnable c = new com.alensw.ui.view.br(this);
   private boolean d;
   private final OnGestureListener e = new com.alensw.ui.view.bs(this);
   protected boolean h;
   protected int i;
   protected int j;
   protected com.alensw.ui.view.au k;
   protected final int l = ViewConfiguration.getPressedStateDuration();
   protected final Drawable m;
   protected final RippleDrawable n;
   protected final com.alensw.ui.view.bn o;
   protected final k p;
   protected final GestureDetector q;

   public bp(Context var1) {
      this(var1, null);
   }

   @TargetApi(9)
   public bp(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.m = com.alensw.b.l.b.e(var1);
      this.m.setCallback(this.a);
      RippleDrawable var5;
      if (VERSION.SDK_INT >= 21 && this.m instanceof RippleDrawable) {
         var5 = (RippleDrawable)this.m;
      } else {
         var5 = null;
      }

      this.n = var5;
      this.q = new GestureDetector(var1, this.e);
      this.o = new com.alensw.ui.view.bn(var1, this);
      this.p = new k(var1);
      if (this.p.a()) {
         try {
            com.alensw.ui.view.au var6 = new com.alensw.ui.view.au(this);
            this.k = var6;
         } catch (Throwable var3) {
         }

         ViewConfiguration var4 = ViewConfiguration.get(var1);
         this.i = var4.getScaledOverscrollDistance();
         this.j = var4.getScaledOverflingDistance();
      }
   }

   public void a(Canvas var1, RectF var2) {
      com.alensw.compat.LegacyPlatform.save(var1, 2);
      var1.clipRect(var2);
      this.m.setBounds((int)var2.left, (int)var2.top, (int)var2.right, (int)var2.bottom);
      this.m.draw(var1);
      var1.restore();
   }

   public void a(MotionEvent var1) {
   }

   public void a(MotionEvent var1, MotionEvent var2, float var3, float var4) {
   }

   public void a(Runnable var1) {
      if (VERSION.SDK_INT >= 16) {
         this.postOnAnimation(var1);
      } else {
         this.postDelayed(var1, 16L);
      }
   }

   public void a(boolean var1) {
      this.removeCallbacks(this.c);
      if (var1) {
         if (this.m.isStateful()) {
            this.m.setState(g);
         }
      } else {
         this.postDelayed(this.c, this.l);
      }
   }

   @TargetApi(9)
   public boolean a(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      int var9 = this.getScrollRangeX();
      int var8 = this.getScrollRangeY();
      if (this.p.a()) {
         if (this.k != null) {
            this.k.a(var1, var2);
         }

         boolean var10 = super.overScrollBy(var1, var2, var3, var4, var9, var8, var5, var6, var7);
         var7 = var10;
         if (this.k != null) {
            var7 = var10;
            if (this.k.a()) {
               this.g();
               var7 = var10;
            }
         }
      } else {
         var1 = var3 + var1;
         var2 = var4 + var2;
         if (var1 < 0) {
            var1 = 0;
            var7 = true;
         } else if (var1 > var9) {
            var7 = true;
            var1 = var9;
         } else {
            var7 = false;
         }

         boolean var15;
         if (var2 < 0) {
            var2 = 0;
            var15 = true;
         } else if (var2 > var8) {
            var15 = true;
            var2 = var8;
         } else {
            var15 = false;
         }

         this.onOverScrolled(var1, var2, var7, var15);
         if (!var7 && !var15) {
            var7 = false;
         } else {
            var7 = true;
         }
      }

      return var7;
   }

   public void b(MotionEvent var1) {
   }

   public void b(MotionEvent var1, MotionEvent var2, float var3, float var4) {
   }

   public void c(float var1, float var2) {
   }

   public void c(MotionEvent var1) {
   }

   @Override
   public void computeScroll() {
      int var3 = 0;
      if (this.p.b()) {
         int var4 = this.getScrollX();
         int var1 = this.getScrollY();
         var3 = this.p.c();
         int var2 = this.p.d();
         if (var4 != var3 || var1 != var2) {
            this.a(var3 - var4, var2 - var1, var4, var1, this.j, this.j, false);
         }

         if (this.k != null) {
            this.k.b(var3, var2, var4, var1, this.getScrollRangeX(), this.getScrollRangeY());
         }

         this.g();
         this.b = false;
      } else if (!this.b) {
         int var6 = this.getScrollRangeX();
         int var10 = this.getScrollRangeY();
         int var8 = this.getScrollX();
         int var5 = this.getScrollY();
         boolean var7;
         if (var8 < 0) {
            var7 = true;
            var8 = 0;
         } else if (var8 > var6) {
            var8 = var6;
            var7 = true;
         } else {
            var7 = false;
         }

         if (var5 < 0) {
            var7 = true;
         } else if (var5 > var10) {
            var7 = true;
            var3 = var10;
         } else {
            var3 = var5;
         }

         if (var7) {
            this.scrollTo(var8, var3);
            this.b = true;
         }
      }
   }

   public void d(MotionEvent var1) {
   }

   @Override
   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.k != null && this.k.a(var1)) {
         this.g();
      }

      if (this.o.a()) {
         this.o.a(var1);
      }
   }

   public String e() {
      return null;
   }

   public final boolean f() {
      boolean var1;
      if (this.p.f() && !this.o.c()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void g() {
      if (VERSION.SDK_INT >= 16) {
         this.postInvalidateOnAnimation();
      } else {
         this.invalidate();
      }
   }

   public int getClientHeight() {
      return this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
   }

   public int getClientWidth() {
      return this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
   }

   public int getScrollMode() {
      byte var1 = 0;
      if (this.getScrollRangeX() > 0) {
         var1 = 1;
      }

      int var2 = var1;
      if (this.getScrollRangeY() > 0) {
         var2 = var1 | 2;
      }

      return var2;
   }

   public float getScrollPosX() {
      int var2 = this.getScrollRangeX();
      float var1;
      if (var2 > 0) {
         var1 = (float)this.getScrollX() / var2;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public float getScrollPosY() {
      int var2 = this.getScrollRangeY();
      float var1;
      if (var2 > 0) {
         var1 = (float)this.getScrollY() / var2;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public int getScrollRangeX() {
      return this.computeHorizontalScrollRange() - this.getClientWidth();
   }

   public int getScrollRangeY() {
      return this.computeVerticalScrollRange() - this.getClientHeight();
   }

   public void h() {
      if (VERSION.SDK_INT >= 16) {
         this.postInvalidateOnAnimation();
      } else {
         this.postInvalidate();
      }
   }

   @TargetApi(14)
   @Override
   public boolean onGenericMotionEvent(MotionEvent var1) {
      boolean var5 = true;
      boolean var4 = var5;
      if (VERSION.SDK_INT >= 14) {
         if ((var1.getSource() & 2) != 0) {
            switch (var1.getAction()) {
               case 8:
                  if (!this.isPressed()) {
                     float var2;
                     float var3;
                     if ((var1.getMetaState() & 1) != 0) {
                        var3 = var1.getAxisValue(9);
                        var2 = 0.0F;
                     } else {
                        var3 = var1.getAxisValue(10);
                        var2 = -var1.getAxisValue(9);
                     }

                     if (var3 != 0.0F || var2 != 0.0F) {
                        this.c(var3, var2);
                        return var5;
                     }
                  }
            }
         }

         var4 = super.onGenericMotionEvent(var1);
      }

      return var4;
   }

   @Override
   protected void onOverScrolled(int var1, int var2, boolean var3, boolean var4) {
      int var5 = this.getScrollRangeX();
      int var7 = this.getScrollRangeY();
      int var6 = this.getScrollX();
      int var8 = this.getScrollY();
      super.scrollTo(var1, var2);
      if (this.k != null) {
         this.k.a(var1, var2, var6, var8, var5, var7);
      }

      if (!this.p.f() && (var3 || var4)) {
         this.p.a(var1, var2, 0, var5, 0, var7);
      }
   }

   @Override
   protected void onScrollChanged(int var1, int var2, int var3, int var4) {
      if (var1 != var3 || var2 != var4) {
         this.o.c(this.h);
      }
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      if (var1 > 0 && var2 > 0) {
         this.o.c(this.h);
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      boolean var3 = true;
      boolean var2;
      if (this.o.a(var1)) {
         var2 = var3;
         if (!this.p.f()) {
            this.p.a(true);
            var2 = var3;
         }
      } else {
         switch (var1.getAction()) {
            case 0:
               this.d = true;
               if (this.n != null) {
                  this.n.setHotspot(var1.getX() + this.getScrollX(), var1.getY() + this.getScrollY());
                  this.g();
               }
               break;
            case 1:
            case 3:
               if (this.d) {
                  this.a(true);
                  this.d = false;
               }

               this.a(false);
               if (this.k != null) {
                  this.k.b();
               }

               if (this.p.a(this.getScrollX(), this.getScrollY(), 0, this.getScrollRangeX(), 0, this.getScrollRangeY())) {
                  this.g();
               }
               break;
            case 2:
               this.d = false;
               if (this.k != null) {
                  this.k.a(var1.getX(), var1.getY());
               }

               if (this.n != null) {
                  this.n.setHotspot(var1.getX() + this.getScrollX(), var1.getY() + this.getScrollY());
                  this.g();
               }
         }

         var2 = this.q.onTouchEvent(var1);
      }

      return var2;
   }

   public void setFastScrollEnabled(boolean var1) {
      this.h = var1;
   }

   public void setScrollPosX(float var1) {
      int var2 = this.getScrollRangeX();
      if (var2 > 0) {
         var1 = Math.max(0.0F, Math.min(1.0F, var1));
         this.scrollTo(Math.round(var2 * var1), 0);
      }
   }

   public void setScrollPosY(float var1) {
      int var2 = this.getScrollRangeY();
      if (var2 > 0) {
         var1 = Math.max(0.0F, Math.min(1.0F, var1));
         this.scrollTo(0, Math.round(var2 * var1));
      }
   }
}
