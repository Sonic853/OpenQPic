package com.alensw.b.h;

class g implements com.alensw.b.k.k {
   final com.alensw.b.h.d a;
   private volatile boolean b;
   private final com.alensw.b.k.f c;
   private final Object d;

   g(com.alensw.b.h.d owner, com.alensw.b.h.e unused) {
      this(owner);
   }

   private g(com.alensw.b.h.d var1) {
      this.a = var1;
      this.c = new com.alensw.b.k.f();
      this.d = new Object();
   }

   private com.alensw.b.h.f e() {
        com.alensw.b.h.f srcType0198;
        synchronized (this.c) {
            srcType0198 = (com.alensw.b.h.f) this.c.b();
            if (srcType0198 == null && !this.b) {
                try {
                    this.c.wait(100L);
                } catch (Throwable th) {
                }
            }
        }
        return srcType0198;
    }

   @Override
   public void a() {
        this.b = true;
        synchronized (this.d) {
            this.d.notifyAll();
        }
        c();
    }

   public void a(com.alensw.b.h.f param1) {
        synchronized (this.c) {
            this.c.a(param1);
            this.c.notifyAll();
        }
    }

   @Override
   public void b() {
   }

   public void c() {
        synchronized (this.c) {
            this.c.a();
            this.c.notifyAll();
        }
    }

   @Override
   public Object call() {
      return this.d();
   }

   public Void d() {
        while (!this.b) {
            com.alensw.b.h.f srcType0198E = e();
            if (srcType0198E != null) {
                long jUptimeMillis = android.os.SystemClock.uptimeMillis();
                srcType0198E.c = com.alensw.b.h.d.c(this.a);
                int iB = com.alensw.b.h.d.b(this.a, srcType0198E.c, srcType0198E.a, srcType0198E.b);
                if (com.alensw.b.h.d.d(this.a) >= com.alensw.b.h.d.e(this.a)) {
                    com.alensw.b.h.d.b(this.a, 0);
                }
                if (iB > 0 && !this.b) {
                    com.alensw.b.h.d.f(this.a).post(srcType0198E);
                }
                int iMax = Math.max(iB - ((int) (android.os.SystemClock.uptimeMillis() - jUptimeMillis)), 10);
                if (!this.b) {
                    synchronized (this.d) {
                        try {
                            this.d.wait(iMax);
                        } catch (Throwable th) {
                        }
                    }
                }
            }
        }
        return null;
    }
}
