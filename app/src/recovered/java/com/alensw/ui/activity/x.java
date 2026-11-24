package com.alensw.ui.activity;

import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class x implements OnPreferenceClickListener {
   final Intent a;
   final MainPreference b;

   x(MainPreference var1, Intent var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      com.alensw.ui.activity.a.a(this.b, this.a);
      return true;
   }
}
