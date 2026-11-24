package com.alensw.ui.activity;

import android.preference.SwitchPreference;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.view.PasswordView;

class w implements com.alensw.ui.a.ad {
   final SwitchPreference a;
   final MainPreference b;

   w(MainPreference var1, SwitchPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean a(PasswordView var1, String var2) {
      if (var2.length() > 0) {
         this.b.getPreferenceManager().getSharedPreferences().edit().putString("password", var2).commit();
         this.a.setChecked(true);
         SwitchPreference var4 = this.a;
         int var3;
         if (Character.isDigit(var2.charAt(0))) {
            var3 = com.alensw.PicFolder.R.string.password_mode_pin;
         } else {
            var3 = com.alensw.PicFolder.R.string.password_mode_pattern;
         }

         var4.setSummary(var3);
         QuickApp.y = var2;
         com.alensw.ui.a.f.a(this.b, 17039380, (CharSequence)this.b.getString(com.alensw.PicFolder.R.string.password_stored));
      } else {
         com.alensw.ui.a.x.a(var1);
      }

      return true;
   }
}
