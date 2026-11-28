package com.alensw.image;

import com.alensw.cache.LruEntry;

public class PictureMetadata extends LruEntry {
   public int a;
   public int b;
   public int c;
   public int d;
   public boolean h;
   public boolean i;
   public long j;
   public float[] k;
   public String l;
   public String m;
   public String n;
   public String o;

   public PictureMetadata() {
   }

   public PictureMetadata(PictureMetadata var1) {
      this.a = var1.a;
      this.b = var1.b;
      this.c = var1.c;
      this.d = var1.d;
      this.h = var1.h;
      this.i = var1.i;
      this.j = var1.j;
      this.k = var1.k;
      this.l = var1.l;
      this.m = var1.m;
      this.n = var1.n;
      this.o = var1.o;
   }
}
