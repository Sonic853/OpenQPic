package com.alensw.ui.activity;

import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class OpenThemePicker implements OnPreferenceClickListener {
   final MainPreference a;

   OpenThemePicker(MainPreference var1) {
      this.a = var1;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      Intent var2 = new Intent(this.a, SelectThemeActivity.class);
      this.a.startActivityForResult(var2, 1001);
      return true;
   }
}
