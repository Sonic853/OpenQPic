package com.alensw.ui.activity;

import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import com.alensw.svg.SvgDrawable;
final class VerticalFlipIcon extends SvgDrawable {
   VerticalFlipIcon(Picture var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   @Override
   public void draw(Canvas var1) {
      Rect var2 = this.getBounds();
      com.alensw.compat.LegacyPlatform.save(var1, 1);
      var1.rotate(270.0F, var2.exactCenterX(), var2.centerY());
      super.draw(var1);
      var1.restore();
   }
}
