package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class CancelProgressDialog implements OnClickListener {
   final BackgroundProgressDialog a;

   CancelProgressDialog(BackgroundProgressDialog var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      var1.cancel();
   }
}
