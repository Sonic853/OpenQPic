package com.alensw.gallery;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.media.MediaFileTypes;
import com.alensw.sort.NameSortOrder;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class GalleryFolders {
   public static final Comparator b = new FolderDateComparator();
   public static final Comparator c = new ReverseFolderDateComparator();
   public static final Comparator d = new FolderNameComparator();
   public static final Comparator e = new ReverseFolderNameComparator();
   public static final Comparator f = new FolderPathComparator();
   public static final Comparator g = new ReverseFolderPathComparator();
   public static final Comparator h = new NonemptyFolderComparator();
   public static final Comparator i = new ReverseNonemptyFolderComparator();
   public static final Comparator j = new AbsolutePathComparator();
   protected Comparator a;
   private int k = a(false, 0, 1);
   private final ArrayList l;
   private final HashSet m;

   public GalleryFolders() {
      this.a = d;
      this.l = new ArrayList(256);
      this.m = new HashSet(256);
   }

   public static final int a(ScannedFolder var0, ScannedFolder var1) {
      String var3 = var0.getPath();
      String var4 = var1.getPath();
      int var2;
      if (var3.length() == var4.length() && var3.equalsIgnoreCase(var4)) {
         var2 = 0;
      } else {
         var2 = NameSortOrder.a.a(var3, var4);
      }

      return var2;
   }

   public static int a(boolean var0, int var1, int var2) {
      int var3;
      if (var0) {
         var3 = 65536;
      } else {
         var3 = 0;
      }

      return var3 | var2 << 8 | var1;
   }

   public static final int b(ScannedFolder var0, ScannedFolder var1) {
      int var2 = a(var0, var1);
      if (var2 != 0) {
         int var3 = var0.e - var1.e;
         if (var3 != 0) {
            var2 = var3;
         }
      }

      return var2;
   }

   public static final int c(ScannedFolder var0, ScannedFolder var1) {
      int var2 = a(var0, var1);
      if (var2 != 0) {
         int var3 = NameSortOrder.a.a(var0.getName(), var1.getName());
         if (var3 != 0) {
            var2 = var3;
         }
      }

      return var2;
   }

   public static final int d(ScannedFolder var0, ScannedFolder var1) {
        int iA = a(var0, var1);
        if (iA == 0) {
            return iA;
        }
        int i2 = var0.j() > 0 ? 0 : 1;
        int i3 = var1.j() <= 0 ? 1 : 0;
        if (i2 != i3) {
            return i2 - i3;
        }
        int iA2 = NameSortOrder.a.a(var0.getName(), var1.getName());
        if (iA2 == 0) {
            iA2 = iA;
        }
        return iA2;
    }

   public static final int e(ScannedFolder var0, ScannedFolder var1) {
      byte var4 = 1;
      int var2 = var0.b();
      int var5 = var1.b();
      byte var6;
      if (var2 != 1 && (var2 != 0 || var0.g != 2)) {
         var6 = 1;
      } else {
         var6 = 0;
      }

      if (var5 != 1) {
         if (var5 != 0) {
            return var6 - var4;
         }

         if (var1.g != 2) {
            return var6 - var4;
         }
      }

      byte var3 = 0;
      return var6 - var3;
   }

   public final int a() {
      return this.k & 0xFF;
   }

   public int a(int var1, String var2) {
        if (var1 < 0 || var1 >= this.l.size()) {
            return -1;
        }
        ScannedFolder srcType0111 = (ScannedFolder) this.l.get(var1);
        File parentFile = srcType0111.getParentFile();
        if (parentFile == null) {
            return -1;
        }
        ScannedFolder srcType0112 = new ScannedFolder(parentFile, var2, 0);
        try {
            if (srcType0112.exists() || !GalleryUtils.b(srcType0111, var2)) {
                return -1;
            }
        } catch (Throwable th) {
            return -1;
        }
        srcType0112.a(srcType0111, true);
        c(var1);
        return d(srcType0112);
    }

   public int a(ScannedFolder var1) {
      int var3 = GalleryUtils.a(this.l, var1, this.a);
      if (var3 < 0 && this.m.contains(var1.getPath().toLowerCase().hashCode())) {
         Log.w("Gallery", "not found: " + var1.getPath());
         int var2 = this.c(var1);
         if (var2 >= 0) {
            return var2;
         }
      }

      return var3;
   }

   public ScannedFolder a(Context var1, Uri var2) {
      String var7 = QuickApp.s.a(var2);
      ScannedFolder var9;
      if (var7 == null) {
         var9 = null;
      } else {
         File var8 = new File(var7);
         boolean var6 = var8.isDirectory();
         ScannedFolder var10;
         if (var6) {
            var10 = new ScannedFolder(var7, -1);
         } else {
            String var11 = var8.getParent();
            if (var11 != null) {
               var10 = new ScannedFolder(var11, -1);
            } else {
               var10 = null;
            }
         }

         if (var10 == null) {
            var9 = null;
         } else {
            var10.d = FolderSortSettings.a(var1).a(var10);
            int var5 = this.a(var10);
            var9 = var10;
            if (var5 >= 0) {
               var9 = this.b(var5);
               if (var6) {
                  var9.c = -1;
               } else if (!var7.equals(var9.b(var9.c))) {
                  char var4 = MediaFileTypes.a(var7);
                  char var3 = var4;
                  if (var4 == 0) {
                     var3 = 'I';
                  }

                  var9.c = var9.a(var8, var3);
                  if (var9.c < 0) {
                     var9.c = var9.c(new ScannedMediaFile(var8, var3, -1));
                  }
               }
            }
         }
      }

      return var9;
   }

   public void a(int var1) {
      this.k = var1;
      boolean var2 = this.b();
      Comparator var4 = this.a;
      switch (this.c()) {
         case 1:
            Comparator var5;
            if (var2) {
               var5 = h;
            } else {
               var5 = d;
            }

            this.a = var5;
            break;
         case 2:
            Comparator var3;
            if (var2) {
               var3 = i;
            } else {
               var3 = e;
            }

            this.a = var3;
            break;
         case 3:
            this.a = b;
            break;
         case 4:
            this.a = c;
            break;
         case 5:
            this.a = f;
            break;
         case 6:
            this.a = g;
      }

      if (this.a != var4) {
         this.i();
      }
   }

   public void a(ScannedFolder var1, boolean var2) {
      var1.n();
      if (this.d()) {
         this.i();
      }

      if (var1.h()) {
         this.f(var1);
      } else if (var2) {
         QuickApp.v.c(var1);
      }
   }

   public boolean a(boolean var1) {
      boolean var3 = false;

      for (int var2 = this.l.size() - 1; var2 >= 0; var2--) {
         if (((ScannedFolder)this.l.get(var2)).a(var1)) {
            this.c(var2);
            var3 = true;
         }
      }

      return var3;
   }

   public int b(ScannedFolder var1) {
      return GalleryUtils.a(this.l, var1, this.a);
   }

   public final ScannedFolder b(int var1) {
      ScannedFolder var2;
      if (var1 >= 0 && var1 < this.l.size()) {
         var2 = (ScannedFolder)this.l.get(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public final boolean b() {
      boolean var1;
      if ((this.k >> 16 & 0xFF) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final int c() {
      return this.k >> 8 & 0xFF;
   }

   public int c(ScannedFolder var1) {
      int var3 = this.l.size();
      int var2 = 0;

      while (true) {
         if (var2 >= var3) {
            var2 = -1;
            break;
         }

         if (a((ScannedFolder)this.l.get(var2), var1) == 0) {
            break;
         }

         var2++;
      }

      return var2;
   }

   public ScannedFolder c(int var1) {
      ScannedFolder var2;
      if (var1 >= 0 && var1 < this.l.size()) {
         var2 = (ScannedFolder)this.l.remove(var1);
         this.m.remove(var2.getPath().toLowerCase().hashCode());
         QuickApp.v.c(var2);
      } else {
         var2 = null;
      }

      return var2;
   }

   public int d(ScannedFolder var1) {
      if (var1.j() == 1) {
         var1.o();
      }

      int var2 = this.a(var1);
      if (var2 >= 0) {
         ScannedFolder var4 = (ScannedFolder)this.l.get(var2);
         if (var4.d != var1.d && var1.d != 0) {
            var4.d = var1.d;
            FolderSortSettings.a(QuickApp.a()).b(var4);
         }

         this.l.remove(var2);
         this.m.remove(var4.getPath().toLowerCase().hashCode());
         if (!var1.a(var4, 4)) {
            QuickApp.v.c(var4);
         }

         var2 = this.a(var1);
      }

      int var3 = var2;
      if (var2 < 0) {
         var3 = -var2 - 1;
         this.l.add(var3, var1);
      }

      this.m.add(var1.getPath().toLowerCase().hashCode());
      return var3;
   }

   public final boolean d() {
      int var1 = this.k >> 8 & 0xFF;
      boolean var2;
      if (var1 != 3 && var1 != 4) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public final int e() {
      return this.k;
   }

   public void e(ScannedFolder var1) {
      this.f(var1);
      String var3 = var1.getPath();

      for (int var2 = this.l.size() - 1; var2 >= 0; var2--) {
         if (GalleryUtils.c(var3, ((ScannedFolder)this.l.get(var2)).getPath())) {
            this.c(var2);
         }
      }
   }

   public final int f() {
      return this.l.size();
   }

   public ScannedFolder f(ScannedFolder var1) {
      int var2 = this.a(var1);
      if (var2 >= 0 && var2 < this.l.size()) {
         var1 = this.c(var2);
      } else {
         var1 = null;
      }

      return var1;
   }

   public void g() {
      Iterator var1 = this.l.iterator();

      while (var1.hasNext()) {
         ((ScannedFolder)var1.next()).k();
      }

      this.l.clear();
      this.m.clear();
   }

   public final ArrayList h() {
      return new ArrayList(this.l);
   }

   public void i() {
      GalleryUtils.a(this.l, this.a);
   }
}
