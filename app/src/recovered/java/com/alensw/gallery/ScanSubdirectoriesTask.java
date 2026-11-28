package com.alensw.gallery;

import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

class ScanSubdirectoriesTask implements Callable {
   final boolean a;
   final ScannedFolder b;
   final AtomicInteger c;
   final Stack d;
   final boolean e;
   final FolderScanRequest f;

   ScanSubdirectoriesTask(FolderScanRequest var1, boolean var2, ScannedFolder var3, AtomicInteger var4, Stack var5, boolean var6) {
      this.f = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
      this.e = var6;
   }

   public Void a() {
      if (this.a) {
         if (this.b.m() || this.b.g != 0 && !this.b.getPath().startsWith(ScannedFolder.b)) {
            String var2 = this.b.getPath();
            FolderScanner.a(this.f.f.a).c(var2);
            FolderScanner.b(this.f.f.a).c(var2);
            ScannedFolder var4 = new ScannedFolder(var2, 0);
            int var1 = FolderScanRequest.a(this.f, var4, true);
            if (this.b.h == null) {
               var4.h = null;
            }

            if (var1 == 0 || var4.e != this.b.e) {
               this.f.c.obtainMessage(13, 0, 0, this.b).sendToTarget();
            }

            if (var1 > 0 || this.b.i() > 0) {
               this.f.c.obtainMessage(12, 0, 0, var4).sendToTarget();
            }

            this.c.incrementAndGet();
         }
      } else {
         int var3 = FolderScanRequest.a(this.f, this.b, true);
         if (this.d != null && this.b.i() > 0) {
            this.d.addAll(this.b.h);
         }

         if (!this.e) {
            if (this.d == null && !this.f.keepPrescan(this.b, var3)) return null;
            this.b.h = null;
         }

         if (var3 > 0 || this.b.i() > 0) {
            this.f.c.obtainMessage(12, 0, 0, this.b).sendToTarget();
         }

         this.c.incrementAndGet();
      }

      return null;
   }

   @Override
   public Object call() {
      return this.a();
   }
}
