package com.alensw.ui.c;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
class dm implements com.alensw.ui.activity.o {
   final Uri a;
   final com.alensw.ui.c.df b;

   dm(com.alensw.ui.c.df var1, Uri var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if (VERSION.SDK_INT >= 19 && "android.service.wallpaper.CROP_AND_SET_WALLPAPER".equals(var1.getAction())) {
         try {
            var1 = WallpaperManager.getInstance(com.alensw.ui.c.df.a(this.b)).getCropAndSetWallpaperIntent(this.a);
            com.alensw.ui.activity.a.a(com.alensw.ui.c.df.a(this.b), var1);
         } catch (Throwable var3) {
         }
      } else {
         com.alensw.ui.activity.a.a(com.alensw.ui.c.df.a(this.b), var1, var2);
      }
   }
}
