package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.dialog.BackgroundProgressDialog;
class UnhideFolderDialog extends BackgroundProgressDialog {
   final String a;
   final MediaActionPage b;

   UnhideFolderDialog(MediaActionPage var1, Activity var2, String var3, int var4, String var5) {
      super(var2, var3, var4);
      this.b = var1;
      this.a = var5;
   }

   @Override
   protected void a() {
      if (QuickApp.s.e(this.a) == 0) {
         QuickApp.b(true);
      }
   }
}
