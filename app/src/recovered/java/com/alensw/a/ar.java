package com.alensw.a;

import android.net.Uri;
import com.alensw.PicFolder.QuickApp;
import java.io.File;

class ar implements com.alensw.b.k.k {
   final Uri a;
   final com.alensw.a.au b;
   final String c;
   final long d;
   final com.alensw.a.ao e;

   ar(com.alensw.a.ao var1, Uri var2, com.alensw.a.au var3, String var4, long var5) {
      this.e = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   @Override
   public void a() {
      com.alensw.a.ao.b(this.e).remove(this.a);
   }

   @Override
   public void b() {
      com.alensw.a.ao.b(this.e).remove(this.a);
   }

   public Void c() {
      com.alensw.b.h.j var3 = this.e.a(this.a);
      if (var3 != null) {
         boolean var1 = var3.j();
         if (var1 && !com.alensw.a.ao.a(this.e, this.a)) {
            this.b.a = true;
            File var4 = com.alensw.a.ao.a(this.e).f();
            if (com.alensw.a.ao.a(var4.getPath(), var3)) {
               String var2;
               if (this.c != null) {
                  var2 = "file://" + this.c;
               } else {
                  var2 = this.a.toString();
               }

               com.alensw.a.ao.a(this.e).a(var2, this.d, var4);
            }

            this.b.a = false;
         }

         if (var1 && this.c == null) {
            com.alensw.a.bc var6 = QuickApp.r;
            com.alensw.b.h.h var5 = var3.a(var6.d, var6.e, var6.b, com.alensw.a.bc.a);
            if (var5 != null) {
               var6.a(this.a.toString(), this.d, var5);
               var5.m();
            }
         }

         var3.m();
      }

      return null;
   }

   @Override
   public Object call() {
      return this.c();
   }
}
