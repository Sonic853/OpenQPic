package com.alensw.ui.view;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

class TitleBarActionTooltip implements OnLongClickListener {
   final TitleBar a;

   TitleBarActionTooltip(TitleBar var1) {
      this.a = var1;
   }

   @Override
   public boolean onLongClick(View var1) {
      boolean var4;
      if (this.a.d()) {
         MenuItem var6 = TitleBar.a(this.a).findItem(var1.getId());
         if (var6 == null) {
            var4 = false;
         } else {
            int var2 = var1.getRootView().getHeight();
            int var3 = this.a.getHeight();
            int[] var5 = new int[2];
            var1.getLocationOnScreen(var5);
            Toast var7 = Toast.makeText(var1.getContext(), var6.getTitle(), 0);
            byte var8;
            if (var5[1] + var3 < var2 / 2) {
               var8 = 48;
            } else {
               var8 = 80;
            }

            var7.setGravity(var8 | 3, var5[0], var3);
            var7.show();
            var4 = true;
         }
      } else {
         var4 = false;
      }

      return var4;
   }
}
