package com.alensw.ui.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class PictureGestureDetector {
   public interface VerticalDragListener {
      boolean onVerticalDragStart(float distanceY);
      void onVerticalDrag(float distanceY);
      void onVerticalDragEnd(float distanceY);
      void onVerticalDragCancel(boolean animate);
   }

   private static final int VERTICAL_DRAG = 5;
   private VerticalDragListener verticalDragListener;
   private boolean verticalDragAllowed;
   private final PointF A;
   private final PointF B;
   private final PointF C;
   private final PointF D;
   private boolean E;
   private final Runnable F;
   private int a = 0;
   private boolean b;
   private boolean c;
   private boolean d;
   private boolean e;
   private float f;
   private float g;
   private float h;
   private float i;
   private float j;
   private float k;
   private float l;
   private float m;
   private float n;
   private MotionEvent o;
   private MotionEvent p;
   private VelocityTracker q;
   private final int r;
   private final int s;
   private final int t;
   private final float u;
   private final float v;
   private final float w;
   private final float x;
   private final Handler y;
   private final PictureGestureListener z;

   public PictureGestureDetector(Context var1, PictureGestureListener var2, Handler var3) {
      this.A = new PointF();
      this.B = new PointF();
      this.C = new PointF();
      this.D = new PointF();
      this.E = false;
      this.F = new ConfirmSingleTap(this);
      this.z = var2;
      Handler var5 = var3;
      if (var3 == null) {
         var5 = new Handler();
      }

      this.y = var5;
      ViewConfiguration var6 = ViewConfiguration.get(var1);
      float var4 = var6.getScaledTouchSlop();
      this.v = var4;
      this.u = var4;
      this.w = var6.getScaledDoubleTapSlop();
      this.r = var6.getScaledMaximumFlingVelocity();
      this.s = var6.getScaledMinimumFlingVelocity();
      this.t = ViewConfiguration.getDoubleTapTimeout();
      this.x = var1.getResources().getDisplayMetrics().density * 2.0F;
   }

   void setVerticalDragListener(VerticalDragListener listener) {
      this.verticalDragListener = listener;
   }

   void clearVerticalDrag() {
      if (this.a == VERTICAL_DRAG) {
         this.a = 0;
      }
   }

   private float a(float var1) {
      this.k = Math.abs(var1) + this.v;
      if (this.k > this.v && this.l > 0.0F) {
         boolean var2;
         if (this.k >= this.l) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1 = Math.abs(1.0F - this.k / this.l) * this.j;
         if (var2) {
            var1++;
         } else {
            var1 = 1.0F - var1;
         }
      } else {
         var1 = 1.0F;
      }

      return var1;
   }

   public static float a(float var0, float var1, float var2, float var3) {
      return (float)Math.hypot(var2 - var0, var3 - var1);
   }

   private boolean a(MotionEvent var1, MotionEvent var2, MotionEvent var3) {
      boolean var5 = false;
      boolean var4;
      if (!this.d) {
         var4 = var5;
      } else {
         var4 = var5;
         if (var3.getEventTime() - var2.getEventTime() <= this.t) {
            var4 = var5;
            if (a(var1.getX(), var1.getY(), var3.getX(), var3.getY()) < this.w) {
               var4 = true;
            }
         }
      }

      return var4;
   }

   static boolean a(PictureGestureDetector var0) {
      return var0.c;
   }

   static boolean a(PictureGestureDetector var0, boolean var1) {
      var0.E = var1;
      return var1;
   }

   public static float b(float var0, float var1, float var2, float var3) {
      return (float)Math.toDegrees(Math.atan2(var3 - var1, var2 - var0));
   }

   static int b(PictureGestureDetector var0) {
      return var0.a;
   }

   private void b(MotionEvent var1) {
      this.f = var1.getX(0);
      this.g = var1.getY(0);
      this.h = var1.getX(1);
      this.i = var1.getY(1);
      this.C.set((this.f + this.h) / 2.0F, (this.g + this.i) / 2.0F);
      this.k = a(this.f, this.g, this.h, this.i);
      this.m = b(this.f, this.g, this.h, this.i);
   }

   static PointF c(PictureGestureDetector var0) {
      return var0.A;
   }

   private boolean c(float var1, float var2, float var3, float var4) {
      float var8 = this.f;
      float var7 = this.g;
      float var5 = this.h;
      float var6 = this.i;
      boolean var9;
      if ((var8 - var1) * (var5 - var3) > 0.0F && (var7 - var2) * (var6 - var4) > 0.0F) {
         var9 = true;
      } else {
         var9 = false;
      }

      return var9;
   }

   static PictureGestureListener d(PictureGestureDetector var0) {
      return var0.z;
   }

   public boolean a(MotionEvent var1) {
      int var6 = var1.getAction() & 0xFF;
      float var4 = var1.getX();
      float var3 = var1.getY();
      if (this.q == null) {
         this.q = VelocityTracker.obtain();
      }

      this.q.addMovement(var1);
      switch (var6) {
         case 0:
            this.verticalDragAllowed = this.verticalDragListener != null;
            boolean var7 = this.E;
            if (this.E) {
               this.y.removeCallbacks(this.F);
               this.E = false;
            }

            if (var7 && this.o != null && this.p != null && this.a(this.o, this.p, var1)) {
               this.b = true;
            } else {
               this.y.postDelayed(this.F, this.t);
               this.E = true;
            }

            this.c = true;
            this.d = true;
            this.a = 1;
            this.A.set(var4, var3);
            if (this.o != null) {
               this.o.recycle();
            }

            this.o = MotionEvent.obtain(var1);
            this.z.a(this.A.x, this.A.y);
            break;
         case 1:
            this.c = false;
            if (this.a == VERTICAL_DRAG) {
               this.clearVerticalDrag();
               this.verticalDragListener.onVerticalDragEnd(var3 - this.A.y);
            } else if (this.a == 3) {
               this.z.f(this.C.x, this.C.y);
            } else if (this.a == 2) {
               VelocityTracker var8 = this.q;
               var8.computeCurrentVelocity(1000, this.r);
               var3 = var8.getXVelocity();
               float var12 = var8.getYVelocity();
               if (Math.max(Math.abs(var3), Math.abs(var12)) > this.s) {
                  this.z.e(var3, var12);
               } else {
                  this.z.a();
               }
            } else if (this.b) {
               this.z.c(this.A.x, this.A.y);
            }

            if (this.p != null) {
               this.p.recycle();
            }

            this.p = MotionEvent.obtain(var1);
         case 3:
            if (this.q != null) {
               this.q.recycle();
               this.q = null;
            }

            if (var6 == 3) {
               if (this.a == VERTICAL_DRAG) {
                  this.verticalDragListener.onVerticalDragCancel(true);
               }
               this.a = 0;
            }

            this.c = false;
            this.b = false;
            this.e = false;
            break;
         case 2:
            if (this.a == VERTICAL_DRAG) {
               this.verticalDragListener.onVerticalDrag(var3 - this.A.y);
            } else if (this.a == 4) {
               float var5 = this.f;
               var3 = this.g;
               float var2 = this.h;
               var4 = this.i;
               if (var1.getPointerCount() > 1) {
                  this.b(var1);
               }

               if (this.c(var5, var3, var2, var4)) {
                  this.z.d(this.C.x - this.D.x, this.C.y - this.D.y);
                  this.e = false;
               } else {
                  var2 = Math.abs(this.k - this.l);
                  if (this.l > 0.0F && this.l != this.k && this.z.a(this.k / this.l, this.C.x, this.C.y)) {
                     this.l = this.k;
                  }

                  if (!this.e) {
                     if (var2 > this.x) {
                        this.n = this.m;
                     } else if (Math.abs(this.m - this.n) > 3.0F) {
                        this.e = true;
                        this.n = this.m;
                     }
                  }

                  if (this.e && this.n != this.m && this.z.b(this.m - this.n, this.C.x, this.C.y)) {
                     this.n = this.m;
                  }
               }

               this.D.set(this.C.x, this.C.y);
            } else if (this.a == 3) {
               float var10 = this.a(var3);
               if (this.z.a(var10, this.C.x, this.C.y)) {
                  this.l = this.k;
               }
            } else if (this.a == 2) {
               this.z.d(var4 - this.B.x, var3 - this.B.y);
               this.B.set(var4, var3);
            } else if (this.a == 1) {
               float var11 = this.A.x;
               float var16 = var3 - this.A.y;
               if (this.b) {
                  this.C.set(this.A);
                  this.a(var3);
                  if (Math.abs(var16) > this.v) {
                     this.a = 3;
                     this.l = this.k;
                     this.j = (float)Math.sqrt((Math.abs(this.A.y) + this.v) / (this.v * 2.0F));
                  }
               } else if (Math.max(Math.abs(var4 - var11), Math.abs(var16)) > this.u) {
                  this.d = false;
                  if (this.verticalDragAllowed && Math.abs(var16) > 1.4F * Math.abs(var4 - var11)
                        && this.verticalDragListener.onVerticalDragStart(var16)) {
                     this.a = VERTICAL_DRAG;
                     this.y.removeCallbacks(this.F);
                     this.E = false;
                     this.verticalDragListener.onVerticalDrag(var16);
                  } else {
                     this.a = 2;
                     this.B.set(var4, var3);
                  }
               }
            }
         case 4:
         default:
            break;
         case 5:
            this.verticalDragAllowed = false;
            if (this.a == VERTICAL_DRAG) {
               this.clearVerticalDrag();
               this.verticalDragListener.onVerticalDragCancel(false);
            }
            if (var1.getPointerCount() > 1) {
               this.b(var1);
            }

            if (this.k > this.v) {
               this.a = 4;
               this.D.set(this.C.x, this.C.y);
               this.l = this.k;
               this.n = this.m;
            }

            this.b = false;
            break;
         case 6:
            if (this.a == 4) {
               this.z.f(this.C.x, this.C.y);
               this.a = 0;
               this.l = 0.0F;
               this.e = false;
            }
      }

      return true;
   }
}
