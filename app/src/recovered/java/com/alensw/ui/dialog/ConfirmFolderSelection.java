package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.io.File;

class ConfirmFolderSelection implements OnClickListener {
   final FolderSelectionCallback a;
   final FolderPickerDialog b;

   ConfirmFolderSelection(FolderPickerDialog var1, FolderSelectionCallback var2) {
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
