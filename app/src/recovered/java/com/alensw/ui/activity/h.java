package com.alensw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

final class h implements com.alensw.ui.activity.o {
   final Context a;

   h(Context var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      var1.setAction(null);
      com.alensw.ui.activity.a.a(this.a, var1, var2);
   }
}
