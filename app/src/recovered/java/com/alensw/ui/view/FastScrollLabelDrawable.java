package com.alensw.ui.view;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;

class FastScrollLabelDrawable extends PaintDrawable {
   final FastScroller a;
   private final Path b;

   FastScrollLabelDrawable(FastScroller var1) {
      this.a = var1;
      this.b = new Path();
   }

   @Override
   public void draw(Canvas var1) {
      super.draw(var1);
      Rect var4 = this.getBounds();
      float var2 = Math.min(var4.width(), var4.height()) / 6.0F;
      this.b.reset();
      if (FastScroller.a(this.a) == 1) {
         float var3 = var4.exactCenterX();
         this.b.moveTo(var3 - var2, var4.bottom);
         this.b.lineTo(var3, var4.bottom + var2);
         this.b.lineTo(var2 + var3, var4.bottom);
      } else if (FastScroller.a(this.a) == 2) {
         float var5 = var4.exactCenterY();
         this.b.moveTo(var4.right, var5 - var2);
         this.b.lineTo(var4.right + var2, var5);
         this.b.lineTo(var4.right, var2 + var5);
      }

      var1.drawPath(this.b, this.getPaint());
   }
}
