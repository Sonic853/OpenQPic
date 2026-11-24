package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.io.File;

class ai implements OnClickListener {
   final com.alensw.ui.a.an a;
   final com.alensw.ui.a.ae b;

   ai(com.alensw.ui.a.ae var1, com.alensw.ui.a.an var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      File var3 = this.b.c.getPath();
      if (var3 != null) {
         this.a.a(var3);
      }
   }
}
