package com.alensw.ui.view;

import android.view.View;
import android.view.View.OnClickListener;
import java.io.File;

class BreadcrumbClick implements OnClickListener {
   final PathBar a;

   BreadcrumbClick(PathBar var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      Object var2 = var1.getTag();
      if (var2 != null) {
         this.a.setPath((File)var2);
         if (PathBar.a(this.a) != null) {
            PathBar.a(this.a).onClick(var1);
         }
      }
   }
}
