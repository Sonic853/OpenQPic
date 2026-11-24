package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
class ac implements OnPreferenceClickListener {
   final SwitchPreference a;
   final MainPreference b;

   ac(MainPreference var1, SwitchPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      if (this.a.isChecked()) {
         com.alensw.ui.a.f.a(this.b, 17039380, this.b.getString(com.alensw.PicFolder.R.string.auto_hide_navbar_tips), null, new com.alensw.ui.activity.ad(this));
      }

      return true;
   }
}
