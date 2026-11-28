package com.alensw.ui.page;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import com.alensw.PicFolder.CropActivity;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.IntentTargetCallback;
class LaunchMediaEditor implements IntentTargetCallback {
   final Bundle a;
   final MediaDetails b;

   LaunchMediaEditor(MediaDetails var1, Bundle var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if (CropActivity.class.getName().equals(var2.activityInfo.name)) {
         var1.setClass(MediaDetails.a(this.b), CropActivity.class);
         if (this.a != null) {
            var1.putExtras(this.a);
         }

         GalleryActions.a((Activity)MediaDetails.a(this.b), var1, 2);
      } else {
         GalleryActions.a(MediaDetails.a(this.b), var1, var2);
      }
   }
}
