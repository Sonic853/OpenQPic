package com.alensw.ui.page;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryFolders;
import com.alensw.gallery.ScannedFolder;
import com.alensw.image.PictureBitmap;
import com.alensw.ui.view.ImageGridAdapter;
import com.alensw.ui.view.ImageGridCell;
import java.io.File;

class AlbumGridAdapter implements ImageGridAdapter {
   final AlbumsPage a;
   private final int b;
   private int c;

   AlbumGridAdapter(AlbumsPage var1) {
      this.a = var1;
      this.b = ScannedFolder.b.length();
   }

   @Override
   public int a() {
      return this.a.l.f();
   }

   @Override
   public String a(int var1) {
      ScannedFolder var2 = this.a.l.b(var1);
      String var3;
      if (this.a.t != 3 && this.a.t != 4) {
         var3 = this.a.b(var2.getName());
      } else {
         var3 = AlbumsPage.c(var2.e);
      }

      return var3;
   }

   public String a(String var1) {
      String var3 = var1;
      if (var1 != null) {
         int var2 = var1.lastIndexOf(File.separatorChar);
         var3 = var1;
         if (var2 > 0) {
            if (var1.startsWith(ScannedFolder.b) && var2 > this.b) {
               var3 = var1.substring(this.b, var2);
            } else if (var1.startsWith("/storage/") && var2 > 9) {
               var3 = var1.substring(9, var2);
            } else if (var1.startsWith("/mnt/") && var2 > 5) {
               var3 = var1.substring(5, var2);
            } else {
               var3 = var1.substring(0, var2);
            }
         }
      }

      return var3;
   }

   @Override
   public void a(int var1, int var2, int var3) {
      boolean var4 = true;
      if (this.a.H == 2 && !this.a.p) {
         if (var3 != 0) {
            if (var3 < 0) {
               var4 = false;
            }
         } else if (this.c < 0) {
            var4 = false;
         }

         if (this.c != var3) {
            this.c = var3;
            QuickApp.t.a(false);
         }

         for (int var7 = 0; var7 < var2 - var1; var7++) {
            GalleryFolders var6 = this.a.l;
            int var5;
            if (var4) {
               var5 = var1 + var7;
            } else {
               var5 = var2 - 1 - var7;
            }

            ScannedFolder var8 = var6.b(var5);
            if (var8 != null) {
               this.a.d(var8);
            }
         }
      }
   }

   @Override
   public void a(int var1, int var2, boolean var3) {
      while (var1 < var2) {
         ScannedFolder var4 = this.a.l.b(var1);
         if (var4 != null) {
            if (var3) {
               this.a.A.add(var4);
            } else {
               this.a.A.remove(var4);
            }
         }

         var1++;
      }

      this.a.j.b();
      this.a.C.a(Integer.toString(this.a.A.size()) + "/" + this.a.l.f(), false);
   }

   @Override
   public void a(int var1, Canvas var2, RectF var3) {
   }

   @Override
   public void a(int var1, ImageGridCell var2) {
      ScannedFolder var5 = this.a.l.b(var1);
      var1 = var5.i();
      int var3 = var5.j();
      if (var3 > 0) {
         var2.a = (PictureBitmap)QuickApp.v.b(var5);
      } else {
         var2.a = this.a.m();
      }

      if (var2.a != null) {
         var2.a.l();
      }

      if (var1 == 0 && var5.g == 2) {
         var2.c = this.a.g(com.alensw.PicFolder.R.string.camera);
      } else {
         var2.c = var5.getName();
      }

      if (this.a.s == 2) {
         String var6 = AlbumsPage.c(var5.e);
         String[] var7 = var2.d;
         String var4;
         if (var3 > 0) {
            var4 = Integer.toString(var3);
         } else {
            var4 = "";
         }

         var7[0] = var4;
         if (this.a.o) {
            var2.d[1] = var6;
         } else {
            var2.d[1] = this.a(var5.getPath());
            var2.d[2] = var6;
         }
      } else if (var3 > 0) {
         var2.d[0] = Integer.toString(var3);
      }

      if (this.a.j.d && this.a.A.contains(var5)) {
         var2.b = 2;
      }

      if (QuickApp.q.b && var5.a(false)) {
         var2.b |= 1;
      }
   }

   @Override
   public void b(int var1) {
      if (this.a.H == 2) {
         this.a.j.d(this.a.v);
         this.a.v = var1;
         this.a.j.d(var1);
         ScannedFolder var2 = this.a.l.b(var1);
         if (this.a.u != 3 || this.a.o && var2.i() != 0) {
            if (this.a.j.d) {
               if (this.a.A.contains(var2)) {
                  this.a.A.remove(var2);
               } else {
                  this.a.A.add(var2);
               }

               this.a.j.d(var1);
               if (this.a.C.q()) {
                  if (this.a.A.isEmpty()) {
                     this.a.C.d(false);
                  } else {
                     this.a.C.a(Integer.toString(this.a.A.size()) + "/" + this.a.l.f(), false);
                  }
               }
            } else if (var2.i() != 0) {
               this.a.b(var2);
            } else {
               this.a.a(var2, var1);
            }
         } else {
            this.a.a((java.io.File) var2);
         }
      }
   }

   @Override
   public void c(int var1) {
      if (this.a.H == 2) {
         if (this.a.u != 0) {
            this.b(var1);
         } else if (!this.a.C.q()) {
            this.a.C.d(true);
            this.a.v = var1;
            this.a(var1, var1 + 1, true);
         }
      }
   }
}
