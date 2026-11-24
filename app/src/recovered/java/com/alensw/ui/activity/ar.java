package com.alensw.ui.activity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
final class ar implements OnClickListener {
   final Activity a;

   ar(Activity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.onMenuItemSelected(0, new com.alensw.b.g.l(null, var1.getContext(), 0, 16908332, null));
   }
}
