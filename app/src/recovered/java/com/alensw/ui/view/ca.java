package com.alensw.ui.view;

import android.view.View;
import android.view.View.OnClickListener;

class ca implements OnClickListener {
   final OnClickListener a;
   final TitleBar b;

   ca(TitleBar var1, OnClickListener var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onClick(View var1) {
      this.a.onClick(TitleBar.e(this.b));
   }
}
