package com.alensw.ui.page;

import android.content.Intent;
import com.alensw.ui.dialog.TextInputCallback;
class SubmitGallerySearch implements TextInputCallback {
   final String a;
   final MediaActionPage b;

   SubmitGallerySearch(MediaActionPage var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(String var1) {
      if (var1.length() > 0) {
         Intent var2 = GallerySearchPage.a(this.b.C, var1, this.a);
         this.b.C.a(GallerySearchPage.class, var2, null);
         MediaActionPage.e(var1);
      }
   }
}
