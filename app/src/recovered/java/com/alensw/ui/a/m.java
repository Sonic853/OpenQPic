package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

final class m implements OnCancelListener {
   final Runnable a;

   m(Runnable var1) {
      this.a = var1;
   }

   @Override
   public void onCancel(DialogInterface var1) {
      if (this.a != null) {
         this.a.run();
      }
   }
}
