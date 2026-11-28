package com.alensw.concurrent;

import java.util.concurrent.FutureTask;

public class CancellableFutureTask extends FutureTask {
   private final CancellableCallable a;

   public CancellableFutureTask(CancellableCallable var1) {
      super(var1);
      this.a = var1;
   }

   public CancellableCallable b() {
      return this.a;
   }

   @Override
   public boolean cancel(boolean var1) {
      var1 = super.cancel(var1);
      if (var1) {
         this.a.a();
      }

      return var1;
   }

   @Override
   public void done() {
      this.a.b();
   }

   @Override
   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof CancellableFutureTask && ((Object)this.a).equals(((CancellableFutureTask)var1).a)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   @Override
   public int hashCode() {
      return ((Object)this.a).hashCode();
   }
}
