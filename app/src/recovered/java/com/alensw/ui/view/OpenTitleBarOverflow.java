package com.alensw.ui.view;

import android.view.View;
import android.view.View.OnClickListener;

class OpenTitleBarOverflow implements OnClickListener {
   final TitleBar a;

   OpenTitleBarOverflow(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.a(var1);
   }
}
