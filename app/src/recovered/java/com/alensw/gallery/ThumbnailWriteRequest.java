package com.alensw.gallery;

import com.alensw.cache.LruEntry;

class ThumbnailWriteRequest extends LruEntry {
   public int a;
   public long b;
   public byte[] c;
   public String d;

   public ThumbnailWriteRequest(String var1, int var2, long var3, byte[] var5) {
      this.a = var2;
      this.b = var3;
      this.c = var5;
      this.d = var1;
   }
}
