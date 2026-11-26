package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
@SuppressLint("Instantiatable")
public class PictureView extends com.alensw.ui.view.j {
   private final com.alensw.ui.view.w a;
   private final RectF b = new RectF();
   private boolean c;
   private int d;
   private final com.alensw.ui.view.y e = new com.alensw.ui.view.bb(this);
   private float verticalDragOffset;
   private float detailsReveal;

   public PictureView(Context var1) {
      this(var1, null);
   }

   public void setVerticalDragListener(w.VerticalDragListener listener) {
      this.a.setVerticalDragListener(listener);
   }

   public void clearVerticalDrag() {
      this.a.clearVerticalDrag();
   }

   public boolean isPictureAtFit() {
      Animation animation = this.getAnimation();
      return this.getPictureType() > 0 && !this.P.isEmpty() && !this.m()
            && (animation == null || animation.hasEnded())
            && Math.abs(this.getTransScale() - 1.0F) <= 0.02F
            && this.P.width() <= this.Q.width() + 1.0F && this.P.height() <= this.Q.height() + 1.0F;
   }

   public void setVerticalDragPosition(float offsetY, float detailsReveal) {
      this.verticalDragOffset = offsetY;
      this.detailsReveal = detailsReveal;
      this.invalidate();
   }

   public boolean beginDetailsPageDrag() {
      if (this.G || this.K == null || this.getPictureType() <= 0) {
         return false;
      }
      this.h();
      return true;
   }

   public void finishDetailsPageDrag(float distanceX) {
      if (distanceX == 0.0F) {
         this.l();
      } else {
         this.a(Math.copySign(this.ah * 2.0F, distanceX));
      }
   }

   public void startViewerTransition(Animation animation, boolean entering) {
      boolean fromDrag = !entering && this.verticalDragOffset != 0.0F && animation instanceof bu;
      if (fromDrag) {
         ((bu)animation).setExitOffsetY(this.verticalDragOffset);
      }
      this.setVerticalDragPosition(0.0F, 0.0F);
      this.startAnimation(animation);
      if (fromDrag) {
         // Android evaluates the first animation frame before invoking draw().
         this.b();
      }
   }

   public PictureView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.a = new com.alensw.ui.view.w(var1, this.e, this.getHandler());
   }

   private RectF b() {
      Animation var2 = this.getAnimation();
      if (var2 instanceof com.alensw.ui.view.bu) {
         RectF var1 = this.b;
         var1.set(0.0F, 0.0F, this.C, this.D);
         var1.intersect(this.P);
         var1.offset(0.0F, this.d);
         if (((com.alensw.ui.view.bu)var2).a(var1, var1)) {
            return var1;
         }
      }

      return null;
   }

   public void a() {
      if (VERSION.SDK_INT >= 16) {
         this.postInvalidateOnAnimation();
      } else {
         this.invalidate();
      }
   }

   @Override
   public void a(com.alensw.b.h.j var1, boolean var2) {
      super.a(var1, var2);
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.c = var2;
      if (this.c) {
         this.b();
      }
   }

   @Override
   public void draw(Canvas canvas) {
      RectF transitionClip = this.c ? this.b() : null;
      int checkpoint = canvas.save();
      if (transitionClip != null) {
         canvas.clipRect(transitionClip);
      }
      // Keep the picture's gesture coordinates and zoom matrix unchanged.
      canvas.translate(0.0F, this.d + this.verticalDragOffset);
      super.draw(canvas);
      canvas.restoreToCount(checkpoint);
      if (transitionClip != null) {
         this.a();
      }
   }

   @Override
   protected void transformPictureForDrawing(Canvas canvas, RectF bounds) {
      if (this.detailsReveal > 0.0F && !bounds.isEmpty()) {
         float availableHeight = Math.max(1.0F, this.D - this.detailsReveal);
         float scale = Math.min(1.0F, availableHeight / bounds.height());
         // Scale each page about its own center so the paging distance stays the same.
         canvas.scale(scale, scale, bounds.centerX(), bounds.centerY());
      }
   }

   @Override
   public void getFocusedRect(Rect var1) {
      int var2 = this.getPaddingLeft();
      int var3 = this.D / 2;
      var1.set(var2, var3 - 10, var2 + 20, var3 + 10);
   }

   @TargetApi(14)
   @Override
   public boolean onGenericMotionEvent(MotionEvent var1) {
      boolean var4 = true;
      boolean var3 = var4;
      if (VERSION.SDK_INT >= 14) {
         if ((var1.getSource() & 2) != 0) {
            switch (var1.getAction()) {
               case 8:
                  float var2 = var1.getAxisValue(9);
                  if (var2 != 0.0F) {
                     this.a(Math.signum(var2) * this.ae / 2.0F);
                     return var4;
                  }
            }
         }

         var3 = super.onGenericMotionEvent(var1);
      }

      return var3;
   }

   @Override
   public boolean onKeyDown(int var1, KeyEvent var2) {
      float var3 = this.ah * 2.0F;
      switch (var1) {
         case 19:
            this.h();
            if (this.P.top < -1.0F && this.d(0.0F, var3)) {
               return true;
            }
            break;
         case 20:
            this.h();
            if (this.P.bottom - this.D > 1.0F && this.d(0.0F, -var3)) {
               return true;
            }

            if (this.getTransScale() > 1.0F && this.d(false) < 1.0F) {
               return true;
            }
            break;
         case 21:
         case 22:
            this.h();
            if (var1 != 21) {
               var3 = -var3;
            }

            if (this.d(var3, 0.0F)) {
               return true;
            }
            break;
         case 23:
         case 66:
            if (this.K != null && this.isShown()) {
               this.K.a(this.C / 2, this.D / 2);
            }
            break;
         case 92:
         case 93:
            this.h();
            if (var1 != 92) {
               var3 = -var3;
            }

            if (this.a(var3)) {
               return true;
            }
      }

      return super.onKeyDown(var1, var2);
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.h();
      int var5 = var2;
      if (VERSION.SDK_INT < 16) {
         int[] var6 = new int[2];
         this.getLocationOnScreen(var6);
         this.d = -var6[1];
         var5 = var2 + var6[1];
      }

      super.onSizeChanged(var1, var5, var3, var4);
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      return this.a.a(var1);
   }
}
