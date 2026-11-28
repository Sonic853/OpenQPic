package com.alensw.PicFolder;

class FinishCropRotation implements Runnable {
   final CropImageView a;

   FinishCropRotation(CropImageView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a(0, false);
      this.a.e = true;
      this.a.a();
   }
}
