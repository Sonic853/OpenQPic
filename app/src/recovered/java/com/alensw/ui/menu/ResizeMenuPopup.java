package com.alensw.ui.menu;

import android.widget.PopupWindow;

final class ResizeMenuPopup implements Runnable {
   final PopupWindow a;
   final int b;

   ResizeMenuPopup(PopupWindow var1, int var2) {
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
