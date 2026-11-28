package com.alensw.gallery;

import com.alensw.utils.GalleryUtils;
import java.io.File;

public class ScannedMediaFile {
   public String a;
   public String b;
   public char c;
   public int d;
   public int e;
   public long f;

   public ScannedMediaFile(File var1, char var2, int var3) {
      this.a = var1.getParent();
      this.b = var1.getName();
      this.c = var2;
      this.d = var3;
      this.e = var3;
   }

   public ScannedMediaFile(String var1, String var2, char var3, int var4, int var5, long var6) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      if (var5 != 0) {
         var4 = var5;
      }

      this.e = var4;
      this.f = var6;
   }

   public String a() {
      return GalleryUtils.b(this.a, this.b);
   }

   public void a(ScannedMediaFile var1) {
      this.a = var1.a;
      this.b = var1.b;
      this.c = var1.c;
      this.d = var1.d;
      this.e = var1.e;
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof ScannedMediaFile)) {
            return false;
        }
        ScannedMediaFile srcType0110 = (ScannedMediaFile) var1;
        return this.d == srcType0110.d && (this.a == srcType0110.a || this.a.equals(srcType0110.a)) && (this.b == srcType0110.b || this.b.equals(srcType0110.b));
    }

   @Override
   public int hashCode() {
      return this.a.hashCode() + this.b.hashCode() + this.d;
   }

   @Override
   public String toString() {
      return this.a + '/' + this.b;
   }
}
