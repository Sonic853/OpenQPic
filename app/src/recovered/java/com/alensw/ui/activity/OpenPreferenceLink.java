package com.alensw.ui.activity;

import android.content.Intent;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class OpenPreferenceLink implements OnPreferenceClickListener {
   final Intent a;
   final MainPreference b;

   OpenPreferenceLink(MainPreference var1, Intent var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      GalleryActions.a(this.b, this.a);
      return true;
   }
}
