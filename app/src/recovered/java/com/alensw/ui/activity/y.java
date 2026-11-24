package com.alensw.ui.activity;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import java.util.Arrays;

class y implements OnPreferenceChangeListener {
   final ListPreference a;
   final MainPreference b;

   y(MainPreference var1, ListPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceChange(Preference var1, Object var2) {
      CharSequence[] var4 = this.a.getEntries();
      int var3 = Arrays.asList(this.a.getEntryValues()).indexOf(var2);
      if (var3 >= 0 && var3 < var4.length) {
         this.a.setSummary(var4[var3]);
      }

      return true;
   }
}
