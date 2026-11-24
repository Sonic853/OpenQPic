package com.alensw.PicFolder;

import android.content.Intent;
import android.content.pm.ResolveInfo;
class l implements com.alensw.ui.activity.o {
   final CropActivity a;

   l(CropActivity var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      this.a.a(com.alensw.ui.activity.a.a(var1, var2));
   }
}
