package com.alensw.ui.page;

import android.view.View;
import android.view.View.OnFocusChangeListener;

class LocalViewerFocusListener implements OnFocusChangeListener {
   final LocalViewerPage a;

   LocalViewerFocusListener(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void onFocusChange(View var1, boolean var2) {
      this.a.C.u();
      if (this.a.H == 2) {
         GalleryPageActivity var3 = this.a.C;
         if (!var2) {
            var2 = true;
         } else {
            var2 = false;
         }

         var3.a(var2);
      }
   }
}
