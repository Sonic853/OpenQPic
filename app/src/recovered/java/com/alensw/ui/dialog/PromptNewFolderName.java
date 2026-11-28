package com.alensw.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class PromptNewFolderName implements OnClickListener {
   final Context a;
   final FolderSelectionCallback b;
   final FolderPickerDialog c;

   PromptNewFolderName(FolderPickerDialog var1, Context var2, FolderSelectionCallback var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      GalleryDialogs.a(this.a, com.alensw.PicFolder.R.string.new_folder, "", null, new CreatePickerFolder(this));
   }
}
