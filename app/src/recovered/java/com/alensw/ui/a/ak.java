package com.alensw.ui.a;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class ak implements OnClickListener {
   final Context a;
   final com.alensw.ui.a.an b;
   final com.alensw.ui.a.ae c;

   ak(com.alensw.ui.a.ae var1, Context var2, com.alensw.ui.a.an var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      com.alensw.ui.a.f.a(this.a, com.alensw.PicFolder.R.string.new_folder, "", null, new com.alensw.ui.a.al(this));
   }
}
