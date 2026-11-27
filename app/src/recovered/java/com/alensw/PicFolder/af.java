package com.alensw.PicFolder;

import android.view.View;
import android.view.animation.Interpolator;
class af extends com.alensw.ui.view.bt {
   final boolean a;
   final GalleryActivity b;

   af(GalleryActivity var1, View var2, Interpolator var3, boolean var4) {
      super(var2, var3);
      this.b = var1;
      this.a = var4;
   }

   @Override
   public void a() {
      GalleryDrawerToggle var2 = GalleryActivity.c(this.b);
      float var1;
      if (this.a) {
         var1 = 1.0F;
      } else {
         var1 = 0.0F;
      }

      var2.setProgress(var1);
   }

   @Override
   public void a(float var1) {
      float var2 = var1;
      if (this.a) {
         var2 = 1.0F - var1;
      }

      GalleryActivity.c(this.b).setProgress(var2);
   }
}
