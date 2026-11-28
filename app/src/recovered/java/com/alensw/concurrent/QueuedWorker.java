package com.alensw.concurrent;

public class QueuedWorker {
   private long a;
   private StoppableThread b;
   private WorkItem c;
   private final WorkQueue d = new WorkQueue();

   static WorkItem a(QueuedWorker var0, WorkItem var1) {
      var0.c = var1;
      return var1;
   }

   static WorkQueue a(QueuedWorker var0) {
      return var0.d;
   }

   static WorkItem b(QueuedWorker var0) {
      return var0.c;
   }

   protected WorkItem a(int var1, int var2, int var3, Object... var4) {
      throw new RuntimeException("override!");
   }

   protected void a() {
   }

   protected void a(WorkItem var1) {
   }

   public void a(boolean param1) {
        synchronized (this.d) {
            this.d.a();
            if (param1 && this.c != null) {
                a(this.c);
            }
        }
    }

   protected int b(WorkItem var1) {
      return 0;
   }

   public void b() {
      if (this.b == null) {
         this.b = new QueueWorkerThread(this);
      }

      this.b.start();
      this.a = this.b.getId();
   }

   public boolean b(int param1, int param2, int param3, Object... param4) {
        boolean z = false;
        synchronized (this.d) {
            if (this.c == null || !this.c.a(param1, param2, param3, param4)) {
                if (this.d.a(param1, param2, param3, param4) == null) {
                    this.d.a(a(param1, param2, param3, param4));
                    this.d.notifyAll();
                    z = true;
                }
            }
        }
        return z;
    }

   public void c() {
      this.a(true);
      if (this.b != null) {
         this.b.a(this.d);
         this.b = null;
         this.a = 0L;
      }
   }

   protected void d() {
   }
}
