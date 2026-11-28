package com.alensw.concurrent;

public class WorkQueue {
   private WorkItem a;
   private WorkItem b;

   public final WorkItem a(int var1, int var2, int var3, Object... var4) {
      WorkItem var5 = this.a;

      while (true) {
         if (var5 == null) {
            var5 = null;
            break;
         }

         if (var5.a(var1, var2, var3, var4)) {
            break;
         }

         var5 = var5.g;
      }

      return var5;
   }

   public void a() {
      for (WorkItem var1 = this.a; var1 != null; var1 = var1.g) {
         var1.a();
      }

      this.b = null;
      this.a = null;
   }

   public void a(WorkItem var1) {
      if (this.a == null) {
         this.b = var1;
         this.a = var1;
      } else {
         this.b.g = var1;
         this.b = var1;
         var1.g = null;
      }
   }

   public WorkItem b() {
      WorkItem var1 = this.a;
      if (var1 != null) {
         this.a = var1.g;
         var1.g = null;
      }

      return var1;
   }
}
