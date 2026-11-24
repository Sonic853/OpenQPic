package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class ap implements OnClickListener {
   final com.alensw.ui.a.ao a;

   ap(com.alensw.ui.a.ao var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      var1.cancel();
   }
}
