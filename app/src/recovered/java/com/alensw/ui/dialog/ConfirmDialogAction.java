package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class ConfirmDialogAction implements OnClickListener {
   final Runnable a;

   ConfirmDialogAction(Runnable var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (this.a != null) {
         this.a.run();
      }
   }
}
