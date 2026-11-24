package com.alensw.PicFolder;

import android.view.View;
import android.view.animation.Interpolator;
class az extends com.alensw.ui.view.bt {
   final float a;
   final float b;
   final float c;
   final PlayerView d;

   az(PlayerView var1, View var2, Interpolator var3, float var4, float var5, float var6) {
      super(var2, var3);
      this.d = var1;
      this.a = var4;
      this.b = var5;
      this.c = var6;
   }

   @Override
   public void a(float var1) {
      PlayerView.a(this.d, this.a + this.b * var1);
      this.d.requestLayout();
   }

   @Override
   public void b() {
      PlayerView.a(this.d, this.c);
      this.d.requestLayout();
   }
}
