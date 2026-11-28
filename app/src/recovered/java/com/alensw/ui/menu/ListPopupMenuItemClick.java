package com.alensw.ui.menu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.AdapterView.OnItemClickListener;

final class ListPopupMenuItemClick implements OnItemClickListener {
   final MenuListAdapter a;
   final ListPopupWindow b;
   final int[] c;
   final int d;
   final MenuSelectionListener e;

   ListPopupMenuItemClick(MenuListAdapter var1, ListPopupWindow var2, int[] var3, int var4, MenuSelectionListener var5) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      GalleryMenuItem var6 = (GalleryMenuItem)this.a.getItem(var3);
      if (var6.isEnabled()) {
         if (var6.hasSubMenu()) {
            GallerySubMenu var7 = (GallerySubMenu)var6.getSubMenu();
            this.a.a(var7);
            this.a.notifyDataSetChanged();
            int[] var8 = this.a.a();
            this.b.setContentWidth(Math.max(this.c[0], var8[0]));
            if (var8[1] > this.d) {
               this.b.setHeight(this.d);
            }
         } else {
            if (this.b.isShowing()) {
               this.b.dismiss();
            }

            this.e.a(var6, var3);
         }
      }
   }
}
