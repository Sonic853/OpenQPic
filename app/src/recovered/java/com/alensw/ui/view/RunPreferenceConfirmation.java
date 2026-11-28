package com.alensw.ui.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class RunPreferenceConfirmation implements OnClickListener {
   final ConfirmPreference a;

   RunPreferenceConfirmation(ConfirmPreference var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (this.a.a != null) {
         this.a.a.run();
      }
   }
}
