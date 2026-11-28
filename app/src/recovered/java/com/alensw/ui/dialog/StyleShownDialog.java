package com.alensw.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;

final class StyleShownDialog implements OnShowListener {
   final Context a;
   final AlertDialog b;

   StyleShownDialog(Context var1, AlertDialog var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void onShow(DialogInterface var1) {
      GalleryAlertDialog.a(this.a, this.b);
   }
}
