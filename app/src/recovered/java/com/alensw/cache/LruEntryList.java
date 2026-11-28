package com.alensw.cache;

import android.util.Log;

class LruEntryList {
   final ConcurrentLruCache a;
   private final LruEntry b;

   LruEntryList(ConcurrentLruCache owner, LruListConstructorMarker unused) {
      this(owner);
   }

   private LruEntryList(ConcurrentLruCache var1) {
      this.a = var1;
      this.b = new LruListSentinel(this);
   }

   public void a() {
      LruEntry var3 = this.b;
      LruEntry var1 = var3.g;

      while (var1 != var3) {
         if (var1 == null) {
            Log.v("ConcurrentLruHashMap", "entry is null!");
            break;
         }

         LruEntry var2 = var1.g;
         var1.f = null;
         var1.g = null;
         var1.e = null;
         this.a.a(var1);
         var1 = var2;
      }

      var3.f = var3;
      var3.g = var3;
   }

   public void a(LruEntry var1) {
      LruEntry var2 = this.b;
      LruEntry var3 = var2.f;
      var1.g = var2;
      var1.f = var3;
      var2.f = var1;
      var3.g = var1;
   }

   public LruEntry b() {
      LruEntry var1 = this.b.g;
      if (var1 == this.b) {
         var1 = null;
      }

      return var1;
   }

   public boolean b(LruEntry var1) {
      boolean var2;
      if (var1.f != null && var1.g != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         var1.f.g = var1.g;
         var1.g.f = var1.f;
      }

      var1.f = null;
      var1.g = null;
      return var2;
   }
}
