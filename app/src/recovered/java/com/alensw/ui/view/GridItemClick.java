package com.alensw.ui.view;

import android.view.View;
import android.view.View.OnClickListener;

class GridItemClick implements OnClickListener {
   final ImageGridView a;

   GridItemClick(ImageGridView var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      int var2 = ImageGridView.a(this.a);
      if (var2 != -1) {
         this.a.postDelayed(ImageGridView.b(this.a), this.a.l);
      }

      if (ImageGridView.c(this.a) != null && this.a.isShown() && var2 >= 0 && var2 < ImageGridView.c(this.a).a()) {
         ImageGridView.c(this.a).b(var2);
      }
   }
}
