package com.alensw.ui.activity;

import android.view.View.OnSystemUiVisibilityChangeListener;

class ay implements OnSystemUiVisibilityChangeListener {
   final com.alensw.ui.activity.ao a;

   ay(com.alensw.ui.activity.ao var1) {
      this.a = var1;
   }

   @Override
   public void onSystemUiVisibilityChange(int var1) {
      this.a.d(var1);
   }
}
