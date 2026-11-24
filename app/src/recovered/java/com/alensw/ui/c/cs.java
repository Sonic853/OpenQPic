package com.alensw.ui.c;

import android.view.View;
import android.view.View.OnClickListener;
class cs implements OnClickListener {
   final com.alensw.ui.c.bz a;

   cs(com.alensw.ui.c.bz var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      if (this.a.o) {
         this.a.b(new com.alensw.a.e(this.a.k.getPath(), 0));
      }
   }
}
