package com.alensw.PicFolder;

import com.alensw.ui.dialog.DimensionPairCallback;

class ApplyCropAspectRatio implements DimensionPairCallback {
   final SelectCropDimensions a;

   ApplyCropAspectRatio(SelectCropDimensions var1) {
      this.a = var1;
   }

   @Override
   public void a(int var1, int var2) {
      this.a.c.a(true, 0, 0, var1, var2, true);
   }
}
