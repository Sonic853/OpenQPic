package com.alensw.ui.view;

import android.content.Context;
import android.view.View;

class a {
   protected View a;
   protected int b = 0;
   protected int c = 240;
   protected int d = 0;
   protected Runnable e = new com.alensw.ui.view.b(this);
   private boolean f;
   private com.alensw.ui.view.bt g;
   private Runnable h = new com.alensw.ui.view.c(this);

   public a(Context var1, View var2) {
      this.a = var2;
   }

   static boolean a(com.alensw.ui.view.a var0) {
      return var0.f;
   }

   static Runnable b(com.alensw.ui.view.a var0) {
      return var0.h;
   }

   public void a(int var1) {
      switch (var1) {
         case 0:
            this.b(0);
            this.b();
            break;
         case 1:
            this.b(false);
            break;
         case 2:
            this.b(true);
            break;
         case 3:
            this.b(this.c);
            this.b();
      }

      this.b = var1;
   }

   public void a(boolean var1) {
      if (var1 && this.b <= 1) {
         this.a(2);
      } else if (!var1 && this.b >= 2) {
         this.a(1);
      }
   }

   public boolean a() {
      boolean var1;
      if (this.b != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void b() {
   }

   public void b(int var1) {
      if (this.d != var1) {
         this.d = var1;
         this.b();
      }
   }

   public void b(boolean var1) {
      if (this.g != null) {
         this.g.c();
      }

      this.f = var1;
      if (this.g == null) {
         this.g = new com.alensw.ui.view.d(this, this.a, null);
      }

      this.g.a(300L, true);
   }
}
