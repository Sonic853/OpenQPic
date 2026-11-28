package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import com.alensw.bean.MediaStoreRoot;
import com.alensw.concurrent.CancellableCallable;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ScannedMediaFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

class SearchFoldersTask implements CancellableCallable {
   final GallerySearchPage a;
   private volatile boolean b;
   private final String c;
   private final Pattern d;
   private final ArrayList e;
   private final CommonFolder f;

   public SearchFoldersTask(GallerySearchPage var1, String var2) {
      this.a = var1;
      String var3 = var2;
      if (var2 == null) {
         var3 = "";
      }

      this.c = var3.toLowerCase();
      this.d = Pattern.compile(var3, 2);
      if ("moments".equals(GallerySearchPage.a(var1))) {
         this.e = null;
         this.f = new MediaStoreRoot(var1.g(com.alensw.PicFolder.R.string.local_moments), "*/*").l();
      } else {
         this.e = QuickApp.o.h();
         this.f = null;
      }
   }

   private int a(CommonFolder var1) {
      int var2 = 0;
      int var5 = var1.o();
      int var4 = 0;

      int var3;
      while (true) {
         var3 = var2;
         if (var4 >= var5) {
            break;
         }

         CommonFile var6 = var1.a(var4);
         var3 = var2;
         if (var6 != null) {
            if (this.a(var6.a())) {
               GallerySearchPage.a(this.a, var6);
            }

            var3 = var2;
            if (this.a(var6.g())) {
               GallerySearchPage.a(this.a, var6);
               var3 = var2 + 1;
            }
         }

         if (this.b) {
            break;
         }

         var4++;
         var2 = var3;
      }

      return var3;
   }

   private int a(ArrayList arrayList) {
        int i;
        Iterator it = arrayList.iterator();
        int i2 = 0;
        while (true) {
            if (!it.hasNext()) {
                i = i2;
                break;
            }
            ScannedFolder srcType0111 = (ScannedFolder) it.next();
            if (a(srcType0111.getName())) {
                GallerySearchPage.a(this.a, srcType0111);
                i = i2 + 1;
            } else {
                i = i2;
            }
            if (this.b) {
                break;
            }
            i2 = i;
        }
        Iterator it2 = arrayList.iterator();
        do {
            int i3 = i;
            if (!it2.hasNext()) {
                return i3;
            }
            ScannedFolder srcType0112 = (ScannedFolder) it2.next();
            int iJ = srcType0112.j();
            int i4 = 0;
            while (true) {
                if (i4 >= iJ) {
                    i = i3;
                    break;
                }
                ScannedMediaFile srcType0110E = srcType0112.e(i4);
                if (srcType0110E != null && a(srcType0110E.b)) {
                    GallerySearchPage.a(this.a, srcType0110E);
                    i3++;
                }
                if (this.b) {
                    i = i3;
                    break;
                }
                i4++;
            }
        } while (!this.b);
        return i;
    }

   private boolean a(String var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1 != null) {
         if (var1.length() == 0) {
            var2 = var3;
         } else {
            if (!var1.toLowerCase().contains(this.c) && !this.d.matcher(var1).matches()) {
               return var3;
            }

            var2 = true;
         }
      }

      return var2;
   }

   @Override
   public void a() {
      this.b = true;
   }

   @Override
   public void b() {
      this.a.b.post(new FinishGallerySearch(this));
   }

   public Void c() {
      if (this.e != null) {
         this.a(this.e);
      } else if (this.f != null) {
         this.a(this.f);
      }

      return null;
   }

   @Override
   public Object call() {
      return this.c();
   }
}
