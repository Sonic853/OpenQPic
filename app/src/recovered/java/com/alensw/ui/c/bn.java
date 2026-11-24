package com.alensw.ui.c;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import java.util.HashSet;

class bn extends com.alensw.ui.a.ao {
   final HashSet a;
   final int b;
   final com.alensw.ui.c.bf c;

   bn(com.alensw.ui.c.bf var1, Activity var2, String var3, int var4, HashSet var5, int var6) {
      super(var2, var3, var4);
      this.c = var1;
      this.a = var5;
      this.b = var6;
   }

   @Override
   protected void a() {
        int iA;
        for (Object entry : this.a) {
         com.alensw.a.d srcType0110 = (com.alensw.a.d) entry;
            String strA = srcType0110.a();
            if ((srcType0110.c == 'I' && strA.length() > 0) && (iA = com.alensw.b.l.b.a(strA, this.b, false)) != -1) {
                QuickApp.w.c(srcType0110);
                QuickApp.s.a(strA, iA);
                QuickApp.r.a(strA);
            }
            if (!d()) {
                return;
            }
        }
    }

   @Override
   protected void b() {
      this.c.f.requestLayout();
      QuickApp.o.a(this.c.g, true);
      this.c.o();
   }
}
