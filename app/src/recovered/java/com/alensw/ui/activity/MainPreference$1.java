package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class MainPreference$1 implements OnPreferenceClickListener {
   final MainPreference this$0;

   MainPreference$1(MainPreference var1) {
      this.this$0 = var1;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      return this.this$0.isBatteryOptimizationEnabled(true);
   }
}
