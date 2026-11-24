package com.alensw.a;

import android.annotation.TargetApi;
import android.content.ContentValues;
import com.alensw.jni.JniUtils;

class bg extends com.alensw.b.k.c {
   final com.alensw.a.bc a;
   private final ContentValues b;
   private final String[] c;

   private bg(com.alensw.a.bc var1) {
      this.a = var1;
      this.b = new ContentValues();
      this.c = new String[1];
   }

   bg(com.alensw.a.bc owner, com.alensw.a.bd unused) {
      this(owner);
   }

   private int a(String var1, String[] var2) {
      int var3;
      try {
         var3 = com.alensw.a.bc.c(this.a).delete("thumbs", var1, var2);
      } catch (Throwable var4) {
         var3 = -1;
      }

      return var3;
   }

   private boolean a(com.alensw.a.bf var1) {
      boolean var2 = true;
      switch (var1.a) {
         case 1:
            var2 = this.b(var1);
            break;
         case 2:
            var2 = this.c(var1);
            break;
         case 3:
            this.c[0] = var1.d;
            if (this.a("path=?", this.c) <= 0) {
               var2 = false;
            }
            break;
         case 4:
            this.c[0] = com.alensw.b.l.b.e(var1.d) + '%';
            if (this.a("path LIKE ?", this.c) <= 0) {
               var2 = false;
            }
            break;
         case 5:
            if (this.a(null, null) <= 0) {
               var2 = false;
            }
            break;
         default:
            var2 = false;
      }

      return var2;
   }

   private boolean b(com.alensw.a.bf item) {
      this.b.put("path", item.d);
      if (item.b == 0L && item.d.startsWith("/")) item.b = JniUtils.fuGetFileTime(item.d);
      this.b.put("modified", item.b);
      if (item.c != null) this.b.put("thumb", item.c);
      try {
         return com.alensw.a.bc.c(this.a).replace("thumbs", null, this.b) != -1L;
      } catch (Throwable ignored) {
         return false;
      } finally {
         this.b.clear();
      }
   }

   private boolean c(com.alensw.a.bf item) {
      if (item.b == 0L && item.d.startsWith("/")) item.b = JniUtils.fuGetFileTime(item.d);
      this.b.put("modified", item.b);
      String selection = "path=\"" + item.d + "\" AND modified != " + item.b;
      try {
         return com.alensw.a.bc.c(this.a).update("thumbs", this.b, selection, null) > 0;
      } catch (Throwable ignored) {
         return false;
      } finally {
         this.b.clear();
      }
   }

   @TargetApi(11)
   @Override
   public void run() {
      android.os.Process.setThreadPriority(7);
      com.alensw.a.bc.a(this.a, false);
      while (!this.a(0)) {
         com.alensw.a.be queue = com.alensw.a.bc.a(this.a);
         synchronized (queue) {
            int size = queue.a();
            if (size < 16) {
               try {
                  if (size == 0) queue.wait();
                  else queue.wait(3000L);
                  if (queue.a() == 0) continue;
               } catch (Throwable ignored) {
                  // An interrupted wait still allows a pending batch to be attempted.
               }
            }
         }
         try {
            if (com.alensw.a.bc.b(this.a)) {
               com.alensw.a.bc.c(this.a).beginTransactionNonExclusive();
            } else {
               com.alensw.a.bc.c(this.a).beginTransaction();
            }
            try {
               int completed = 0;
               for (int i = 0; i < 16; i++) {
                  com.alensw.a.bf item = queue.b();
                  if (item != null && this.a(item)) completed++;
               }
               if (completed > 0) com.alensw.a.bc.c(this.a).setTransactionSuccessful();
            } finally {
               com.alensw.a.bc.c(this.a).endTransaction();
            }
         } catch (Throwable ignored) {
            // Retry on the next queue iteration.
         }
      }
      this.a.a();
   }
}
