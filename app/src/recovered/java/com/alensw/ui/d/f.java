package com.alensw.ui.d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class f extends com.alensw.ui.d.a {
   private float g;
   private float h;
   private String i;

   public f(String var1) {
      this.i = var1;
   }

   @Override
   protected Object a(float var1, float var2) {
      this.a = 6.0F;
      return this.i;
   }

   @Override
   protected void a(Canvas var1, Paint var2, float var3, float var4) {
      float var5 = Math.min(var3, var4) / 96.0F * 72.0F;
      var2.setTextAlign(Align.CENTER);
      var2.setTextSize(var5);
      this.g = var3 / 2.0F;
      var3 = var4 / 2.0F;
      this.h = var5 / 2.0F + var3 - var2.descent() * 3.0F / 4.0F;
      var1.translate(this.g, this.h);
   }

   @Override
   protected void b(Canvas var1, Paint var2, float var3, float var4) {
      var1.translate(-this.g, -this.h);
   }
}
