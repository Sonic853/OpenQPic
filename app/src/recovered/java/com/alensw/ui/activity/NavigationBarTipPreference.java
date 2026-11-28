package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
import com.alensw.ui.dialog.GalleryDialogs;
class NavigationBarTipPreference implements OnPreferenceClickListener {
   final SwitchPreference a;
   final MainPreference b;

   NavigationBarTipPreference(MainPreference var1, SwitchPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      if (this.a.isChecked()) {
         GalleryDialogs.a(this.b, 17039380, this.b.getString(com.alensw.PicFolder.R.string.auto_hide_navbar_tips), null, new RevertAutoHideNavigation(this));
      }

      return true;
   }
}
