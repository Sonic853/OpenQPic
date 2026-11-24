package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class l implements OnClickListener {
   final Runnable a;

   l(Runnable var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (this.a != null) {
         this.a.run();
      }
   }
}
