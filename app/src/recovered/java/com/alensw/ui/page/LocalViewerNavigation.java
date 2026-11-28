package com.alensw.ui.page;

import android.net.Uri;
import android.os.Build.VERSION;
import com.alensw.PicFolder.QuickApp;
import com.alensw.image.Picture;
import com.alensw.ui.view.PictureNavigationListener;
class LocalViewerNavigation implements PictureNavigationListener {
   final LocalViewerPage a;

   LocalViewerNavigation(LocalViewerPage var1) {
      this.a = var1;
   }

   private void c(float var1, float var2) {
      this.a(LocalViewerPage.e(this.a).a(var1, var2), true);
   }

   @Override
   public Picture a(int i, boolean z) {
        if (LocalViewerPage.a(this.a) == null) {
            return null;
        }
        LocalViewerPage.b(this.a, i > 0);
        int iA = this.a.a(LocalViewerPage.a(this.a).c, LocalViewerPage.j(this.a));
        Uri uriD = LocalViewerPage.a(this.a).d(iA);
        if (LocalViewerPage.k(this.a) != null) {
            if (LocalViewerPage.k(this.a).a(uriD)) {
                return LocalViewerPage.k(this.a).c();
            }
            LocalViewerPage.k(this.a).m();
            LocalViewerPage.a(this.a, (Picture) null);
        }
        LocalViewerPage.a(this.a, LocalViewerPage.a(this.a, uriD, LocalViewerPage.a(this.a).f(iA), z));
        if (LocalViewerPage.k(this.a) != null) {
            return LocalViewerPage.k(this.a).c();
        }
        return null;
    }

   @Override
   public void a(float var1, float var2) {
      if (this.a.C.Q && VERSION.SDK_INT < 19 && System.currentTimeMillis() < this.a.C.S + 500L) {
         this.b(var1, var2);
      } else if (LocalViewerPage.l(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void a(float var1, boolean var2) {
      boolean var3 = true;
      if (LocalViewerPage.m(this.a) && var2 && var1 > 1.0F && LocalViewerPage.e(this.a).getPictureType() == 1) {
         LocalViewerPage.c(this.a, false);
         if (LocalViewerPage.n(this.a) == null) {
            LocalViewerPage.a(this.a, QuickApp.u.a(LocalViewerPage.b(this.a), LocalViewerPage.o(this.a), 3, this.a.b));
         }
      }

      if (LocalViewerPage.p(this.a)) {
         var1 = LocalViewerPage.e(this.a).getTransScale();
         LocalViewerPage var4 = this.a;
         if (var1 > 0.95F && var1 < 1.05F) {
            var2 = var3;
         } else {
            var2 = false;
         }

         LocalViewerPage.d(var4, var2);
      }
   }

   @Override
   public boolean a(int var1) {
      boolean var3 = true;
      boolean var5;
      if (LocalViewerPage.a(this.a) != null) {
         if (var1 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var1 = this.a.a(LocalViewerPage.a(this.a).c, var5);
         if (var5) {
            if (var1 > LocalViewerPage.a(this.a).c) {
               var5 = var3;
            } else {
               var5 = false;
            }
         } else {
            var5 = var3;
            if (var1 >= LocalViewerPage.a(this.a).c) {
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
      if (!LocalViewerPage.l(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void b(int i) {
        if (LocalViewerPage.a(this.a) != null) {
            LocalViewerPage.b(this.a, i > 0);
            LocalViewerPage.a(this.a).c = this.a.a(LocalViewerPage.a(this.a).c, LocalViewerPage.j(this.a));
            LocalViewerPage.a(this.a, LocalViewerPage.a(this.a).d(LocalViewerPage.a(this.a).c));
            LocalViewerPage.a(this.a, LocalViewerPage.a(this.a).f(LocalViewerPage.a(this.a).c));
            Picture srcType0202A = (LocalViewerPage.k(this.a) == null || !LocalViewerPage.k(this.a).a(LocalViewerPage.b(this.a))) ? QuickApp.u.a(LocalViewerPage.b(this.a)) : LocalViewerPage.k(this.a).c();
            this.a.a(srcType0202A, true);
            if (srcType0202A != null) {
                srcType0202A.m();
            }
            if (LocalViewerPage.k(this.a) != null) {
                LocalViewerPage.k(this.a).m();
                LocalViewerPage.a(this.a, (Picture) null);
            }
            if (this.a.b.hasMessages(3000)) {
                return;
            }
            this.a.b.sendEmptyMessageDelayed(3000, 20L);
        }
    }
}
