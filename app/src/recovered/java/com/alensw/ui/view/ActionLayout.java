package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@SuppressLint("Instantiatable")
class ActionLayout extends LinearLayout {
   private int a = this.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);

   public ActionLayout(Context var1) {
      this(var1, null);
   }

   public ActionLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   @Override
   public void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      int var7 = var4 - var2;
      int var8 = var5 - var3;
      if (var7 > 0 && var8 > 0 && this.getLayoutParams().width == -1) {
         int var9 = this.getChildCount();
         var2 = 0;
         var3 = 0;
         var4 = 0;

         while (var4 < var9) {
            View var10 = this.getChildAt(var4);
            int var6 = var2;
            var5 = var3;
            if (var10.getVisibility() != 8) {
               var5 = var3 + var10.getWidth();
               var6 = var2 + 1;
            }

            var4++;
            var2 = var6;
            var3 = var5;
         }

         if (var2 > 0) {
            var5 = Math.max(Math.min((var7 - var3) / var2, this.a), 0);
            if (var2 > 1) {
               var4 = var2 - 1;
            } else {
               var4 = 1;
            }

            int var21 = (var7 - var3 - var5 * var2) / var4;
            if (var2 == 1) {
               var2 = (var7 - var3) / 2;
            } else {
               var2 = 0;
            }

            var3 = 0;

            while (var3 < var9) {
               View var22 = this.getChildAt(var3);
               var4 = var2;
               if (var22.getVisibility() != 8) {
                  var4 = var22.getWidth() + var5;
                  if (var3 == var9 - 1) {
                     var22.layout(var7 - var4, 0, var7, var8);
                     var4 = var2;
                  } else {
                     var22.layout(var2, 0, var2 + var4, var8);
                     var4 = var2 + var4 + var21;
                  }
               }

               var3++;
               var2 = var4;
            }
         }
      }
   }
}
