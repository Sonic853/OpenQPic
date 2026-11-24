package com.alensw.b.k;

import java.util.concurrent.FutureTask;

public class d extends FutureTask {
   private final com.alensw.b.k.k a;

   public d(com.alensw.b.k.k var1) {
      super(var1);
      this.a = var1;
   }

   public com.alensw.b.k.k b() {
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
      if (var1 instanceof com.alensw.b.k.d && ((Object)this.a).equals(((com.alensw.b.k.d)var1).a)) {
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
