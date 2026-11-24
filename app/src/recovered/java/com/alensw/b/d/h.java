package com.alensw.b.d;

class h implements Runnable {
   final long a;
   final float b;
   final float c;
   final com.alensw.b.d.g d;
   final com.alensw.b.d.f e;

   public h(com.alensw.b.d.f var1, float var2, float var3, long var4, com.alensw.b.d.g var6) {
      this.e = var1;
      this.a = var4;
      this.b = var2;
      this.c = var3;
      this.d = var6;
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof com.alensw.b.d.h)) {
            return false;
        }
        com.alensw.b.d.h srcType0160 = (com.alensw.b.d.h) var1;
        return this.a == srcType0160.a && this.d == srcType0160.d;
    }

   @Override
   public int hashCode() {
      return (int)(this.a ^ this.a >>> 32);
   }

   @Override
   public void run() {
      com.alensw.b.d.c var3 = new com.alensw.b.d.c();
      boolean var1 = com.alensw.b.d.f.b(this.b, this.c);
      if (com.alensw.b.d.f.c(this.e).a(this.b, this.c, var3)) {
         if (var1) {
            var3.b = com.alensw.b.d.f.a(var3.b);
            var3.c = com.alensw.b.d.f.a(var3.c);
            var3.d = com.alensw.b.d.f.a(var3.d);
         }

         String var2 = var3.a();
         com.alensw.b.d.f.d(this.e).put(this.a, var2);
         com.alensw.b.d.f.e(this.e).a(this.a, var2);
         com.alensw.b.d.f.f(this.e);
      }

      if (this.d != null) {
         com.alensw.b.d.f.g(this.e).post(new com.alensw.b.d.i(this, var3));
      }

      com.alensw.b.d.f.h(this.e).remove(this.a);
      if (com.alensw.b.d.f.i(this.e) > 0 && com.alensw.b.d.f.h(this.e).isEmpty()) {
         com.alensw.b.d.f.j(this.e);
      }
   }
}
