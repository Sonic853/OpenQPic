package com.alensw.ui.dialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.io.File;

class OpenPickerFolder implements OnItemClickListener {
   final FolderPickerDialog a;

   OpenPickerFolder(FolderPickerDialog var1) {
      this.a = var1;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      this.a.c.setPath((File)this.a.k.h.get(var3));
      this.a.a(this.a.c.getPath());
   }
}
