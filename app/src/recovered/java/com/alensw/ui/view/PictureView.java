package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
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

   public PictureView(Context var1) {
      this(var1, null);
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
   public void draw(Canvas var1) {
      boolean var3 = false;
      boolean var2 = false;
      if (this.c) {
         RectF var4 = this.b();
         if (var4 != null) {
            var2 = true;
         }

         var3 = var2;
         if (var2) {
            com.alensw.compat.LegacyPlatform.save(var1, 2);
            var1.clipRect(var4, Op.INTERSECT);
            var3 = var2;
         }
      }

      if (this.d != 0) {
         var1.translate(0.0F, this.d);
      }

      super.draw(var1);
      if (this.d != 0) {
         var1.translate(0.0F, -this.d);
      }

      if (var3) {
         var1.restore();
         this.a();
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
