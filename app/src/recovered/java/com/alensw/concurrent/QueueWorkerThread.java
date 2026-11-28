package com.alensw.concurrent;

class QueueWorkerThread extends StoppableThread {
   final QueuedWorker a;

   QueueWorkerThread(QueuedWorker var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a();
      int delay = 0;
      while (!a(delay)) {
         synchronized (QueuedWorker.a(this.a)) {
            QueuedWorker.a(this.a, QueuedWorker.a(this.a).b());
            if (QueuedWorker.b(this.a) == null) {
               try {
                  QueuedWorker.a(this.a).wait();
               } catch (Throwable ignored) {
                  // Continue through the stop check after a wakeup or an interrupt.
               }
               continue;
            }
         }
         delay = this.a.b(QueuedWorker.b(this.a));
         synchronized (QueuedWorker.a(this.a)) {
            QueuedWorker.b(this.a).a();
            QueuedWorker.a(this.a, null);
         }
      }
      this.a.d();
   }
}
