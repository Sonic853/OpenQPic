package com.alensw.concurrent;

import java.util.concurrent.ThreadFactory;

class PriorityThreadFactory implements ThreadFactory {
   private final int a;

   public PriorityThreadFactory(int var1) {
      this.a = var1;
   }

   static int a(PriorityThreadFactory var0) {
      return var0.a;
   }

   @Override
   public Thread newThread(Runnable var1) {
      return new PriorityThread(this, var1);
   }
}
