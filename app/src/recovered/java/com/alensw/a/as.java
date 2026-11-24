package com.alensw.a;

import android.net.Uri;
public class as extends com.alensw.b.k.d {
   final com.alensw.a.ao a;
   private com.alensw.a.at b;

   public as(com.alensw.a.ao var1, com.alensw.a.at var2) {
      super(var2);
      this.a = var1;
      this.b = var2;
   }

   public com.alensw.b.h.j a() {
      com.alensw.b.h.j var1;
      if (this.isDone()) {
         var1 = this.a.a(this.b.a.i());
      } else {
         var1 = null;
      }

      if (var1 == null) {
         var1 = (com.alensw.b.h.j)com.alensw.a.at.a(this.b).get();
         if (var1 != null) {
            var1 = var1.c();
         } else {
            var1 = null;
         }
      }

      return var1;
   }

   public final boolean a(Uri var1) {
      Uri var3 = this.b.a.i();
      boolean var2;
      if (var1 != var3 && !var1.equals(var3)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public final boolean b(Uri var1) {
      boolean var2;
      if (!this.isCancelled() && this.a(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   @Override
   public String toString() {
      return this.b.toString();
   }
}
