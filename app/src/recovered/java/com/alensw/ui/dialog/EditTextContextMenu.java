package com.alensw.ui.dialog;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import com.alensw.ui.menu.GalleryMenu;
final class EditTextContextMenu implements OnLongClickListener {
   @Override
   public boolean onLongClick(View var1) {
      EditText var4 = (EditText)var1;
      if ((var4.getInputType() & 128) == 0) {
         EditTextMenuItemClick var5 = new EditTextMenuItemClick(this, var4);
         GalleryMenu var3 = new GalleryMenu(var4.getContext());
         var3.add(0, 16908321, 0, 17039361);
         var3.add(0, 16908320, 0, 17039363);
         var3.add(0, 16908322, 0, 17039371);
         var3.add(0, 16908319, 0, 17039373);

         for (int var2 = var3.size() - 1; var2 >= 0; var2--) {
            var3.getItem(var2).setOnMenuItemClickListener(var5);
         }

         var3.a(var4);
      }

      return true;
   }
}
