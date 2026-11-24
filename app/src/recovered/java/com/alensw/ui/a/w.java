package com.alensw.ui.a;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;

class w implements OnMenuItemClickListener {
   final EditText a;
   final com.alensw.ui.a.v b;

   w(com.alensw.ui.a.v var1, EditText var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      return this.a.onTextContextMenuItem(var1.getItemId());
   }
}
