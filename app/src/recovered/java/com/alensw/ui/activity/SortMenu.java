package com.alensw.ui.activity;

import android.content.Context;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.menu.MenuListAdapter;
final class SortMenu extends GalleryMenu {
   final int f;
   final boolean g;

   SortMenu(Context var1, int var2, boolean var3) {
      super(var1);
      this.f = var2;
      this.g = var3;
   }

   @Override
   public MenuListAdapter b() {
      SortMenuAdapter var1 = new SortMenuAdapter(this, this.d, this.b);
      var1.a(this);
      return var1;
   }
}
