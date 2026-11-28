package com.alensw.PicFolder;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import java.util.ArrayList;

class SelectCropScale implements OnMenuItemClickListener {
   final ArrayList a;
   final CropActivity b;

   SelectCropScale(CropActivity var1, ArrayList var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      int var2 = var1.getItemId();
      if (var2 >= 0 && var2 < this.a.size()) {
         CropActivity.a(this.b).c = (Float)this.a.get(var2);
      } else {
         CropActivity.a(this.b).c = 1.0F;
      }

      return true;
   }
}
