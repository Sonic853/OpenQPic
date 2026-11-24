package com.alensw.b.g;

import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

final class g implements OnItemClickListener {
   final com.alensw.b.g.a a;
   final PopupWindow b;
   final View c;
   final int d;
   final int e;
   final com.alensw.b.g.j f;

   g(com.alensw.b.g.a var1, PopupWindow var2, View var3, int var4, int var5, com.alensw.b.g.j var6) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      com.alensw.b.g.l var6 = (com.alensw.b.g.l)this.a.getItem(var3);
      if (var6.isEnabled()) {
         if (this.b.isShowing()) {
            this.b.dismiss();
         }

         if (var6.hasSubMenu()) {
            com.alensw.b.g.m var7 = (com.alensw.b.g.m)var6.getSubMenu();
            this.a.a(var7);
            this.a.notifyDataSetChanged();
            com.alensw.b.g.b.a(this.b, this.c, this.a.a()[0] + this.d * 2, this.e);
         } else {
            this.f.a(var6, var3);
         }
      }
   }
}
