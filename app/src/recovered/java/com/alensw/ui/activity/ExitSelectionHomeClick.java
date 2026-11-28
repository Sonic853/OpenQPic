package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;

class ExitSelectionHomeClick implements OnClickListener {
   final GalleryChromeActivity a;

   ExitSelectionHomeClick(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      if (this.a.q()) {
         this.a.d(false);
      }
   }
}
