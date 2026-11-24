package com.alensw.ui.a;

import android.widget.Toast;

class as implements Runnable {
   final String a;
   final com.alensw.ui.a.ao b;

   as(com.alensw.ui.a.ao var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      Toast.makeText(com.alensw.ui.a.ao.a(this.b), this.a, 1).show();
   }
}
