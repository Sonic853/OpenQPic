package com.alensw.ui.c;

import android.view.View;
import android.view.View.OnFocusChangeListener;

class dq implements OnFocusChangeListener {
   final com.alensw.ui.c.dp a;

   dq(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   @Override
   public void onFocusChange(View var1, boolean var2) {
      this.a.C.u();
      if (this.a.H == 2) {
         com.alensw.ui.c.cx var3 = this.a.C;
         if (!var2) {
            var2 = true;
         } else {
            var2 = false;
         }

         var3.a(var2);
      }
   }
}
