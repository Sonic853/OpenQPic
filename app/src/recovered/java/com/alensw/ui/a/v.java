package com.alensw.ui.a;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
final class v implements OnLongClickListener {
   @Override
   public boolean onLongClick(View var1) {
      EditText var4 = (EditText)var1;
      if ((var4.getInputType() & 128) == 0) {
         com.alensw.ui.a.w var5 = new com.alensw.ui.a.w(this, var4);
         com.alensw.b.g.b var3 = new com.alensw.b.g.b(var4.getContext());
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
