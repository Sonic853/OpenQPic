package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;

class OpenPreferenceScreen implements OnPreferenceClickListener {
   final PreferenceScreen a;
   final GalleryPreferenceActivity b;

   OpenPreferenceScreen(GalleryPreferenceActivity var1, PreferenceScreen var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      GalleryPreferenceActivity.a(this.b).push(this.b.getPreferenceScreen());
      this.b.a(this.a);
      return true;
   }
}
