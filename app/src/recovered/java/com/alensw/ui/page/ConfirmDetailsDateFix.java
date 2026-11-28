package com.alensw.ui.page;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.alensw.ui.dialog.GalleryDialogs;
class ConfirmDetailsDateFix implements OnClickListener {
   final DateChangedCallback a;
   final MediaDetails b;

   ConfirmDetailsDateFix(MediaDetails var1, DateChangedCallback var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      GalleryDialogs.a(MediaDetails.a(this.b), com.alensw.PicFolder.R.string.fix_date, MediaDetails.a(this.b).getString(com.alensw.PicFolder.R.string.fix_date_confirm), new ApplyDetailsDateFix(this), null);
   }
}
