package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

class z implements OnCancelListener {
   final Runnable a;
   final com.alensw.ui.a.x b;

   z(com.alensw.ui.a.x var1, Runnable var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onCancel(DialogInterface var1) {
      this.a.run();
   }
}
