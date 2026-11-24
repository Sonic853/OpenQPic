package com.alensw.ui.activity;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
class aj implements OnPreferenceChangeListener {
   final String a;
   final SharedPreferences b;
   final Integer c;
   final ListPreference d;
   final String e;
   final MainPreference f;

   aj(MainPreference var1, String var2, SharedPreferences var3, Integer var4, ListPreference var5, String var6) {
      this.f = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
      this.e = var6;
   }

   @Override
   public boolean onPreferenceChange(Preference var1, Object var2) {
      if (this.a.equals(var2)) {
         this.b.edit().putString("slideshow_interval", this.c.toString()).commit();
         com.alensw.ui.a.f.a(this.f, com.alensw.PicFolder.R.string.slideshow_interval, this.c, null, new com.alensw.ui.activity.ak(this));
      } else {
         this.d.setSummary(var2.toString() + ' ' + this.e);
      }

      return true;
   }
}
