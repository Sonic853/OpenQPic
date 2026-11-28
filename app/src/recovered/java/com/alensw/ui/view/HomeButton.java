package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.alensw.svg.SvgPicture;
@SuppressLint("Instantiatable")
class HomeButton extends ImageView {
   private int a;
   private int b = this.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);
   private boolean c;
   private boolean d;
   private final GalleryArrow e;

   public HomeButton(Context var1) {
      this(var1, null);
   }

   public HomeButton(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.e = new GalleryArrow(var1);
   }

   public void a(int var1) {
      this.a = var1;
      this.e.setColor(var1);
   }

   public void a(boolean var1) {
      this.c = var1;
      if (!this.d) {
         GalleryArrow var3 = this.e;
         float var2;
         if (this.c) {
            var2 = 1.0F;
         } else {
            var2 = 0.0F;
         }

         var3.setProgress(var2);
         super.setImageDrawable(this.e);
      }
   }

   public void b(boolean var1) {
      if (var1) {
         super.setImageDrawable(SvgPicture.a(this.getResources(), com.alensw.PicFolder.R.raw.ic_menu_done, this.a));
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
      if (var1 instanceof GalleryArrow) {
         ((GalleryArrow)var1).setColor(this.a);
         var4 = var1;
      } else {
         var4 = var1;
         if (var1 == null) {
            GalleryArrow var5 = this.e;
            float var2;
            if (this.c) {
               var2 = 1.0F;
            } else {
               var2 = 0.0F;
            }

            var5.setProgress(var2);
            var4 = this.e;
         }
      }

      super.setImageDrawable((Drawable)var4);
   }
}
