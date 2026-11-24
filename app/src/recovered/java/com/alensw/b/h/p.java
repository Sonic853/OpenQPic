package com.alensw.b.h;

class p implements com.alensw.b.k.k {
   final com.alensw.b.h.n a;
   private volatile boolean b;
   private int c;
   private com.alensw.b.h.a d;
   private final com.alensw.b.i.b e;

   private p(com.alensw.b.h.n var1) {
      this.a = var1;
      this.c = -1;
      this.e = new com.alensw.b.i.b(1024);
   }

   p(com.alensw.b.h.n var1, com.alensw.b.h.o var2) {
      this(var1);
   }

   private int e() {
        int i;
        synchronized (this.e) {
            if (this.e.c() == 0 && !this.b) {
                try {
                    this.e.wait(100L);
                } catch (Throwable th) {
                }
            }
            this.c = this.e.c() > 0 ? this.e.b() : -1;
            i = this.c;
        }
        return i;
    }

   @Override
   public void a() {
      this.b = true;
      this.c();
   }

   public boolean a(int param1) {
        synchronized (this.e) {
            if (this.e.a(param1) || this.c == param1) {
                return false;
            }
            this.e.b(param1);
            this.e.notifyAll();
            return true;
        }
    }

   @Override
   public void b() {
   }

   public void c() {
        synchronized (this.e) {
            this.e.a();
            this.e.notifyAll();
        }
        synchronized (this) {
            if (this.d != null) {
                this.d.requestCancelDecode();
            }
        }
    }

   @Override
   public Object call() {
      return this.d();
   }

   public Void d() {
        boolean z;
        android.view.View viewC;
        android.graphics.Rect rect = new android.graphics.Rect();
        while (!this.b) {
            int iE = e();
            if (iE != -1) {
                com.alensw.b.h.a srcType0193E = com.alensw.b.h.a.e();
                synchronized (this) {
                    this.d = srcType0193E;
                }
                int iA = com.alensw.b.h.m.a(iE);
                int i = com.alensw.b.h.m.b << iA;
                rect.left = com.alensw.b.h.m.c(iE);
                rect.top = com.alensw.b.h.m.d(iE);
                rect.right = rect.left + i;
                rect.bottom = i + rect.top;
                synchronized (com.alensw.b.h.n.a(this.a)) {
                    z = iA == com.alensw.b.h.n.b(this.a) && android.graphics.Rect.intersects(rect, com.alensw.b.h.n.a(this.a));
                }
                if (z) {
                    com.alensw.b.h.n.a(this.a, iE, rect, srcType0193E);
                }
                synchronized (this.e) {
                    this.c = -1;
                    viewC = this.e.c() == 0 ? com.alensw.b.h.n.c(this.a) : null;
                }
                if (viewC != null) {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        viewC.postInvalidateOnAnimation();
                    } else {
                        viewC.postInvalidate();
                    }
                }
                synchronized (this) {
                    this.d = null;
                }
                com.alensw.b.h.a.a(srcType0193E);
            }
        }
        return null;
    }
}
