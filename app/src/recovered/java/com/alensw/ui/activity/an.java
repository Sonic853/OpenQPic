package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class an implements OnPreferenceClickListener {
   final String a;
   final MainPreference b;

   an(MainPreference var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      if (!com.alensw.ui.activity.a.a(this.b, "android.intent.action.VIEW", "market://details?id=" + this.a)) {
         com.alensw.ui.activity.a.a(this.b, "android.intent.action.VIEW", "http://market.android.com/details?id=" + this.a);
      }

      return false;
   }
}
