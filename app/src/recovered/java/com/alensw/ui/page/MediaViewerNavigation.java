package com.alensw.ui.page;

import android.net.Uri;
import android.os.Build.VERSION;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.image.Picture;
import com.alensw.ui.view.PictureNavigationListener;
class MediaViewerNavigation implements PictureNavigationListener {
   final MediaViewerPage a;

   MediaViewerNavigation(MediaViewerPage var1) {
      this.a = var1;
   }

   private void c(float var1, float var2) {
      this.a(MediaViewerPage.a(this.a).a(var1, var2), true);
   }

   @Override
   public Picture a(int i, boolean z) {
        if (MediaViewerPage.b(this.a) == null) {
            return null;
        }
        MediaViewerPage.b(this.a, i > 0);
        CommonFile commonFileA = MediaViewerPage.b(this.a).a(this.a.a(MediaViewerPage.b(this.a).k, MediaViewerPage.g(this.a)));
        Uri uriI = commonFileA != null ? commonFileA.i() : Uri.EMPTY;
        if (MediaViewerPage.h(this.a) != null) {
            if (MediaViewerPage.h(this.a).a(uriI)) {
                return MediaViewerPage.h(this.a).c();
            }
            MediaViewerPage.h(this.a).m();
            MediaViewerPage.a(this.a, (Picture) null);
        }
        if (commonFileA != null) {
            MediaViewerPage.a(this.a, MediaViewerPage.a(this.a, commonFileA, z));
        }
        if (MediaViewerPage.h(this.a) != null) {
            return MediaViewerPage.h(this.a).c();
        }
        return null;
    }

   @Override
   public void a(float var1, float var2) {
      if (this.a.C.Q && VERSION.SDK_INT < 19 && System.currentTimeMillis() < this.a.C.S + 500L) {
         this.b(var1, var2);
      } else if (MediaViewerPage.i(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void a(float var1, boolean var2) {
      boolean var3 = true;
      if (MediaViewerPage.j(this.a) && var2 && var1 > 1.0F && MediaViewerPage.a(this.a).getPictureType() == 1) {
         MediaViewerPage.c(this.a, false);
         if (MediaViewerPage.k(this.a) == null) {
            MediaViewerPage.a(this.a, QuickApp.u.a(MediaViewerPage.d(this.a), 3, this.a.b));
         }
      }

      if (MediaViewerPage.l(this.a)) {
         var1 = MediaViewerPage.a(this.a).getTransScale();
         MediaViewerPage var4 = this.a;
         if (var1 > 0.95F && var1 < 1.05F) {
            var2 = var3;
         } else {
            var2 = false;
         }

         MediaViewerPage.d(var4, var2);
      }
   }

   @Override
   public boolean a(int var1) {
      boolean var3 = true;
      boolean var5;
      if (MediaViewerPage.b(this.a) != null) {
         if (var1 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var1 = this.a.a(MediaViewerPage.b(this.a).k, var5);
         if (var5) {
            if (var1 > MediaViewerPage.b(this.a).k) {
               var5 = var3;
            } else {
               var5 = false;
            }
         } else {
            var5 = var3;
            if (var1 >= MediaViewerPage.b(this.a).k) {
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
      if (!MediaViewerPage.i(this.a)) {
         this.c(var1, var2);
      } else if (this.a.H == 2) {
         this.a.C.v();
      }
   }

   @Override
   public void b(int i) {
        if (MediaViewerPage.b(this.a) != null) {
            MediaViewerPage.b(this.a, i > 0);
            MediaViewerPage.b(this.a).k = this.a.a(MediaViewerPage.b(this.a).k, MediaViewerPage.g(this.a));
            MediaViewerPage.a(this.a, MediaViewerPage.b(this.a).a(MediaViewerPage.b(this.a).k));
            Uri uriI = MediaViewerPage.d(this.a).i();
            Picture srcType0202A = (MediaViewerPage.h(this.a) == null || !MediaViewerPage.h(this.a).a(uriI)) ? QuickApp.u.a(uriI) : MediaViewerPage.h(this.a).c();
            this.a.a(srcType0202A, true);
            if (srcType0202A != null) {
                srcType0202A.m();
            }
            if (MediaViewerPage.h(this.a) != null) {
                MediaViewerPage.h(this.a).m();
                MediaViewerPage.a(this.a, (Picture) null);
            }
            if (this.a.b.hasMessages(3000)) {
                return;
            }
            this.a.b.sendEmptyMessageDelayed(3000, 20L);
        }
    }
}
