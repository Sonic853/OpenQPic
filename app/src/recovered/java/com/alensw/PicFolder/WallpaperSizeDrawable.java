package com.alensw.PicFolder;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import com.alensw.utils.GalleryUtils;
class WallpaperSizeDrawable extends Drawable {
   final int a;
   final Point b;
   final float c;
   final int d;
   final int e;
   final WallpaperActivity f;

   WallpaperSizeDrawable(WallpaperActivity var1, int var2, Point var3, float var4, int var5, int var6) {
      this.f = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
      this.e = var6;
   }

   @Override
   public void draw(Canvas var1) {
      WallpaperActivity.a(this.f).setColor(this.f.Y & 16777215 | -1610612736);
      WallpaperActivity.b(this.f).set(this.getBounds());
      WallpaperActivity.b(this.f).inset(this.a, this.a);
      float var2 = WallpaperActivity.b(this.f).width();
      float var3 = this.b.x * var2 / this.b.y;
      WallpaperActivity.b(this.f).inset((var2 - var3) / 2.0F, 0.0F);
      WallpaperActivity.a(this.f).setTextSize(this.c);
      WallpaperActivity.a(this.f).setTextAlign(Align.CENTER);
      var1.drawText(
         Integer.toString(this.b.x) + "x" + this.b.y,
         WallpaperActivity.b(this.f).centerX(),
         WallpaperActivity.b(this.f).bottom - this.d * 2,
         WallpaperActivity.a(this.f)
      );
      GalleryUtils.a(var1, WallpaperActivity.b(this.f), this.d, WallpaperActivity.a(this.f));
   }

   @Override
   public int getIntrinsicHeight() {
      return this.e;
   }

   @Override
   public int getIntrinsicWidth() {
      return this.e;
   }

   @Override
   public int getOpacity() {
      return 0;
   }

   @Override
   public void setAlpha(int var1) {
   }

   @Override
   public void setColorFilter(ColorFilter var1) {
   }
}
