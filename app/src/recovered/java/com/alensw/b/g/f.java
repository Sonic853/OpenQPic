package com.alensw.b.g;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.AdapterView.OnItemClickListener;

final class f implements OnItemClickListener {
   final com.alensw.b.g.a a;
   final ListPopupWindow b;
   final int[] c;
   final int d;
   final com.alensw.b.g.j e;

   f(com.alensw.b.g.a var1, ListPopupWindow var2, int[] var3, int var4, com.alensw.b.g.j var5) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      com.alensw.b.g.l var6 = (com.alensw.b.g.l)this.a.getItem(var3);
      if (var6.isEnabled()) {
         if (var6.hasSubMenu()) {
            com.alensw.b.g.m var7 = (com.alensw.b.g.m)var6.getSubMenu();
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
