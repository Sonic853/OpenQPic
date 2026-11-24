package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;

class aw implements OnClickListener {
   final com.alensw.ui.activity.ao a;

   aw(com.alensw.ui.activity.ao var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      if (this.a.q()) {
         this.a.d(false);
      }
   }
}
