package com.alensw.ui.activity;

import android.view.ViewTreeObserver.OnGlobalLayoutListener;

class bi implements OnGlobalLayoutListener {
   final SelectThemeActivity a;

   bi(SelectThemeActivity var1) {
      this.a = var1;
   }

   @Override
   public void onGlobalLayout() {
      if (SelectThemeActivity.a(this.a).getWidth() > 0) {
         int var2 = SelectThemeActivity.a(this.a).getWidth() / SelectThemeActivity.b(this.a);
         int var1 = var2;
         if ((var2 & 1) != 0) {
            var1 = var2 + 1;
         }

         SelectThemeActivity.a(this.a).setNumColumns(var1);
      }
   }
}
