package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
final class i implements OnClickListener {
   final EditText a;
   final EditText b;
   final com.alensw.ui.a.t c;

   i(EditText var1, EditText var2, com.alensw.ui.a.t var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (var2 == -1) {
         var2 = com.alensw.b.l.b.a(this.a.getText().toString(), 0);
         int var3 = com.alensw.b.l.b.a(this.b.getText().toString(), 0);
         this.c.a(var2, var3);
      }
   }
}
