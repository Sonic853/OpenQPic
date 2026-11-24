package com.alensw.updater;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class UpdateView$1 implements OnPreferenceClickListener {
   final UpdateView this$0;

   UpdateView$1(UpdateView var1) {
      this.this$0 = var1;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      GitHubUpdates.check(this.this$0, true);
      return true;
   }
}
