package com.alensw.ui.view;

class ViewerPictureGestures implements PictureGestureListener {
   final PictureView a;
   private float b;
   private float c;

   ViewerPictureGestures(PictureView var1) {
      this.a = var1;
   }

   @Override
   public void a() {
      this.a.b(this.a.af);
   }

   @Override
   public void a(float var1, float var2) {
      if (this.a.m()) {
         this.a.h();
      }

      this.b = this.a.getTransRotation();
      this.c = 0.0F;
   }

   @Override
   public boolean a(float var1, float var2, float var3) {
      boolean var9 = false;
      boolean var8;
      if (this.a.getPictureType() <= 0) {
         var8 = var9;
      } else {
         var8 = var9;
         if (!Float.isNaN(var1)) {
            var8 = var9;
            if (!Float.isInfinite(var1)) {
               if (var1 < 1.0F) {
                  float var4 = Math.min(this.a.getPictureWidth(), 16);
                  float var7 = Math.min(this.a.getPictureHeight(), 16);
                  float var5 = this.a.P.width();
                  float var6 = this.a.P.height();
                  if (var5 * var1 < var4) {
                     return var9;
                  }

                  if (var6 * var1 < var7) {
                     return var9;
                  }
               }

               this.a.a(var1, var2, var3);
               if (this.a.K != null && this.a.isShown()) {
                  this.a.K.a(var1, true);
               }

               var8 = true;
            }
         }
      }

      return var8;
   }

   @Override
   public void b(float var1, float var2) {
      if (this.a.K != null && this.a.isShown()) {
         this.a.K.a(var1, var2);
      }
   }

   @Override
   public boolean b(float var1, float var2, float var3) {
      boolean var4;
      if (this.a.getPictureType() <= 0) {
         var4 = false;
      } else {
         this.a.c(var1, var2, var3);
         this.c = var1;
         var4 = true;
      }

      return var4;
   }

   @Override
   public void c(float var1, float var2) {
      if (this.a.K != null && this.a.isShown()) {
         this.a.K.b(var1, var2);
      }
   }

   @Override
   public void d(float var1, float var2) {
      this.a.c(var1, var2);
   }

   @Override
   public void e(float var1, float var2) {
      this.a.d(var1, var2);
   }

   @Override
   public void f(float var1, float var2) {
      float var4 = this.a.getTransRotation();
      if (var4 == 0.0F && this.b == 0.0F) {
         this.a.e(var1, var2);
      } else {
         int var5 = (int)Math.signum(this.c);
         float var3;
         if (Math.abs(var4 - this.b) < 20.0F) {
            var3 = 0.0F;
         } else if (var5 > 0) {
            var3 = var4 + 45.0F;
         } else {
            var3 = var4;
            if (var5 < 0) {
               var3 = var4 - 45.0F;
            }
         }

         this.a.a(Math.round(var3 / 90.0F) * 90, false, var1, var2, this.a.ad, null);
      }
   }
}
