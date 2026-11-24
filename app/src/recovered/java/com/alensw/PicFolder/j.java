package com.alensw.PicFolder;

import android.view.Menu;
import android.view.View;
class j implements com.alensw.b.g.k {
   final CropActivity a;

   j(CropActivity var1) {
      this.a = var1;
   }

   @Override
   public boolean a(Menu var1, View var2) {
      this.a.a(var1);
      boolean var3;
      if (var1.size() > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }
}
