package com.alensw.ui.c;

import android.app.Activity;
import java.io.File;

class j extends com.alensw.ui.a.ao {
   final com.alensw.a.e a;
   final com.alensw.a.ba b;
   final String c;
   final Runnable e;
   final com.alensw.ui.c.a f;

   j(com.alensw.ui.c.a var1, Activity var2, String var3, int var4, com.alensw.a.e var5, com.alensw.a.ba var6, String var7, Runnable var8) {
      super(var2, var3, var4);
      this.f = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
   }

   private void a(Object var1) {
      this.f.b.post(new com.alensw.ui.c.k(this, var1));
   }

   @Override
   protected void a() {
      byte var1 = 1;

      try {
         File var2 = new File(this.a, "test");
         var2 = com.alensw.b.l.b.a(var2, "tmp");
         if (!com.alensw.b.l.b.b(var2)) {
            RuntimeException var6 = new RuntimeException("test write failed!");
            throw var6;
         }

         com.alensw.b.l.b.c(var2);
      } catch (Throwable var3) {
         var3.printStackTrace();
         return;
      }

      if (!this.b.d(this.c)) {
         this.a("Hide from the media store failed!\nPlease try again later.");
      } else if (this.b.c()) {
         this.a(com.alensw.PicFolder.R.string.can_not_operate_now);
      } else {
         com.alensw.a.e var5 = this.a;
         if (!com.alensw.b.l.b.a(this.a, true)) {
            var1 = 0;
         }

         var5.f = var1;
      }
   }

   @Override
   protected void b() {
      if (this.a.a(true)) {
         if (this.e != null) {
            this.e.run();
         }
      } else {
         this.f.a(this.a.getPath());
      }
   }
}
