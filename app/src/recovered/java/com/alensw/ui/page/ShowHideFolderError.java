package com.alensw.ui.page;

import android.widget.Toast;

class ShowHideFolderError implements Runnable {
   final Object a;
   final HideFolderDialog b;

   ShowHideFolderError(HideFolderDialog var1, Object var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.a instanceof String) {
         Toast.makeText(this.b.f.C, (String)this.a, 1).show();
      } else {
         Toast.makeText(this.b.f.C, (Integer)this.a, 1).show();
      }
   }
}
