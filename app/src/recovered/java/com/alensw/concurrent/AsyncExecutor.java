package com.alensw.concurrent;

import android.os.Build.VERSION;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncExecutor extends ThreadPoolExecutor {
   public static final boolean b;
   public static final int c;

   static {
      byte var0 = 1;
      boolean var2;
      if (VERSION.SDK_INT >= 11) {
         var2 = true;
      } else {
         var2 = false;
      }

      b = var2;
      int var1 = Runtime.getRuntime().availableProcessors();
      if (b) {
         var0 = 2;
      }

      c = Math.max(var1, var0);
   }

   public AsyncExecutor(int var1, int var2, int var3, int var4) {
      super(var1, var2, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(var3), new PriorityThreadFactory(var4));
      super.setRejectedExecutionHandler(new DiscardQueuedTaskPolicy(this));
   }

   public static AsyncExecutor a(int var0, int var1) {
      return new AsyncExecutor(var0, var0, Integer.MAX_VALUE, var1);
   }

   public CancellableFutureTask a(CancellableFutureTask var1) {
      try {
         super.execute(var1);
      } catch (Throwable var3) {
         Log.e("AsyncExecutor", "execute", var3);
      }

      return var1;
   }

   public CancellableFutureTask a(CancellableCallable var1) {
      return this.a(new CancellableFutureTask(var1));
   }

   public void a() {
      BlockingQueue var1;
      try {
         var1 = this.getQueue();
      } catch (Throwable var5) {
         Log.e("AsyncExecutor", "clear", var5);
         return;
      }

      while (true) {
         Runnable var2;
         try {
            var2 = (Runnable)var1.poll();
         } catch (Throwable var4) {
            Log.e("AsyncExecutor", "clear", var4);
            break;
         }

         if (var2 == null) {
            break;
         }

         try {
            this.a(var2);
         } catch (Throwable var3) {
            Log.e("AsyncExecutor", "clear", var3);
            break;
         }
      }
   }

   protected void a(Runnable var1) {
      if (var1 instanceof Future) {
         ((Future)var1).cancel(false);
      }
   }
}
