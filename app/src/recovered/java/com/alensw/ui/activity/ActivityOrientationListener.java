package com.alensw.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import com.alensw.ui.view.DeviceOrientationMonitor;
class ActivityOrientationListener extends DeviceOrientationMonitor {
   final GalleryIntentActivity a;

   ActivityOrientationListener(GalleryIntentActivity var1, Context var2) {
      super(var2);
      this.a = var1;
   }

   @TargetApi(9)
   @Override
   public void a(int var1) {
      byte var3 = 0;
      int var2 = var1;
      if (this.a.B == 2) {
         var2 = ++var1;
         if (var1 > 3) {
            var2 = 0;
         }
      }

      byte var5 = var3;
      switch (var2) {
         case 0:
            var5 = 1;
         case 1:
            break;
         case 2:
            var5 = 9;
            break;
         case 3:
            var5 = 8;
            break;
         default:
            var5 = 4;
      }

      this.a.setRequestedOrientation(var5);
   }
}
