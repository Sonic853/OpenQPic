package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

final class SubmitTextInput implements OnClickListener {
   final TextInputCallback a;
   final EditText b;

   SubmitTextInput(TextInputCallback var1, EditText var2) {
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
