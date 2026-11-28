package com.alensw.ui.page;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.text.format.Formatter;
import com.alensw.PicFolder.CropActivity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.image.PictureBitmap;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.view.ImageGridAdapter;
import com.alensw.ui.view.ImageGridCell;
import com.alensw.utils.GalleryUtils;
import java.io.File;

class LocalFileGridAdapter implements ImageGridAdapter {
   final LocalFolderPage a;
   private int b;

   LocalFileGridAdapter(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public int a() {
      return this.a.g.j();
   }

   @Override
   public String a(int var1) {
      ScannedMediaFile var2 = this.a.g.e(var1);
      String var3;
      if (var2 != null) {
         if (this.a.j == 5 || this.a.j == 6) {
            var3 = QuickApp.a(var2.e * 1000L);
         } else if (this.a.j == 3 || this.a.j == 1) {
            var3 = QuickApp.a(var2.d * 1000L);
         } else if (this.a.j != 7 && this.a.j != 8) {
            var3 = this.a.b(var2.b);
         } else {
            var3 = Formatter.formatFileSize(this.a.C, var2.f);
         }
      } else {
         var3 = null;
      }

      return var3;
   }

   @Override
   public void a(int var1, int var2, int var3) {
      boolean var4 = true;
      if (this.a.H == 2) {
         if (var3 != 0) {
            if (var3 < 0) {
               var4 = false;
            }
         } else if (this.b < 0) {
            var4 = false;
         }

         if (this.b != var3) {
            this.b = var3;
            QuickApp.t.a(false);
         }

         for (int var7 = 0; var7 < var2 - var1; var7++) {
            int var5;
            if (var4) {
               var5 = var1 + var7;
            } else {
               var5 = var2 - 1 - var7;
            }

            ScannedMediaFile var6 = this.a.g.e(var5);
            if (var6 != null && !QuickApp.w.a(var6)) {
               LocalFolderPage.a(this.a, var6);
            }
         }
      }
   }

   @Override
   public void a(int var1, int var2, boolean var3) {
      while (var1 < var2) {
         ScannedMediaFile var4 = this.a.g.e(var1);
         if (var4 != null) {
            if (var3) {
               this.a.k.add(var4);
            } else {
               this.a.k.remove(var4);
            }
         }

         var1++;
      }

      this.a.f.b();
      this.a.m();
   }

   @Override
   public void a(int var1, Canvas var2, RectF var3) {
   }

   @Override
   public void a(int var1, ImageGridCell var2) {
      ScannedMediaFile var3 = this.a.g.e(var1);
      if (var3 != null) {
         var2.a = (PictureBitmap)QuickApp.w.b(var3);
         if (var2.a != null) {
            var2.a.l();
         }

         boolean var4;
         if (this.a.i == 2) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var3.c != 'I' || var4) {
            var2.c = GalleryUtils.c(var3.b);
         }

         if (var4) {
            if (this.a.j == 5 || this.a.j == 6) {
               var2.d[1] = QuickApp.b(var3.e * 1000L);
            } else if (this.a.j == 3 || this.a.j == 1) {
               var2.d[1] = QuickApp.b(var3.d * 1000L);
            } else if (this.a.j == 7 || this.a.j == 8) {
               var2.d[1] = Formatter.formatFileSize(this.a.C, var3.f);
            }
         }

         if (this.a.f.d && this.a.k.contains(var3)) {
            var2.b |= 2;
         }
      }
   }

   @Override
   public void b(int var1) {
      if (this.a.H == 2) {
         File var4 = this.a.g.c(var1);
         char var2 = this.a.g.f(var1);
         if (this.a.f.d) {
            ScannedMediaFile var3 = this.a.g.e(var1);
            if (this.a.k.contains(var3)) {
               this.a.k.remove(var3);
            } else {
               this.a.k.add(var3);
            }

            this.a.f.d(var1);
            if (this.a.C.q() && this.a.k.isEmpty()) {
               this.a.C.d(false);
            } else {
               this.a.m();
            }
         } else if (this.a.h != 0) {
            Intent var5 = this.a.C.getIntent();
            this.a.g.c = var1;
            if (var2 == 'I' && var5.hasExtra("crop")) {
               Intent var8 = new Intent(var5.getAction(), Uri.fromFile(var4));
               var8.setClass(this.a.C, CropActivity.class);
               var8.putExtras(var5);
               GalleryActions.a(this.a.C, var8, 2);
            } else {
               Uri var6 = MediaDetails.a(var4.getPath(), var2);
               this.a.a(var6, this.a.g.g(var1));
            }
         } else if (var2 == 'I') {
            Uri var9 = Uri.fromFile(var4);
            String var7 = this.a.g.g(var1);
            this.a.g.c = var1;
            this.a.a(var9, var7, false);
         } else if (var2 == 'V') {
            GalleryActions.a(this.a.C, var4.getPath());
         }
      }
   }

   @Override
   public void c(int var1) {
      if (this.a.H == 2 && this.a.h == 0 && !this.a.C.q()) {
         this.a.C.d(true);
         this.a(var1, var1 + 1, true);
      }
   }
}
