package com.alensw.b.k;

import android.os.Process;

class j extends Thread {
   final com.alensw.b.k.i a;

   j(com.alensw.b.k.i var1, Runnable var2) {
      super(var2);
      this.a = var1;
   }

   @Override
   public void run() {
      Process.setThreadPriority(com.alensw.b.k.i.a(this.a));
      super.run();
   }
}
