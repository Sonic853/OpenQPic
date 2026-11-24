package com.alensw.updater;

import android.os.Bundle;
public class UpdateView extends com.alensw.ui.activity.ba {
   @Override
   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(com.alensw.PicFolder.R.layout.update_view);
      this.addPreferencesFromResource(com.alensw.PicFolder.R.xml.update_settings);
      this.findPreference("checkForUpdates").setOnPreferenceClickListener(new com.alensw.updater.UpdateView$1(this));
   }
}
