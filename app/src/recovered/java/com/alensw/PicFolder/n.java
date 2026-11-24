package com.alensw.PicFolder;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import java.util.ArrayList;

class n implements OnMenuItemClickListener {
   final ArrayList a;
   final ArrayList b;
   final CropActivity c;

   n(CropActivity var1, ArrayList var2, ArrayList var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      boolean var7 = false;
      int var6 = var1.getItemId();
      int var3;
      int var4;
      int var5;
      int var11;
      if (var6 >= 1000 && var6 < 2000) {
         var11 = var6 - 1000;
         if (var11 < 0 || var11 >= this.a.size()) {
            com.alensw.ui.a.f.a(this.c, com.alensw.PicFolder.R.string.ratio, " : ", CropActivity.b(this.c), CropActivity.c(this.c), new com.alensw.PicFolder.o(this));
            return true;
         }

         long var13 = (Long)this.a.get(var11);
         var4 = (int)(var13 & 2147483647L);
         var5 = (int)(var13 >> 32 & 2147483647L);
         var3 = 0;
         var11 = 0;
      } else if (var6 >= 2000) {
         var11 = var6 - 2000;
         if (var11 < 0 || var11 >= this.b.size()) {
            com.alensw.ui.a.f.a(this.c, com.alensw.PicFolder.R.string.pic_resolution, " x ", CropActivity.d(this.c), CropActivity.e(this.c), new com.alensw.PicFolder.p(this));
            return true;
         }

         long var8 = (Long)this.b.get(var11);
         var11 = (int)(var8 & 2147483647L);
         var3 = (int)(var8 >> 32 & 2147483647L);
         var5 = 0;
         var4 = 0;
      } else {
         var5 = 0;
         var4 = 0;
         var3 = 0;
         var11 = 0;
      }

      CropActivity var10 = this.c;
      if (var6 != 999) {
         var7 = true;
      }

      var10.a(var7, var11, var3, var4, var5, true);
      return true;
   }
}
