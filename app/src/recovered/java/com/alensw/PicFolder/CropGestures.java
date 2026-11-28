package com.alensw.PicFolder;

import com.alensw.ui.view.PictureGestureListener;

class CropGestures implements PictureGestureListener {
   final CropImageView a;

   CropGestures(CropImageView var1) {
      this.a = var1;
   }

   @Override
   public void a() {
      if (this.a.u.b()) {
         this.a.b();
         this.a.u.a = 0;
      } else if (this.a.u.c()) {
         this.a.c();
         this.a.u.a = 0;
      } else {
         this.a.b(CropImageView.f(this.a));
      }
   }

   @Override
   public void a(float var1, float var2) {
      if (this.a.m()) {
         this.a.h();
      }

      this.a.u.a(var1, var2);
   }

   @Override
   public boolean a(float var1, float var2, float var3) {
      boolean var9 = false;
      boolean var8;
      if (this.a.u.a()) {
         var8 = var9;
      } else {
         var8 = var9;
         if (!Float.isNaN(var1)) {
            var8 = var9;
            if (!Float.isInfinite(var1)) {
               if (var1 < 1.0F) {
                  float var6 = Math.min(this.a.getPictureWidth(), 16);
                  float var4 = Math.min(this.a.getPictureHeight(), 16);
                  float var7 = CropImageView.g(this.a).width();
                  float var5 = CropImageView.h(this.a).height();
                  if (var7 * var1 < var6) {
                     return var9;
                  }

                  if (var5 * var1 < var4) {
                     return var9;
                  }
               }

               this.a.a(var1, var2, var3);
               var8 = true;
            }
         }
      }

      return var8;
   }

   @Override
   public void b(float var1, float var2) {
      if (this.a.u.a()) {
         this.a.c();
         this.a.u.a = 0;
      }
   }

   @Override
   public boolean b(float var1, float var2, float var3) {
      boolean var4;
      if (this.a.u.a()) {
         var4 = false;
      } else {
         this.a.c(var1, var2, var3);
         var4 = true;
      }

      return var4;
   }

   @Override
   public void c(float var1, float var2) {
      this.a.a(var1, var2);
   }

   @Override
   public void d(float var1, float var2) {
      if (this.a.u.b()) {
         this.a.u.a(this.a.u.a, var1, var2);
         this.a.c();
      } else if (this.a.u.c()) {
         this.a.v.offset(var1, var2);
         this.a.d();
         this.a.c();
      } else {
         this.a.c(var1, var2);
      }
   }

   @Override
   public void e(float var1, float var2) {
      if (this.a.u.a()) {
         this.a();
      } else {
         this.a.d(var1, var2);
      }
   }

   @Override
   public void f(float var1, float var2) {
      if (!this.a.u.a()) {
         this.a.e(var1, var2);
      }
   }
}
