package com.alensw.ui.activity;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
class ae implements OnPreferenceClickListener {
   final SwitchPreference a;
   final SharedPreferences b;
   final MainPreference c;

   ae(MainPreference var1, SwitchPreference var2, SharedPreferences var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      if (this.a.isChecked()) {
         this.b.edit().remove("password").commit();
         this.a.setChecked(false);
         this.a.setSummary(null);
         com.alensw.ui.activity.af var3 = new com.alensw.ui.activity.af(this);
         com.alensw.b.g.b var2 = new com.alensw.b.g.b(this.c);
         var2.add(0, 1, 0, com.alensw.PicFolder.R.string.password_mode_pattern).setOnMenuItemClickListener(var3);
         var2.add(0, 2, 0, com.alensw.PicFolder.R.string.password_mode_pin).setOnMenuItemClickListener(var3);
         var2.a(this.c.getString(com.alensw.PicFolder.R.string.password_mode));
      } else if (this.b.getString("password", "").length() > 0) {
         this.a.setChecked(true);
         com.alensw.ui.a.x.a(this.c, "password_protect", new com.alensw.ui.activity.ag(this), null);
      }

      return true;
   }
}
