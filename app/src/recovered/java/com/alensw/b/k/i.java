package com.alensw.b.k;

import java.util.concurrent.ThreadFactory;

class i implements ThreadFactory {
   private final int a;

   public i(int var1) {
      this.a = var1;
   }

   static int a(com.alensw.b.k.i var0) {
      return var0.a;
   }

   @Override
   public Thread newThread(Runnable var1) {
      return new com.alensw.b.k.j(this, var1);
   }
}
