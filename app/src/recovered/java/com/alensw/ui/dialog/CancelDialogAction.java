package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

final class CancelDialogAction implements OnCancelListener {
   final Runnable a;

   CancelDialogAction(Runnable var1) {
      this.a = var1;
   }

   @Override
   public void onCancel(DialogInterface var1) {
      if (this.a != null) {
         this.a.run();
      }
   }
}
