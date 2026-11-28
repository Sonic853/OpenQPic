package com.alensw.ui.activity;

import android.graphics.Canvas;
import android.graphics.Picture;
import com.alensw.svg.SvgDrawable;
final class RotateLeftIcon extends SvgDrawable {
   RotateLeftIcon(Picture var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   @Override
   public void draw(Canvas var1) {
      com.alensw.compat.LegacyPlatform.save(var1, 1);
      var1.scale(-1.0F, 1.0F, this.getBounds().exactCenterX(), 0.0F);
      super.draw(var1);
      var1.restore();
   }
}
