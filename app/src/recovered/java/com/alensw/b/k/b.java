package com.alensw.b.k;

import android.util.Log;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

class b implements RejectedExecutionHandler {
   final com.alensw.b.k.a a;

   b(com.alensw.b.k.a var1) {
      this.a = var1;
   }

   @Override
   public void rejectedExecution(Runnable var1, ThreadPoolExecutor var2) {
      if (!var2.isShutdown()) {
         try {
            Runnable var3 = (Runnable)var2.getQueue().poll();
            this.a.a(var3);
            var2.execute(var1);
            StringBuilder var5 = new StringBuilder();
            Log.w("AsyncExecutor", var5.append("discard: ").append(var3).append(" -> ").append(var1).toString());
         } catch (Throwable var4) {
            Log.e("AsyncExecutor", "discard " + var1, var4);
         }
      }
   }
}
