package com.alensw.ui.activity;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
import com.alensw.ui.dialog.GalleryPasswordDialog;
class ProtectedSwitchClick implements OnPreferenceClickListener {
   final SharedPreferences a;
   final MainPreference b;

   ProtectedSwitchClick(MainPreference var1, SharedPreferences var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      SwitchPreference var2 = (SwitchPreference)var1;
      if (!var2.isChecked() && this.a.getString("password", "").length() > 0) {
         var2.setChecked(true);
         GalleryPasswordDialog.a(this.b, var2.getKey(), new DisableProtectedSwitch(this, var2), null);
      }

      return true;
   }
}
