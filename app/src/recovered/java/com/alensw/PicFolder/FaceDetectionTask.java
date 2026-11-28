package com.alensw.PicFolder;

import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import com.alensw.image.Picture;
class FaceDetectionTask implements Runnable {
   final Picture a;
   final CropImageView b;

   FaceDetectionTask(CropImageView var1, Picture var2) {
      this.b = var1;
      this.a = var2;
   }

   private Face a() {
      try {
         FaceDetector var2 = new FaceDetector(this.a.e(), this.a.f(), 1);
         Face[] var1 = new Face[1];
         if (var2.findFaces(this.a.d(), var1) > 0) {
            return var1[0];
         }
      } catch (Throwable var3) {
      }

      return null;
   }

   @Override
   public void run() {
      Face var1 = this.a();
      this.b.s.post(new ApplyDetectedFaceCrop(this, var1));
   }
}
