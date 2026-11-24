package com.alensw.b.h;

class f extends com.alensw.b.k.e implements Runnable {
   public int a;
   public int b;
   public int c;
   final com.alensw.b.h.d d;

   public f(com.alensw.b.h.d var1, int var2, int var3) {
      this.d = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public boolean a(int var1, int var2, int var3, Object... var4) {
      boolean var5;
      if (this.a == var1 && this.b == var2) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   @Override
   public boolean equals(Object var1) {
        com.alensw.b.h.f srcType0198 = (com.alensw.b.h.f) var1;
        return this.a == srcType0198.a && this.b == srcType0198.b;
    }

   @Override
   public int hashCode() {
      return this.b + this.c;
   }

   @Override
   public void run() {
      if (com.alensw.b.h.d.a(this.d, this.c, this.a, this.b)) {
         com.alensw.b.h.d.a(this.d, this.c);
      }

      if (com.alensw.b.h.d.a(this.d) != null) {
         com.alensw.b.h.d.a(this.d).invalidate();
         if (com.alensw.b.h.d.b(this.d) != null) {
            com.alensw.b.h.d.b(this.d).a(this);
         }
      }
   }
}
