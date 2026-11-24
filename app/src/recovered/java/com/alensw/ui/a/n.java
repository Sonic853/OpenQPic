package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

final class n implements OnClickListener {
   final com.alensw.ui.a.s a;
   final EditText b;

   n(com.alensw.ui.a.s var1, EditText var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (var2 == -1) {
         this.a.a(this.b.getText().toString());
      }
   }
}
