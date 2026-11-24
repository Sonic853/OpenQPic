package com.alensw.ui.a;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;

final class g implements OnShowListener {
   final Context a;
   final AlertDialog b;

   g(Context var1, AlertDialog var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void onShow(DialogInterface var1) {
      com.alensw.ui.a.u.a(this.a, this.b);
   }
}
