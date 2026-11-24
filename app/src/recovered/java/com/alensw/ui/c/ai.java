package com.alensw.ui.c;

import android.database.ContentObserver;
import android.os.Handler;

class ai extends ContentObserver {
   final com.alensw.ui.c.z a;

   ai(com.alensw.ui.c.z var1, Handler var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   public void onChange(boolean var1) {
      super.onChange(var1);
      this.a.b.removeCallbacks(com.alensw.ui.c.z.c(this.a));
      this.a.b.postDelayed(com.alensw.ui.c.z.c(this.a), 300L);
   }
}
