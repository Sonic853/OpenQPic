package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
@SuppressLint("Instantiatable")
class HomeButton extends ImageView {
   private int a;
   private int b = this.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);
   private boolean c;
   private boolean d;
   private final android.support.a.a.m e;

   public HomeButton(Context var1) {
      this(var1, null);
   }

   public HomeButton(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.e = new android.support.a.a.m(var1);
   }

   public void a(int var1) {
      this.a = var1;
      this.e.a(var1);
   }

   public void a(boolean var1) {
      this.c = var1;
      if (!this.d) {
         android.support.a.a.m var3 = this.e;
         float var2;
         if (this.c) {
            var2 = 1.0F;
         } else {
            var2 = 0.0F;
         }

         var3.b(var2);
         super.setImageDrawable(this.e);
      }
   }

   public void b(boolean var1) {
      if (var1) {
         super.setImageDrawable(com.b.a.b.a(this.getResources(), com.alensw.PicFolder.R.raw.ic_menu_done, this.a));
         super.setPadding(this.b, this.b, this.b, this.b);
      } else {
         super.setImageDrawable(this.e);
         super.setPadding(0, 0, 0, 0);
      }
   }

   @Override
   public void setImageDrawable(Drawable var1) {
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.d = var3;
      Object var4;
      if (var1 instanceof android.support.a.a.m) {
         ((android.support.a.a.m)var1).a(this.a);
         var4 = var1;
      } else {
         var4 = var1;
         if (var1 == null) {
            android.support.a.a.m var5 = this.e;
            float var2;
            if (this.c) {
               var2 = 1.0F;
            } else {
               var2 = 0.0F;
            }

            var5.b(var2);
            var4 = this.e;
         }
      }

      super.setImageDrawable((Drawable)var4);
   }
}
