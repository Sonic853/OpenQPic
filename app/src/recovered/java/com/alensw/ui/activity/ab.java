package com.alensw.ui.activity;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class ab implements OnPreferenceClickListener {
   final SharedPreferences a;
   final MainPreference b;

   ab(MainPreference var1, SharedPreferences var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      int var2;
      switch (this.a.getInt("default_sort", 0)) {
         case 2:
            var3 = true;
         case 4:
            var2 = com.alensw.PicFolder.R.id.sort_by_name;
            break;
         case 3:
            var4 = true;
         case 1:
            var2 = com.alensw.PicFolder.R.id.sort_by_date;
            var3 = var4;
            break;
         case 5:
            var5 = true;
         case 6:
            var2 = com.alensw.PicFolder.R.id.sort_by_exif;
            var3 = var5;
            break;
         case 7:
            var6 = true;
         case 8:
            var2 = com.alensw.PicFolder.R.id.sort_by_size;
            var3 = var6;
            break;
         default:
            var2 = com.alensw.PicFolder.R.id.sort_default;
            var3 = var7;
      }

      com.alensw.ui.activity.a.a(this.b, null, new int[]{com.alensw.PicFolder.R.id.sort_default, com.alensw.PicFolder.R.id.sort_by_name, com.alensw.PicFolder.R.id.sort_by_date, com.alensw.PicFolder.R.id.sort_by_exif, com.alensw.PicFolder.R.id.sort_by_size}, var2, var3);
      return true;
   }
}
