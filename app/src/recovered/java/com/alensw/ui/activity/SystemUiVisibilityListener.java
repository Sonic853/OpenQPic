package com.alensw.ui.activity;

import android.view.View.OnSystemUiVisibilityChangeListener;

class SystemUiVisibilityListener implements OnSystemUiVisibilityChangeListener {
   final GalleryChromeActivity a;

   SystemUiVisibilityListener(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public void onSystemUiVisibilityChange(int var1) {
      this.a.d(var1);
   }
}
