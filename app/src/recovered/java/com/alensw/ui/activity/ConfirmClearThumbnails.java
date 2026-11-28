package com.alensw.ui.activity;

import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.dialog.GalleryDialogs;
class ConfirmClearThumbnails implements OnPreferenceClickListener {
   final SwitchPreference a;
   final MainPreference b;

   ConfirmClearThumbnails(MainPreference var1, SwitchPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onPreferenceClick(Preference var1) {
      QuickApp.r.b = this.a.isChecked();
      GalleryDialogs.a(this.b, com.alensw.PicFolder.R.string.clear_thumbnails, this.b.getString(com.alensw.PicFolder.R.string.clear_thumbnails_confirm), MainPreference.a(this.b), null);
      return true;
   }
}
