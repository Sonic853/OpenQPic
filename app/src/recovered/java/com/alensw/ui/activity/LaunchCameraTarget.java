package com.alensw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

final class LaunchCameraTarget implements IntentTargetCallback {
   final Context a;

   LaunchCameraTarget(Context var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      var1.setAction(null);
      GalleryActions.a(this.a, var1, var2);
   }
}
