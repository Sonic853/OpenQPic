package com.alensw.ui.activity;

import com.alensw.ui.dialog.TextInputCallback;

class SetCustomSlideshowInterval implements TextInputCallback {
   final SlideshowIntervalPreference a;

   SetCustomSlideshowInterval(SlideshowIntervalPreference var1) {
      this.a = var1;
   }

   @Override
   public void a(String var1) {
      this.a.b.edit().putString("slideshow_interval", var1).commit();
      this.a.d.setSummary(var1 + ' ' + this.a.e);
   }
}
