package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class SetSelectedChoice implements OnClickListener {
   final OpenChoiceList a;

   SetSelectedChoice(OpenChoiceList var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      this.a.d.setText((CharSequence)this.a.c.getItem(var2));
   }
}
