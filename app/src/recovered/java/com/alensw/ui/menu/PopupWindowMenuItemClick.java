package com.alensw.ui.menu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

final class PopupWindowMenuItemClick implements OnItemClickListener {
   final MenuListAdapter a;
   final PopupWindow b;
   final View c;
   final int d;
   final int e;
   final MenuSelectionListener f;

   PopupWindowMenuItemClick(MenuListAdapter var1, PopupWindow var2, View var3, int var4, int var5, MenuSelectionListener var6) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      GalleryMenuItem var6 = (GalleryMenuItem)this.a.getItem(var3);
      if (var6.isEnabled()) {
         if (this.b.isShowing()) {
            this.b.dismiss();
         }

         if (var6.hasSubMenu()) {
            GallerySubMenu var7 = (GallerySubMenu)var6.getSubMenu();
            this.a.a(var7);
            this.a.notifyDataSetChanged();
            GalleryMenu.a(this.b, this.c, this.a.a()[0] + this.d * 2, this.e);
         } else {
            this.f.a(var6, var3);
         }
      }
   }
}
