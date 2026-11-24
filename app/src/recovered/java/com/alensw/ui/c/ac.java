package com.alensw.ui.c;

import android.view.View;
import android.view.View.OnClickListener;

class ac implements OnClickListener {
   final com.alensw.ui.c.z a;

   ac(com.alensw.ui.c.z var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.g.setVisibility(8);
      this.a.g.startAnimation(this.a.C.c(false));
   }
}
