package com.alensw.PicFolder;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

class i implements OnLongClickListener {
   final CropActivity a;

   i(CropActivity var1) {
      this.a = var1;
   }

   @Override
   public boolean onLongClick(View var1) {
      CharSequence var6 = var1.getContentDescription();
      boolean var4;
      if (var6 != null) {
         int var2 = var1.getRootView().getHeight();
         int var3 = var1.getHeight();
         int[] var5 = new int[2];
         var1.getLocationOnScreen(var5);
         Toast var7 = Toast.makeText(var1.getContext(), var6, 0);
         byte var8;
         if (var5[1] + var3 < var2 / 2) {
            var8 = 48;
         } else {
            var8 = 80;
         }

         var7.setGravity(var8 | 3, var5[0], var3);
         var7.show();
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }
}
