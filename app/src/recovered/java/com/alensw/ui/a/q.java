package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class q implements OnClickListener {
   final com.alensw.ui.a.p a;

   q(com.alensw.ui.a.p var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      this.a.d.setText((CharSequence)this.a.c.getItem(var2));
   }
}
