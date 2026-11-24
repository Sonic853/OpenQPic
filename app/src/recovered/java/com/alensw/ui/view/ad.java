package com.alensw.ui.view;

import android.view.View;
import android.view.View.OnLongClickListener;

class ad implements OnLongClickListener {
   final ImageGridView a;

   ad(ImageGridView var1) {
      this.a = var1;
   }

   @Override
   public boolean onLongClick(View var1) {
      int var3 = ImageGridView.a(this.a);
      int var2;
      if (ImageGridView.c(this.a) != null) {
         var2 = this.a.getCount();
      } else {
         var2 = 0;
      }

      if (ImageGridView.c(this.a) != null && this.a.isShown() && var3 >= 0 && var3 < var2) {
         if (this.a.d) {
            if (var3 < ImageGridView.d(this.a)) {
               ImageGridView.a(this.a, var3);
            }

            if (var3 > ImageGridView.e(this.a)) {
               ImageGridView.b(this.a, var3);
            }

            if (ImageGridView.d(this.a) <= ImageGridView.e(this.a)) {
               var3 = Math.max(ImageGridView.d(this.a), 0);
               var2 = Math.min(ImageGridView.e(this.a) + 1, var2);
               ImageGridView.c(this.a).a(var3, var2, true);
            }

            if (ImageGridView.d(this.a) < ImageGridView.e(this.a)) {
               ImageGridView.a(this.a, Integer.MAX_VALUE);
               ImageGridView.b(this.a, Integer.MIN_VALUE);
            }
         } else {
            ImageGridView.a(this.a, ImageGridView.b(this.a, var3));
            ImageGridView.c(this.a).c(var3);
         }
      }

      return true;
   }
}
