package com.alensw.ui.activity;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.dialog.BackgroundProgressDialog;
class ClearPreviewCacheDialog extends BackgroundProgressDialog {
   final ConfirmClearPreviewCache a;

   ClearPreviewCacheDialog(ConfirmClearPreviewCache var1, Activity var2, String var3, int var4) {
      super(var2, var3, var4);
      this.a = var1;
   }

   @Override
   protected void a() {
      if (QuickApp.u != null) {
         QuickApp.u.c();
      }
   }
}
