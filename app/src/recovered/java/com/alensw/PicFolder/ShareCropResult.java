package com.alensw.PicFolder;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.IntentTargetCallback;
class ShareCropResult implements IntentTargetCallback {
   final CropActivity a;

   ShareCropResult(CropActivity var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      this.a.a(GalleryActions.a(var1, var2));
   }
}
