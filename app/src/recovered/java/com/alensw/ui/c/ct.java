package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
import java.util.concurrent.atomic.AtomicReference;

class ct implements com.alensw.a.aj, Runnable {
   final com.alensw.ui.c.bz a;
   private final AtomicReference b;
   private final com.alensw.a.p c;
   private final com.alensw.a.e d;
   private final int e;
   private int f;

   public ct(com.alensw.ui.c.bz var1, com.alensw.a.e var2, com.alensw.a.p var3, int var4, int var5) {
      this.a = var1;
      this.b = new AtomicReference();
      this.c = var3;
      this.d = var2;
      this.e = var4;
      this.f = var5;
   }

   @Override
   public void a(Object var1, com.alensw.b.h.h var2) {
      this.b.set(var2.l());
      this.a.j.a(this);
   }

   @Override
   public void run() {
      com.alensw.b.h.h var2 = (com.alensw.b.h.h)this.b.getAndSet(null);
      if (this.a.H == 2 && this.c.c()) {
         this.c.a(this.f, var2);
         this.a.j.d(this.a.l.b(this.d));
         if (this.f + 1 < Math.min(this.e, this.d.j())) {
            com.alensw.a.ah var4 = QuickApp.t;
            com.alensw.a.e var3 = this.d;
            int var1 = this.f + 1;
            this.f = var1;
            var4.a(var3.e(var1), false, this);
         }
      }

      if (var2 != null) {
         var2.m();
      }
   }
}
