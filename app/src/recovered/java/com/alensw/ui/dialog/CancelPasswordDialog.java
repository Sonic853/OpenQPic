package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

class CancelPasswordDialog implements OnCancelListener {
   final Runnable a;
   final GalleryPasswordDialog b;

   CancelPasswordDialog(GalleryPasswordDialog var1, Runnable var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onCancel(DialogInterface var1) {
      this.a.run();
   }
}
