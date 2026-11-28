package com.alensw.ui.page;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.IntentTargetCallback;
class LaunchSetAsTarget implements IntentTargetCallback {
   final Uri a;
   final MediaDetails b;

   LaunchSetAsTarget(MediaDetails var1, Uri var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if (VERSION.SDK_INT >= 19 && "android.service.wallpaper.CROP_AND_SET_WALLPAPER".equals(var1.getAction())) {
         try {
            var1 = WallpaperManager.getInstance(MediaDetails.a(this.b)).getCropAndSetWallpaperIntent(this.a);
            GalleryActions.a(MediaDetails.a(this.b), var1);
         } catch (Throwable var3) {
         }
      } else {
         GalleryActions.a(MediaDetails.a(this.b), var1, var2);
      }
   }
}
