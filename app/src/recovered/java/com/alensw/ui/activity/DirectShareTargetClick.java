package com.alensw.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import java.util.HashSet;
import java.util.List;

final class DirectShareTargetClick implements OnMenuItemClickListener {
   final List a;
   final IntentTargetCallback b;
   final HashSet c;
   final Intent d;
   final Intent e;
   final SharedPreferences f;

   DirectShareTargetClick(List var1, IntentTargetCallback var2, HashSet var3, Intent var4, Intent var5, SharedPreferences var6) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      ResolveInfo var3 = (ResolveInfo)this.a.get(var1.getItemId());
      IntentTargetCallback var4 = this.b;
      Intent var5;
      if (this.c.contains(var3)) {
         var5 = this.d;
      } else {
         var5 = this.e;
      }

      var4.a(var5, var3);
      int var2 = this.f.getInt(var3.activityInfo.name, 0);
      this.f.edit().putInt(var3.activityInfo.name, var2 + 1).commit();
      return false;
   }
}
