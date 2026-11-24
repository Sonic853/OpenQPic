package com.alensw.ui.c;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
class dj implements OnClickListener {
   final com.alensw.ui.c.DateChangedCallback a;
   final com.alensw.ui.c.df b;

   dj(com.alensw.ui.c.df var1, com.alensw.ui.c.DateChangedCallback var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      com.alensw.ui.a.f.a(com.alensw.ui.c.df.a(this.b), com.alensw.PicFolder.R.string.fix_date, com.alensw.ui.c.df.a(this.b).getString(com.alensw.PicFolder.R.string.fix_date_confirm), new com.alensw.ui.c.dk(this), null);
   }
}
