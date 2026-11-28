package com.alensw.ui.menu;

import android.app.Activity;

class DialogMenuSelection implements MenuSelectionListener {
   final GalleryMenu a;

   DialogMenuSelection(GalleryMenu var1) {
      this.a = var1;
   }

   @Override
   public void a(GalleryMenuItem var1, int var2) {
      if (!var1.c() && this.a.d instanceof Activity) {
         ((Activity)this.a.d).onMenuItemSelected(0, var1);
      }
   }
}
