package com.alensw.ui.dialog;

import android.view.View;
import android.view.View.OnClickListener;

class OpenPickerBreadcrumb implements OnClickListener {
   final FolderPickerDialog a;

   OpenPickerBreadcrumb(FolderPickerDialog var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.a(this.a.c.getPath());
   }
}
