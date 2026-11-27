package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
class cd implements com.alensw.a.aj {
   final com.alensw.a.e a;
   final com.alensw.ui.c.bz b;

   cd(com.alensw.ui.c.bz var1, com.alensw.a.e var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(Object var1, com.alensw.b.h.h var2) {
       com.alensw.b.h.h cover = (com.alensw.b.h.h) var2.l();
       this.b.b.post(() -> {
          com.alensw.a.e current = this.b.l.b(this.b.l.a(this.a));
          if (this.b.H == 2 && current != null && current.a(this.a, 1)) {
             QuickApp.v.a(current, (com.alensw.b.a.c) cover);
             this.b.j.h();
          } else {
             cover.m();
             if (this.b.H == 2 && current != null) this.b.d(current);
          }
       });
   }
}
