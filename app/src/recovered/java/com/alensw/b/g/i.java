package com.alensw.b.g;

import android.widget.PopupWindow;

final class i implements Runnable {
   final PopupWindow a;
   final int b;

   i(PopupWindow var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void run() {
      try {
         this.a.update(this.b, -1);
      } catch (Throwable var2) {
      }
   }
}
