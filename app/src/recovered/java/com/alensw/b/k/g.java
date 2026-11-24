package com.alensw.b.k;

public class g {
   private long a;
   private com.alensw.b.k.c b;
   private com.alensw.b.k.e c;
   private final com.alensw.b.k.f d = new com.alensw.b.k.f();

   static com.alensw.b.k.e a(com.alensw.b.k.g var0, com.alensw.b.k.e var1) {
      var0.c = var1;
      return var1;
   }

   static com.alensw.b.k.f a(com.alensw.b.k.g var0) {
      return var0.d;
   }

   static com.alensw.b.k.e b(com.alensw.b.k.g var0) {
      return var0.c;
   }

   protected com.alensw.b.k.e a(int var1, int var2, int var3, Object... var4) {
      throw new RuntimeException("override!");
   }

   protected void a() {
   }

   protected void a(com.alensw.b.k.e var1) {
   }

   public void a(boolean param1) {
        synchronized (this.d) {
            this.d.a();
            if (param1 && this.c != null) {
                a(this.c);
            }
        }
    }

   protected int b(com.alensw.b.k.e var1) {
      return 0;
   }

   public void b() {
      if (this.b == null) {
         this.b = new com.alensw.b.k.h(this);
      }

      this.b.start();
      this.a = this.b.getId();
   }

   public boolean b(int param1, int param2, int param3, Object... param4) {
        boolean z = false;
        synchronized (this.d) {
            if (this.c == null || !this.c.a(param1, param2, param3, param4)) {
                if (this.d.a(param1, param2, param3, param4) == null) {
                    this.d.a(a(param1, param2, param3, param4));
                    this.d.notifyAll();
                    z = true;
                }
            }
        }
        return z;
    }

   public void c() {
      this.a(true);
      if (this.b != null) {
         this.b.a(this.d);
         this.b = null;
         this.a = 0L;
      }
   }

   protected void d() {
   }
}
