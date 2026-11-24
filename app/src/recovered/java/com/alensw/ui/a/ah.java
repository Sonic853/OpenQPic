package com.alensw.ui.a;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.io.File;

class ah implements OnItemClickListener {
   final com.alensw.ui.a.ae a;

   ah(com.alensw.ui.a.ae var1) {
      this.a = var1;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      this.a.c.setPath((File)this.a.k.h.get(var3));
      this.a.a(this.a.c.getPath());
   }
}
