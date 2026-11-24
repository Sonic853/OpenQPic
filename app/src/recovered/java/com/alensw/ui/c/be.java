package com.alensw.ui.c;

import android.net.Uri;
import android.os.Build.VERSION;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
class be implements com.alensw.ui.view.q {
   final com.alensw.ui.c.as a;

   be(com.alensw.ui.c.as var1) {
      this.a = var1;
   }

   private void c(float var1, float var2) {
      this.a(com.alensw.ui.c.as.a(this.a).a(var1, var2), true);
   }

   @Override
   public com.alensw.b.h.j a(int i, boolean z) {
        if (com.alensw.ui.c.as.b(this.a) == null) {
            return null;
        }
        com.alensw.ui.c.as.b(this.a, i > 0);
        CommonFile commonFileA = com.alensw.ui.c.as.b(this.a).a(this.a.a(com.alensw.ui.c.as.b(this.a).k, com.alensw.ui.c.as.g(this.a)));
        Uri uriI = commonFileA != null ? commonFileA.i() : Uri.EMPTY;
        if (com.alensw.ui.c.as.h(this.a) != null) {
            if (com.alensw.ui.c.as.h(this.a).a(uriI)) {
                return com.alensw.ui.c.as.h(this.a).c();
            }
            com.alensw.ui.c.as.h(this.a).m();
            com.alensw.ui.c.as.a(this.a, (com.alensw.b.h.j) null);
        }
        if (commonFileA != null) {
            com.alensw.ui.c.as.a(this.a, com.alensw.ui.c.as.a(this.a, commonFileA, z));
        }
        if (com.alensw.ui.c.as.h(this.a) != null) {
            return com.alensw.ui.c.as.h(this.a).c();
        }
        return null;
    }

   @Override
   public void a(float var1, float var2) {
      if (this.a.C.Q && VERSION.SDK_INT < 19 && System.currentTimeMillis() < this.a.C.S + 500L) {
         this.b(var1, var2);
      } else if (com.alensw.ui.c.as.i(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void a(float var1, boolean var2) {
      boolean var3 = true;
      if (com.alensw.ui.c.as.j(this.a) && var2 && var1 > 1.0F && com.alensw.ui.c.as.a(this.a).getPictureType() == 1) {
         com.alensw.ui.c.as.c(this.a, false);
         if (com.alensw.ui.c.as.k(this.a) == null) {
            com.alensw.ui.c.as.a(this.a, QuickApp.u.a(com.alensw.ui.c.as.d(this.a), 3, this.a.b));
         }
      }

      if (com.alensw.ui.c.as.l(this.a)) {
         var1 = com.alensw.ui.c.as.a(this.a).getTransScale();
         com.alensw.ui.c.as var4 = this.a;
         if (var1 > 0.95F && var1 < 1.05F) {
            var2 = var3;
         } else {
            var2 = false;
         }

         com.alensw.ui.c.as.d(var4, var2);
      }
   }

   @Override
   public boolean a(int var1) {
      boolean var3 = true;
      boolean var5;
      if (com.alensw.ui.c.as.b(this.a) != null) {
         if (var1 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var1 = this.a.a(com.alensw.ui.c.as.b(this.a).k, var5);
         if (var5) {
            if (var1 > com.alensw.ui.c.as.b(this.a).k) {
               var5 = var3;
            } else {
               var5 = false;
            }
         } else {
            var5 = var3;
            if (var1 >= com.alensw.ui.c.as.b(this.a).k) {
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
      if (!com.alensw.ui.c.as.i(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void b(int i) {
        if (com.alensw.ui.c.as.b(this.a) != null) {
            com.alensw.ui.c.as.b(this.a, i > 0);
            com.alensw.ui.c.as.b(this.a).k = this.a.a(com.alensw.ui.c.as.b(this.a).k, com.alensw.ui.c.as.g(this.a));
            com.alensw.ui.c.as.a(this.a, com.alensw.ui.c.as.b(this.a).a(com.alensw.ui.c.as.b(this.a).k));
            Uri uriI = com.alensw.ui.c.as.d(this.a).i();
            com.alensw.b.h.j srcType0202A = (com.alensw.ui.c.as.h(this.a) == null || !com.alensw.ui.c.as.h(this.a).a(uriI)) ? QuickApp.u.a(uriI) : com.alensw.ui.c.as.h(this.a).c();
            this.a.a(srcType0202A, true);
            if (srcType0202A != null) {
                srcType0202A.m();
            }
            if (com.alensw.ui.c.as.h(this.a) != null) {
                com.alensw.ui.c.as.h(this.a).m();
                com.alensw.ui.c.as.a(this.a, (com.alensw.b.h.j) null);
            }
            if (this.a.b.hasMessages(3000)) {
                return;
            }
            this.a.b.sendEmptyMessageDelayed(3000, 20L);
        }
    }
}
