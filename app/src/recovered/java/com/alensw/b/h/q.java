package com.alensw.b.h;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

class q extends com.alensw.b.h.h {
   protected final boolean a;
   protected final int c;
   final com.alensw.b.h.n d;

   public q(com.alensw.b.h.n var1, int var2, int var3, int var4) {
      super(var3, var4, 0, 0);
      this.d = var1;
      this.a = false;
      this.c = var2;
   }

   public q(com.alensw.b.h.n var1, int var2, Bitmap var3) {
      super(var3);
      this.d = var1;
      boolean var4;
      if (var3.hasAlpha() && var3.getConfig() == Config.ARGB_8888) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a = var4;
      this.c = var2;
   }

   @Override
   protected void a(Bitmap var1) {
      com.alensw.b.h.n.b(var1);
   }

   @Override
   public String toString() {
      return super.toString() + ", tile=" + com.alensw.b.h.m.e(this.c);
   }
}
