package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;

class be implements OnPreferenceClickListener {
   final PreferenceScreen a;
   final com.alensw.ui.activity.ba b;

   be(com.alensw.ui.activity.ba var1, PreferenceScreen var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      com.alensw.ui.activity.ba.a(this.b).push(this.b.getPreferenceScreen());
      this.b.a(this.a);
      return true;
   }
}
