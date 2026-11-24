package com.alensw.ui.view;

import android.graphics.Color;
import android.view.View;
import android.view.animation.Interpolator;

public abstract class r extends com.alensw.ui.view.bt {
   private final int a;
   private final int b;
   private final int c;
   private final int d;
   private final int e;
   private final int f;
   private final int g;
   private final int h;

   public r(View var1, Interpolator var2, int var3, int var4) {
      super(var1, var2);
      this.a = Color.alpha(var3);
      this.b = Color.red(var3);
      this.c = Color.green(var3);
      this.d = Color.blue(var3);
      this.e = Color.alpha(var4) - this.a;
      this.f = Color.red(var4) - this.b;
      this.g = Color.green(var4) - this.c;
      this.h = Color.blue(var4) - this.d;
   }

   @Override
   public void a(float var1) {
      this.a(Color.argb(this.a + (int)(this.e * var1), this.b + (int)(this.f * var1), this.c + (int)(this.g * var1), this.d + (int)(this.h * var1)));
   }

   public abstract void a(int var1);
}
