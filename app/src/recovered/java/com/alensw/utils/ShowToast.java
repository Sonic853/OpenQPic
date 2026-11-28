package com.alensw.utils;

import android.content.Context;
import android.widget.Toast;

final class ShowToast implements Runnable {
   final Context a;
   final String b;

   ShowToast(Context var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void run() {
      try {
         Toast.makeText(this.a, this.b, 1).show();
      } catch (Throwable var2) {
      }
   }
}
