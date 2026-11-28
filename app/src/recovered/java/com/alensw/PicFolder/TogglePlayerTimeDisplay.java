package com.alensw.PicFolder;

import android.view.View;
import android.view.View.OnClickListener;

class TogglePlayerTimeDisplay implements OnClickListener {
   final PlayerActivity a;

   TogglePlayerTimeDisplay(PlayerActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      PlayerActivity var3 = this.a;
      boolean var2;
      if (!this.a.s) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.s = var2;
      this.a.b(true);
   }
}
