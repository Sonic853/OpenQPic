package com.alensw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

final class LaunchMapTarget implements IntentTargetCallback {
   final String a;
   final String b;
   final String c;
   final Context d;

   LaunchMapTarget(String var1, String var2, String var3, Context var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      String var3 = var2.activityInfo.packageName;
      if ("com.google.android.apps.maps".equals(var3)) {
         var1.setData(Uri.parse(this.a));
      } else if (var3.startsWith("com.google.")) {
         var1.setData(Uri.parse(this.b + "&q=" + this.c));
      }

      GalleryActions.a(this.d, var1, var2);
   }
}
