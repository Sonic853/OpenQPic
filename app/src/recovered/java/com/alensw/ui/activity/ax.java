package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;
class ax implements OnClickListener {
   final com.alensw.ui.activity.ao a;

   ax(com.alensw.ui.activity.ao var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      int var2 = var1.getId();
      var1.setId(16908319);
      this.a.onMenuItemSelected(0, com.alensw.b.g.l.a(var1));
      var1.setId(var2);
   }
}
