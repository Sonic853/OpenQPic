package com.alensw.ui.dialog;

import android.widget.Toast;

class ShowProgressMessage implements Runnable {
   final String a;
   final BackgroundProgressDialog b;

   ShowProgressMessage(BackgroundProgressDialog var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      Toast.makeText(BackgroundProgressDialog.a(this.b), this.a, 1).show();
   }
}
