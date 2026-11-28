package com.alensw.gallery;

import android.annotation.TargetApi;
import android.content.ContentValues;
import com.alensw.concurrent.StoppableThread;
import com.alensw.jni.JniUtils;
import com.alensw.utils.GalleryUtils;

class ThumbnailWriterThread extends StoppableThread {
   final ThumbnailDatabase a;
   private final ContentValues b;
   private final String[] c;

   private ThumbnailWriterThread(ThumbnailDatabase var1) {
      this.a = var1;
      this.b = new ContentValues();
      this.c = new String[1];
   }

   ThumbnailWriterThread(ThumbnailDatabase owner, ThumbnailStoreConstructorMarker unused) {
      this(owner);
   }

   private int a(String var1, String[] var2) {
      int var3;
      try {
         var3 = ThumbnailDatabase.c(this.a).delete("thumbs", var1, var2);
      } catch (Throwable var4) {
         var3 = -1;
      }

      return var3;
   }

   private boolean a(ThumbnailWriteRequest var1) {
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
            this.c[0] = GalleryUtils.e(var1.d) + '%';
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

   private boolean b(ThumbnailWriteRequest item) {
      this.b.put("path", item.d);
      if (item.b == 0L && item.d.startsWith("/")) item.b = JniUtils.fuGetFileTime(item.d);
      this.b.put("modified", item.b);
      if (item.c != null) this.b.put("thumb", item.c);
      try {
         return ThumbnailDatabase.c(this.a).replace("thumbs", null, this.b) != -1L;
      } catch (Throwable ignored) {
         return false;
      } finally {
         this.b.clear();
      }
   }

   private boolean c(ThumbnailWriteRequest item) {
      if (item.b == 0L && item.d.startsWith("/")) item.b = JniUtils.fuGetFileTime(item.d);
      this.b.put("modified", item.b);
      String selection = "path=\"" + item.d + "\" AND modified != " + item.b;
      try {
         return ThumbnailDatabase.c(this.a).update("thumbs", this.b, selection, null) > 0;
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
      ThumbnailDatabase.a(this.a, false);
      while (!this.a(0)) {
         ThumbnailWriteQueue queue = ThumbnailDatabase.a(this.a);
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
            if (ThumbnailDatabase.b(this.a)) {
               ThumbnailDatabase.c(this.a).beginTransactionNonExclusive();
            } else {
               ThumbnailDatabase.c(this.a).beginTransaction();
            }
            try {
               int completed = 0;
               for (int i = 0; i < 16; i++) {
                  ThumbnailWriteRequest item = queue.b();
                  if (item != null && this.a(item)) completed++;
               }
               if (completed > 0) ThumbnailDatabase.c(this.a).setTransactionSuccessful();
            } finally {
               ThumbnailDatabase.c(this.a).endTransaction();
            }
         } catch (Throwable ignored) {
            // Retry on the next queue iteration.
         }
      }
      this.a.a();
   }
}
