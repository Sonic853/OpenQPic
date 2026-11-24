package com.alensw.ui.activity;

import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
class f extends com.b.a.l {
   final boolean a;
   final com.alensw.ui.activity.e b;

   f(com.alensw.ui.activity.e var1, Picture var2, int var3, int var4, boolean var5) {
      super(var2, var3, var4);
      this.b = var1;
      this.a = var5;
   }

   @Override
   public void draw(Canvas var1) {
      Rect var3 = this.getBounds();
      com.alensw.compat.LegacyPlatform.save(var1, 1);
      float var2;
      if (this.a) {
         var2 = 270.0F;
      } else {
         var2 = 90.0F;
      }

      var1.rotate(var2, var3.exactCenterX(), var3.centerY());
      super.draw(var1);
      var1.restore();
   }
}
