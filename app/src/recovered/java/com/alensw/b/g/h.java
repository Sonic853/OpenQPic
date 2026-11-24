package com.alensw.b.g;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.PopupWindow;

final class h implements OnKeyListener {
   final PopupWindow a;

   h(PopupWindow var1) {
      this.a = var1;
   }

   @Override
   public boolean onKey(View var1, int var2, KeyEvent var3) {
      boolean var4 = true;
      if (var3.getAction() == 1 && var2 == 82) {
         this.a.dismiss();
      } else {
         var4 = false;
      }

      return var4;
   }
}
