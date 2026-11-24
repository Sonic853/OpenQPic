package com.alensw.b.a;

import android.util.Log;

class d {
   final com.alensw.b.a.a a;
   private final com.alensw.b.a.c b;

   d(com.alensw.b.a.a owner, com.alensw.b.a.b unused) {
      this(owner);
   }

   private d(com.alensw.b.a.a var1) {
      this.a = var1;
      this.b = new com.alensw.b.a.e(this);
   }

   public void a() {
      com.alensw.b.a.c var3 = this.b;
      com.alensw.b.a.c var1 = var3.g;

      while (var1 != var3) {
         if (var1 == null) {
            Log.v("ConcurrentLruHashMap", "entry is null!");
            break;
         }

         com.alensw.b.a.c var2 = var1.g;
         var1.f = null;
         var1.g = null;
         var1.e = null;
         this.a.a(var1);
         var1 = var2;
      }

      var3.f = var3;
      var3.g = var3;
   }

   public void a(com.alensw.b.a.c var1) {
      com.alensw.b.a.c var2 = this.b;
      com.alensw.b.a.c var3 = var2.f;
      var1.g = var2;
      var1.f = var3;
      var2.f = var1;
      var3.g = var1;
   }

   public com.alensw.b.a.c b() {
      com.alensw.b.a.c var1 = this.b.g;
      if (var1 == this.b) {
         var1 = null;
      }

      return var1;
   }

   public boolean b(com.alensw.b.a.c var1) {
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
