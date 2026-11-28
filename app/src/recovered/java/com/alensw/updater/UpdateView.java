package com.alensw.updater;

import android.os.Bundle;
import com.alensw.ui.activity.GalleryPreferenceActivity;
public class UpdateView extends GalleryPreferenceActivity {
   @Override
   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(com.alensw.PicFolder.R.layout.update_view);
      this.addPreferencesFromResource(com.alensw.PicFolder.R.xml.update_settings);
      this.findPreference("checkForUpdates").setOnPreferenceClickListener(new CheckForUpdatesClick(this));
   }
}
