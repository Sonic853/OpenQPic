package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;
class ap implements OnClickListener {
   final com.alensw.ui.activity.ao a;

   ap(com.alensw.ui.activity.ao var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.onMenuItemSelected(0, com.alensw.b.g.l.a(var1));
   }
}
