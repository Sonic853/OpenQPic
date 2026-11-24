package com.alensw.ui.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;

class at implements OnMultiChoiceClickListener {
   final MultiListPreference a;

   at(MultiListPreference var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2, boolean var3) {
      MultiListPreference.a(this.a)[var2] = var3;
   }
}
