package com.alensw.ui.c;

import com.alensw.PicFolder.QuickApp;
class g implements com.alensw.ui.a.s {
   final String a;
   final com.alensw.a.e b;
   final int c;
   final com.alensw.ui.c.o d;
   final com.alensw.ui.c.a e;

   g(com.alensw.ui.c.a var1, String var2, com.alensw.a.e var3, int var4, com.alensw.ui.c.o var5) {
      this.e = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   @Override
   public void a(String var1) {
      boolean var4 = false;
      String var5 = com.alensw.b.l.b.g(com.alensw.b.l.b.a(var1.trim(), '.'));
      if (var5.length() != 0 && !this.a.equals(var5)) {
         var1 = this.b.b(this.c);
         char var2 = this.b.f(this.c);
         int var3 = this.b.a(this.c, var5);
         if (var3 != -1) {
            String var7 = this.b.b(var3);
            QuickApp.s.a(var1, var2, false);
            QuickApp.s.f(var7);
            com.alensw.a.x var9 = QuickApp.o;
            com.alensw.a.e var6 = this.b;
            if (var3 < 4) {
               var4 = true;
            }

            var9.a(var6, var4);
            QuickApp.r.a(var1);
            this.d.a(var1, var7, var3);
         } else {
            this.e.a(var1);
         }
      }
   }
}
