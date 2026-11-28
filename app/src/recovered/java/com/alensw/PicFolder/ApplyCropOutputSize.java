package com.alensw.PicFolder;

import com.alensw.ui.dialog.DimensionPairCallback;

class ApplyCropOutputSize implements DimensionPairCallback {
   final SelectCropDimensions a;

   ApplyCropOutputSize(SelectCropDimensions var1) {
      this.a = var1;
   }

   @Override
   public void a(int var1, int var2) {
      this.a.c.a(true, var1, var2, 0, 0, true);
   }
}
