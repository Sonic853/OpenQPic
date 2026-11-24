package com.alensw.ui.c;

import android.widget.TextView;
class dh implements com.alensw.b.d.g {
   final TextView a;
   final com.alensw.ui.c.df b;

   dh(com.alensw.ui.c.df var1, TextView var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(com.alensw.b.d.c var1) {
      if (var1 != null) {
         try {
            this.a.setText(var1.a(com.alensw.ui.c.df.a(this.b)));
         } catch (Throwable var2) {
         }
      }
   }
}
