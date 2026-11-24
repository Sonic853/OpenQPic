package com.alensw.b.g;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class e implements OnClickListener {
   final com.alensw.b.g.a a;
   final Context b;
   final com.alensw.b.g.j c;

   e(com.alensw.b.g.a var1, Context var2, com.alensw.b.g.j var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      com.alensw.b.g.l var4 = (com.alensw.b.g.l)this.a.getItem(var2);
      if (var4.isEnabled()) {
         if (var4.hasSubMenu()) {
            com.alensw.b.g.m var3 = (com.alensw.b.g.m)var4.getSubMenu();
            this.a.a(var3);
            com.alensw.b.g.b.a(this.b, this.a, var4.getTitle(), this.c);
         } else {
            this.c.a(var4, var2);
         }
      }
   }
}
