package com.alensw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

class t implements OnClickListener {
   final com.alensw.ui.activity.p a;

   t(com.alensw.ui.activity.p var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      Intent var3 = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
      var3.putExtra("android.content.extra.SHOW_ADVANCED", true);
      com.alensw.ui.activity.a.a(this.a, var3, 4);
   }
}
