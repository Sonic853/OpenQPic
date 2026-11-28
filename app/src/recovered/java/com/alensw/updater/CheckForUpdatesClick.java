package com.alensw.updater;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

class CheckForUpdatesClick implements OnPreferenceClickListener {
   final UpdateView this$0;

   CheckForUpdatesClick(UpdateView var1) {
      this.this$0 = var1;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      GitHubUpdates.check(this.this$0, true);
      return true;
   }
}
