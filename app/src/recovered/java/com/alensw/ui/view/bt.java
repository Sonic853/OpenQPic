package com.alensw.ui.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class bt implements Runnable {
   private boolean a;
   private long b;
   private long c;
   private final View d;
   private final Interpolator e;

   public bt(View var1, Interpolator var2) {
      this.d = var1;
      this.e = var2;
   }

   public static void a(View var0, Runnable var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.postOnAnimation(var1);
      } else {
         var0.postDelayed(var1, 16L);
      }
   }

   private boolean e() {
      boolean var7 = true;
      long var5 = AnimationUtils.currentAnimationTimeMillis();
      float var1;
      boolean var4;
      if (this.c > 0L) {
         float var3 = (float)Math.min(this.c, var5 - this.b);
         float var2 = var3 / (float)this.c;
         var1 = var2;
         if (this.e != null) {
            var1 = this.e.getInterpolation(var2);
         }

         if (var3 >= (float)this.c) {
            var4 = true;
         } else {
            var4 = false;
         }
      } else {
         var1 = -1.0F;
         var4 = false;
      }

      if (var4) {
         this.a(1.0F);
         this.c();
         this.b();
      } else {
         if (this.a) {
            a(this.d, this);
         }

         this.a(var1);
      }

      if (var4) {
         var7 = false;
      }

      return var7;
   }

   public void a() {
   }

   public void a(float var1) {
   }

   public void a(long var1, boolean var3) {
      if (!var3 && VERSION.SDK_INT < 16) {
         var3 = false;
      } else {
         var3 = true;
      }

      this.a = var3;
      this.b = AnimationUtils.currentAnimationTimeMillis();
      this.c = var1;
      this.a();
      if (this.a) {
         a(this.d, this);
      } else {
         this.d.invalidate();
      }
   }

   public void b() {
   }

   public void c() {
      if (this.a) {
         this.d.removeCallbacks(this);
      }
   }

   public void d() {
      if (!this.a) {
         this.e();
      }
   }

   @Override
   public void run() {
      if (this.a) {
         this.e();
      }
   }
}
