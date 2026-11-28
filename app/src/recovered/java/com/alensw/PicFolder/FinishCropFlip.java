package com.alensw.PicFolder;

class FinishCropFlip implements Runnable {
   final CropImageView a;

   FinishCropFlip(CropImageView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.e = true;
      this.a.a();
   }
}
