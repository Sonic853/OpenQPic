package com.alensw.a;

class ai extends com.alensw.b.k.a {
   final com.alensw.a.ah a;

   ai(com.alensw.a.ah var1, int var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.a = var1;
   }

   @Override
   protected void a(Runnable var1) {
      super.a(var1);
      if (var1 instanceof com.alensw.a.ak) {
         com.alensw.a.ah.a(this.a).remove(((com.alensw.a.ak)var1).b());
      }
   }

   @Override
   protected void afterExecute(Runnable var1, Throwable var2) {
      super.afterExecute(var1, var2);
      if (var1 instanceof com.alensw.a.ak) {
         com.alensw.a.ah.a(this.a).remove(((com.alensw.a.ak)var1).b());
      }
   }
}
