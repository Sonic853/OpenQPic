package com.alensw.ui.dialog;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;

class EditTextMenuItemClick implements OnMenuItemClickListener {
   final EditText a;
   final EditTextContextMenu b;

   EditTextMenuItemClick(EditTextContextMenu var1, EditText var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      return this.a.onTextContextMenuItem(var1.getItemId());
   }
}
