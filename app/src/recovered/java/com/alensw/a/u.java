package com.alensw.a;

import android.os.Handler;
import android.os.Process;
import com.alensw.PicFolder.QuickApp;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class u extends com.alensw.b.k.g {
   final com.alensw.a.s a;
   private Handler b;
   private com.alensw.a.o c;
   private com.alensw.a.q d;
   private HashMap e;
   private String[] f;
   private String[] g;
   private final ArrayList h;

   private u(com.alensw.a.s var1) {
      this.a = var1;
      this.h = new ArrayList(com.alensw.a.s.a.length);
   }

   u(com.alensw.a.s var1, com.alensw.a.t var2) {
      this(var1);
   }

   static com.alensw.a.o a(com.alensw.a.u var0) {
      return var0.c;
   }

   static HashMap a(com.alensw.a.u var0, HashMap var1) {
      var0.e = var1;
      return var1;
   }

   static String[] a(com.alensw.a.u var0, String[] var1) {
      var0.f = var1;
      return var1;
   }

   static ArrayList b(com.alensw.a.u var0) {
      return var0.h;
   }

   static String[] b(com.alensw.a.u var0, String[] var1) {
      var0.g = var1;
      return var1;
   }

   static HashMap c(com.alensw.a.u var0) {
      return var0.e;
   }

   static String[] d(com.alensw.a.u var0) {
      return var0.f;
   }

   static String[] e(com.alensw.a.u var0) {
      return var0.g;
   }

   static com.alensw.a.q f(com.alensw.a.u var0) {
      return var0.d;
   }

   @Override
   protected com.alensw.b.k.e a(int var1, int var2, int var3, Object... var4) {
      com.alensw.a.v var5 = new com.alensw.a.v(this, null);
      var5.a = var1;
      var5.b = var4[0];
      var5.c = (Handler)var4[1];
      var5.d = this.a.b;
      if (var5.a != 4) {
         com.alensw.a.v.a(var5, new com.alensw.a.al());
         com.alensw.a.v.a(var5).a(com.alensw.a.s.f(this.a));
      }

      return var5;
   }

   @Override
   public void a() {
      Process.setThreadPriority(3);
      this.c = com.alensw.a.o.a(com.alensw.a.s.e(this.a));
      this.d = com.alensw.a.q.a(com.alensw.a.s.e(this.a));
      com.alensw.a.n.a(com.alensw.a.s.e(this.a));

      for (String var7 : QuickApp.p.a()) {
         for (String var8 : com.alensw.a.s.a) {
            var8 = var7 + File.separatorChar + var8;
            if (new File(var8).exists()) {
               this.h.add(var8);
            }
         }
      }

      com.alensw.a.e var9 = new com.alensw.a.e(com.alensw.a.e.b + "/tencent/MicroMsg", 0);
      if (this.a.a(var9, false) > 0) {
         for (com.alensw.a.e var12 : var9.h) {
            String var11 = var12.getName();
            if (var11.length() > 16 && com.alensw.b.l.b.a(var11)) {
               this.h.add(var12.getPath());
               break;
            }
         }
      }
   }

   @Override
   protected void a(com.alensw.b.k.e var1) {
      com.alensw.a.v var2 = (com.alensw.a.v)var1;
      var2.e = true;
      if (com.alensw.a.v.a(var2) != null) {
         com.alensw.a.v.a(var2).a(true);
      }
   }

   @Override
   protected int b(com.alensw.b.k.e var1) {
        com.alensw.a.v srcType0128 = (com.alensw.a.v) var1;
        this.b = srcType0128.c;
        switch (srcType0128.a) {
            case 1:
                this.b.obtainMessage(10, srcType0128.a, 0).sendToTarget();
                com.alensw.a.v.b(srcType0128);
                boolean z = !srcType0128.e;
                if (!z) {
                    this.b.removeMessages(12);
                }
                this.b.obtainMessage(11, srcType0128.a, z ? 1 : 0).sendToTarget();
                break;
            case 2:
            case 3:
                boolean z2 = srcType0128.a == 3;
                if (z2 && com.alensw.a.s.g(this.a).b()) {
                    com.alensw.a.s.g(this.a).d(com.alensw.a.s.h(this.a) + this.a.c);
                }
                this.b.obtainMessage(10, srcType0128.a, 0).sendToTarget();
                com.alensw.a.v.c(srcType0128);
                boolean z3 = !srcType0128.e;
                if (!z3) {
                    this.b.removeMessages(12);
                }
                this.b.obtainMessage(11, srcType0128.a, z3 ? 1 : 0).sendToTarget();
                if (z2 && z3 && com.alensw.a.s.g(this.a).e()) {
                    com.alensw.a.s.g(this.a).h();
                }
                break;
            case 4:
                String str = (String) srcType0128.b;
                if (str != null) {
                    com.alensw.a.s.a(this.a).c(str);
                    com.alensw.a.s.b(this.a).c(str);
                    com.alensw.a.s.g(this.a).c(str);
                } else {
                    com.alensw.a.s.a(this.a).d();
                    com.alensw.a.s.b(this.a).d();
                    com.alensw.a.s.g(this.a).d();
                    com.alensw.b.l.b.c(com.alensw.a.s.c(this.a));
                    com.alensw.b.l.b.c(com.alensw.a.s.d(this.a));
                    for (int i = 1; i <= 3; i++) {
                        com.alensw.b.l.b.i(com.alensw.a.s.h(this.a) + i);
                    }
                }
                com.alensw.a.s.a(this.a).h();
                com.alensw.a.s.b(this.a).h();
                com.alensw.a.s.g(this.a).h();
                break;
        }
        this.b = null;
        return 0;
    }
}
