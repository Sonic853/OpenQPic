package com.alensw.ui.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class DialogMenuItemClick implements OnClickListener {
   final MenuListAdapter a;
   final Context b;
   final MenuSelectionListener c;

   DialogMenuItemClick(MenuListAdapter var1, Context var2, MenuSelectionListener var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      GalleryMenuItem var4 = (GalleryMenuItem)this.a.getItem(var2);
      if (var4.isEnabled()) {
         if (var4.hasSubMenu()) {
            GallerySubMenu var3 = (GallerySubMenu)var4.getSubMenu();
            this.a.a(var3);
            GalleryMenu.a(this.b, this.a, var4.getTitle(), this.c);
         } else {
            this.c.a(var4, var2);
         }
      }
   }
}
