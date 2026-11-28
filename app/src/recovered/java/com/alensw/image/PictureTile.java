package com.alensw.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

class PictureTile extends PictureBitmap {
   protected final boolean a;
   protected final int c;
   final TiledPicture d;

   public PictureTile(TiledPicture var1, int var2, int var3, int var4) {
      super(var3, var4, 0, 0);
      this.d = var1;
      this.a = false;
      this.c = var2;
   }

   public PictureTile(TiledPicture var1, int var2, Bitmap var3) {
      super(var3);
      this.d = var1;
      boolean var4;
      if (var3.hasAlpha() && var3.getConfig() == Config.ARGB_8888) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a = var4;
      this.c = var2;
   }

   @Override
   protected void a(Bitmap var1) {
      TiledPicture.b(var1);
   }

   @Override
   public String toString() {
      return super.toString() + ", tile=" + TileKey.e(this.c);
   }
}
