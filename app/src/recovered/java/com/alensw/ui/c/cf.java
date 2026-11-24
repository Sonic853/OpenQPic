package com.alensw.ui.c;

import android.app.Activity;
import java.io.File;
import java.util.ArrayList;

class cf extends com.alensw.ui.a.ao {
   final ArrayList a;
   final com.alensw.a.ba b;
   final com.alensw.a.bc c;
   final com.alensw.ui.c.bz e;

   cf(com.alensw.ui.c.bz var1, Activity var2, String var3, int var4, ArrayList var5, com.alensw.a.ba var6, com.alensw.a.bc var7) {
      super(var2, var3, var4);
      this.e = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
   }

   @Override
   protected void a() {
        java.util.Iterator iterator = this.a.iterator();
        while (iterator.hasNext()) {
            com.alensw.a.e srcType0111 = (com.alensw.a.e)iterator.next();
            this.b.a(srcType0111.getPath());
            for (int i = srcType0111.j() - 1; i >= 0; --i) {
                com.alensw.a.d srcType0110 = srcType0111.e(i);
                if (srcType0110 == null) continue;
                File file = new File(srcType0111, srcType0110.b);
                String string2 = file.getPath();
                this.b.a(string2, srcType0110.c, false);
                com.alensw.b.l.b.c(file);
                srcType0111.h(i);
                this.c.a(string2);
                if (!this.d()) break;
            }
            srcType0111.o();
            com.alensw.a.e.a(srcType0111);
            if (!srcType0111.h()) continue;
            srcType0111.delete();
        }
        return;
    }

   @Override
   protected void b() {
      for (Object entry : this.a) {
         com.alensw.a.e var2 = (com.alensw.a.e) entry;
         if (var2.h()) {
            this.e.l.f(var2);
         } else {
            this.e.l.a(var2, true);
         }
      }

      if (this.e.C.q()) {
         this.e.C.d(false);
      }

      this.e.j.requestLayout();
   }
}
