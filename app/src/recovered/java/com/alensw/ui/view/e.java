package com.alensw.ui.view;

import android.graphics.Camera;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class e {
   public static Animation a(int var0, boolean var1, int var2, int var3, int var4) {
      Animation var5;
      switch (var0) {
         case 2:
            var5 = a(var1, var2 + var4);
            break;
         case 3:
            var5 = c(var1, var2, var3);
            break;
         case 4:
            var5 = b(var1, var2, var3);
            break;
         case 5:
            if (var1) {
               var5 = a(var1, var2, var3);
            } else {
               var5 = a(var1, var2 + var4);
            }
            break;
         default:
            var5 = a(var1);
      }

      return var5;
   }

   public static Animation a(boolean var0) {
      float var2 = 1.0F;
      float var1;
      if (var0) {
         var1 = 0.0F;
      } else {
         var1 = 1.0F;
      }

      if (!var0) {
         var2 = 0.0F;
      }

      return new AlphaAnimation(var1, var2);
   }

   public static Animation a(boolean var0, int var1) {
      return new com.alensw.ui.view.i(var0, var1);
   }

   public static Animation a(boolean var0, int var1, int var2) {
      float var4 = 1.0F;
      float var5 = var1 / 2.0F;
      float var6 = var2 / 2.0F;
      float var3;
      if (var0) {
         var3 = 0.5F;
      } else {
         var3 = 1.0F;
      }

      if (!var0) {
         var4 = 0.5F;
      }

      return new com.alensw.ui.view.f(var3, var4 - var3, var5, var6);
   }

   public static Animation b(boolean var0, int var1, int var2) {
      float var4 = 0.0F;
      float var6 = var1 / 2.0F;
      float var7 = var2 / 2.0F;
      float var5 = var1 / 2.0F;
      float var3;
      if (var0) {
         var3 = 180.0F;
      } else {
         var3 = 0.0F;
      }

      if (!var0) {
         var4 = -180.0F;
      }

      return new com.alensw.ui.view.g(var3, var4, new Camera(), var5, var6, var7, var0);
   }

   public static Animation c(boolean var0, int var1, int var2) {
      float var4 = 0.0F;
      float var6 = var1 / 2.0F;
      float var5 = var2 / 2.0F;
      float var3 = var1 / 2.0F;
      if (var0) {
         var3 = 180.0F;
      } else {
         var3 = 0.0F;
      }

      if (!var0) {
         var4 = -180.0F;
      }

      return new com.alensw.ui.view.h(var3, var4, new Camera(), var6, var5, var0);
   }
}
