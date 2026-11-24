package com.alensw.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import java.util.Comparator;

final class b implements Comparator {
   final PackageManager a;
   final Context b;
   final SharedPreferences c;
   private final DisplayNameComparator d;
   private final String e;

   b(PackageManager var1, Context var2, SharedPreferences var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = new DisplayNameComparator(this.a);
      this.e = this.b.getPackageName();
   }

   public int a(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
        boolean zEquals = this.e.equals(resolveInfo.activityInfo.packageName);
        boolean zEquals2 = this.e.equals(resolveInfo2.activityInfo.packageName);
        int i = this.c.getInt(resolveInfo2.activityInfo.name, zEquals2 ? 10 : 0) - this.c.getInt(resolveInfo.activityInfo.name, zEquals ? 10 : 0);
        if (i != 0) {
            return i;
        }
        int i2 = (zEquals ? 0 : 1) - (zEquals2 ? 0 : 1);
        return i2 == 0 ? this.d.compare(resolveInfo, resolveInfo2) : i2;
    }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((ResolveInfo)var1, (ResolveInfo)var2);
   }
}
