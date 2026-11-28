package com.alensw.PicFolder;

import android.graphics.Bitmap.Config;
import com.alensw.image.Picture;
class LoadCropPreview implements Runnable {
   final CropImageView a;

   LoadCropPreview(CropImageView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      Picture var1 = this.a.a(Config.RGB_565);
      if (var1 != null) {
         this.a.h = false;
         this.a.a(var1);
         var1.m();
      }
   }
}
