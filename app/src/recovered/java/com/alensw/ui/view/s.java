package com.alensw.ui.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class s implements OnClickListener {
   final ConfirmPreference a;

   s(ConfirmPreference var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (this.a.a != null) {
         this.a.a.run();
      }
   }
}
