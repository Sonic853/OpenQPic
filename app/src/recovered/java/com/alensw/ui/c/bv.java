package com.alensw.ui.c;

import java.util.HashSet;

class bv implements com.alensw.ui.c.n {
   final com.alensw.ui.c.bf a;

   bv(com.alensw.ui.c.bf var1) {
      this.a = var1;
   }

   @Override
   public void a(boolean var1) {
      this.a.a(false);
   }

   @Override
   public void a(boolean var1, HashSet var2) {
      if (!var1) {
         this.a.a(var2);
         this.a.l();
      }

      this.a.o();
   }
}
