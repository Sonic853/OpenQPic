package com.alensw.PicFolder;

import android.graphics.PointF;
import android.media.FaceDetector.Face;

class ApplyDetectedFaceCrop implements Runnable {
   final Face a;
   final com.alensw.PicFolder.FaceDetectionTask b;

   ApplyDetectedFaceCrop(com.alensw.PicFolder.FaceDetectionTask var1, Face var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.a != null) {
         float var2 = Math.max(this.a.eyesDistance() * 2.5F, 56.0F * CropImageView.a(this.b.b));
         float var1;
         if (this.b.b.b != 0.0F) {
            var1 = var2 / this.b.b.b;
         } else {
            var1 = var2;
         }

         PointF var3 = new PointF();
         this.a.getMidPoint(var3);
         var3.offset(CropImageView.b(this.b.b).left, CropImageView.c(this.b.b).top);
         this.b.b.v.set(var3.x - var2, var3.y - var1, var2 + var3.x, var1 + var3.y);
         this.b.b.c();
      }

      if (this.b.a != null) {
         this.b.a.m();
      }

      this.b.b.a(0);
   }
}
