package com.alensw.b.k;

public class c extends Thread {
   private volatile boolean a;

   public void a(Object param1) {
      this.a = true;
      try {
         if (param1 != null) {
            synchronized (param1) {
               param1.notifyAll();
            }
         }
         synchronized (this) {
            super.notifyAll();
         }
         super.join();
      } catch (Throwable ignored) {
         // The original stop operation returns even when joining is interrupted.
      }
   }

   protected boolean a(int param1) {
        if (param1 > 0 && !this.a) {
            try {
                synchronized (this) {
                    super.wait(param1);
                }
            } catch (Throwable th) {
            }
        }
        return this.a;
    }

   @Override
   public synchronized void start() {
      this.a = false;
      try {
         super.start();
      } catch (Throwable ignored) {
         // Original thread restarts are ignored.
      }
   }
}
