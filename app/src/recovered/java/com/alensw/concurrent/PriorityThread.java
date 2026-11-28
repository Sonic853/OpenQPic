package com.alensw.concurrent;

import android.os.Process;

class PriorityThread extends Thread {
   final PriorityThreadFactory a;

   PriorityThread(PriorityThreadFactory var1, Runnable var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   public void run() {
      Process.setThreadPriority(PriorityThreadFactory.a(this.a));
      super.run();
   }
}
