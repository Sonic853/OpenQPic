package com.alensw.ui.shape;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build.VERSION;
import com.alensw.image.BitmapUtils;
final class CircularClippedDrawable extends InsetDrawable {
   private final Path a = new Path();
   private Bitmap b;
   private Canvas c;
   private int d = 0;

   CircularClippedDrawable(Drawable var1, int var2) {
      super(var1, var2);
   }

   @TargetApi(11)
   private boolean a(Canvas var1) {
      boolean var2;
      if (VERSION.SDK_INT < 18 && VERSION.SDK_INT >= 11 && var1.isHardwareAccelerated()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   @Override
   public void draw(Canvas var1) {
      Rect var7 = this.getBounds();
      boolean var6 = this.a(var1);
      int var5 = var7.width();
      int var4 = var7.height();
      int var3 = Math.min(var5, var4);
      if (this.d != var3) {
         float var2 = 39.0F * var3 / 96.0F;
         this.a.reset();
         this.a.addCircle(var7.exactCenterX(), var7.exactCenterY(), var2, Direction.CW);
         this.d = var3;
         if (!var6) {
            if (this.b != null) {
               this.b.recycle();
            }

            this.b = BitmapUtils.a(var3, var3, Config.ARGB_8888);
            Canvas var8;
            if (this.b != null) {
               var8 = new Canvas(this.b);
            } else {
               var8 = null;
            }

            this.c = var8;
         }
      }

      if (this.b != null) {
         this.b.eraseColor(0);
      }

      Canvas var9;
      if (!var6 && this.c != null) {
         var9 = this.c;
      } else {
         var9 = var1;
      }

      com.alensw.compat.LegacyPlatform.save(var9, 2);
      if (var6 || var9 == this.c) {
         var9.clipPath(this.a);
      }

      super.draw(var9);
      var9.restore();
      if (var9 == this.c && this.b != null) {
         var1.drawBitmap(this.b, ((float)var5 - var3) / 2.0F, ((float)var4 - var3) / 2.0F, null);
      }
   }
}
