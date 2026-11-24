package com.alensw.ui.view;

import android.view.View;
import android.view.View.OnClickListener;

class bx implements OnClickListener {
   final TitleBar a;

   bx(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.a(var1);
   }
}
