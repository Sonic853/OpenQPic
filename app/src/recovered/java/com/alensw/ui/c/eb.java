package com.alensw.ui.c;

import android.net.Uri;
import android.os.Build.VERSION;
import com.alensw.PicFolder.QuickApp;
class eb implements com.alensw.ui.view.q {
   final com.alensw.ui.c.dp a;

   eb(com.alensw.ui.c.dp var1) {
      this.a = var1;
   }

   private void c(float var1, float var2) {
      this.a(com.alensw.ui.c.dp.e(this.a).a(var1, var2), true);
   }

   @Override
   public com.alensw.b.h.j a(int i, boolean z) {
        if (com.alensw.ui.c.dp.a(this.a) == null) {
            return null;
        }
        com.alensw.ui.c.dp.b(this.a, i > 0);
        int iA = this.a.a(com.alensw.ui.c.dp.a(this.a).c, com.alensw.ui.c.dp.j(this.a));
        Uri uriD = com.alensw.ui.c.dp.a(this.a).d(iA);
        if (com.alensw.ui.c.dp.k(this.a) != null) {
            if (com.alensw.ui.c.dp.k(this.a).a(uriD)) {
                return com.alensw.ui.c.dp.k(this.a).c();
            }
            com.alensw.ui.c.dp.k(this.a).m();
            com.alensw.ui.c.dp.a(this.a, (com.alensw.b.h.j) null);
        }
        com.alensw.ui.c.dp.a(this.a, com.alensw.ui.c.dp.a(this.a, uriD, com.alensw.ui.c.dp.a(this.a).f(iA), z));
        if (com.alensw.ui.c.dp.k(this.a) != null) {
            return com.alensw.ui.c.dp.k(this.a).c();
        }
        return null;
    }

   @Override
   public void a(float var1, float var2) {
      if (this.a.C.Q && VERSION.SDK_INT < 19 && System.currentTimeMillis() < this.a.C.S + 500L) {
         this.b(var1, var2);
      } else if (com.alensw.ui.c.dp.l(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void a(float var1, boolean var2) {
      boolean var3 = true;
      if (com.alensw.ui.c.dp.m(this.a) && var2 && var1 > 1.0F && com.alensw.ui.c.dp.e(this.a).getPictureType() == 1) {
         com.alensw.ui.c.dp.c(this.a, false);
         if (com.alensw.ui.c.dp.n(this.a) == null) {
            com.alensw.ui.c.dp.a(this.a, QuickApp.u.a(com.alensw.ui.c.dp.b(this.a), com.alensw.ui.c.dp.o(this.a), 3, this.a.b));
         }
      }

      if (com.alensw.ui.c.dp.p(this.a)) {
         var1 = com.alensw.ui.c.dp.e(this.a).getTransScale();
         com.alensw.ui.c.dp var4 = this.a;
         if (var1 > 0.95F && var1 < 1.05F) {
            var2 = var3;
         } else {
            var2 = false;
         }

         com.alensw.ui.c.dp.d(var4, var2);
      }
   }

   @Override
   public boolean a(int var1) {
      boolean var3 = true;
      boolean var5;
      if (com.alensw.ui.c.dp.a(this.a) != null) {
         if (var1 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var1 = this.a.a(com.alensw.ui.c.dp.a(this.a).c, var5);
         if (var5) {
            if (var1 > com.alensw.ui.c.dp.a(this.a).c) {
               var5 = var3;
            } else {
               var5 = false;
            }
         } else {
            var5 = var3;
            if (var1 >= com.alensw.ui.c.dp.a(this.a).c) {
               var5 = false;
            }
         }
      } else {
         var5 = false;
      }

      return var5;
   }

   @Override
   public void b(float var1, float var2) {
      if (!com.alensw.ui.c.dp.l(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void b(int i) {
        if (com.alensw.ui.c.dp.a(this.a) != null) {
            com.alensw.ui.c.dp.b(this.a, i > 0);
            com.alensw.ui.c.dp.a(this.a).c = this.a.a(com.alensw.ui.c.dp.a(this.a).c, com.alensw.ui.c.dp.j(this.a));
            com.alensw.ui.c.dp.a(this.a, com.alensw.ui.c.dp.a(this.a).d(com.alensw.ui.c.dp.a(this.a).c));
            com.alensw.ui.c.dp.a(this.a, com.alensw.ui.c.dp.a(this.a).f(com.alensw.ui.c.dp.a(this.a).c));
            com.alensw.b.h.j srcType0202A = (com.alensw.ui.c.dp.k(this.a) == null || !com.alensw.ui.c.dp.k(this.a).a(com.alensw.ui.c.dp.b(this.a))) ? QuickApp.u.a(com.alensw.ui.c.dp.b(this.a)) : com.alensw.ui.c.dp.k(this.a).c();
            this.a.a(srcType0202A, true);
            if (srcType0202A != null) {
                srcType0202A.m();
            }
            if (com.alensw.ui.c.dp.k(this.a) != null) {
                com.alensw.ui.c.dp.k(this.a).m();
                com.alensw.ui.c.dp.a(this.a, (com.alensw.b.h.j) null);
            }
            if (this.a.b.hasMessages(3000)) {
                return;
            }
            this.a.b.sendEmptyMessageDelayed(3000, 20L);
        }
    }
}
