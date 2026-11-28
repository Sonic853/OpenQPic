package com.alensw.ui.view;

import com.alensw.image.PictureBitmap;

public class ImageGridCell {
   public PictureBitmap a;
   public int b;
   public String c;
   public String[] d;
   final ImageGridView e;
   private int f;
   private int g;

   public ImageGridCell(ImageGridView var1) {
      this.e = var1;
      this.f = -1;
      this.g = 255;
      this.a = null;
      this.b = 0;
      this.c = null;
      this.d = new String[3];
   }

   static int a(ImageGridCell var0) {
      return var0.f;
   }

   static int a(ImageGridCell var0, int var1) {
      var0.f = var1;
      return var1;
   }

   static int b(ImageGridCell var0) {
      return var0.g;
   }

   static int b(ImageGridCell var0, int var1) {
      var0.g = var1;
      return var1;
   }

   public void a() {
      this.f = -1;
      if (this.a != null) {
         this.a.m();
      }

      this.a = null;
      this.c = null;
      this.b = 0;
      String[] var1 = this.d;
      String[] var2 = this.d;
      this.d[2] = null;
      var2[1] = null;
      var1[0] = null;
   }
}
