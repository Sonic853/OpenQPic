package com.alensw.ui.page;

import android.graphics.Rect;
import android.view.View;
import com.alensw.ui.view.WindowInsetsListener;
class PageInsetsListener implements WindowInsetsListener {
   final GalleryPageActivity a;

   PageInsetsListener(GalleryPageActivity var1) {
      this.a = var1;
   }

   @Override
   public void a(Rect var1) {
      if (!this.a.d.equals(var1)) {
         this.a.d.set(var1);

         for (Object entry : GalleryPageActivity.b(this.a)) {
         GalleryPage var6 = (GalleryPage) entry;
            View var5 = var6.D;
            int var3 = var5.getLeft();
            int var2 = var5.getRight();
            var6.a(var3, var5.getTop(), var2, var5.getBottom());
         }
      }
   }
}
