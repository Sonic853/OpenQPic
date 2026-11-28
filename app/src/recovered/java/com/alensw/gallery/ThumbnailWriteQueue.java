package com.alensw.gallery;

import com.alensw.cache.ConcurrentLruCache;

class ThumbnailWriteQueue {
   private ConcurrentLruCache a = new ConcurrentLruCache(128);

   private ThumbnailWriteQueue() {
   }

   ThumbnailWriteQueue(ThumbnailStoreConstructorMarker unused) {
      this();
   }

   public int a() {
      return this.a.a();
   }

   public void a(String param1, int param2, long param3, byte[] param5) {
        switch (param2) {
            case 1:
                ThumbnailWriteRequest srcType0106 = (ThumbnailWriteRequest) this.a.b(param1);
                if (srcType0106 != null) {
                    srcType0106.a = param2;
                    srcType0106.c = param5;
                    return;
                }
                break;
            case 2:
                ThumbnailWriteRequest srcType0107 = (ThumbnailWriteRequest) this.a.b(param1);
                if (srcType0107 != null) {
                    if (srcType0107.a != 1) {
                        srcType0107.a = param2;
                    }
                    srcType0107.b = param3;
                    return;
                }
                break;
            case 3:
                this.a.c(param1);
                break;
            case 5:
                this.a.c();
                break;
        }
        this.a.a(param1, new ThumbnailWriteRequest(param1, param2, param3, param5));
        synchronized (this) {
            notify();
        }
    }

   public byte[] a(String var1) {
      ThumbnailWriteRequest var2 = (ThumbnailWriteRequest)this.a.b(var1);
      byte[] var3;
      if (var2 != null) {
         if (var2.c != null) {
            var3 = var2.c;
         } else {
            var3 = ThumbnailDatabase.e();
         }
      } else {
         var3 = null;
      }

      return var3;
   }

   public ThumbnailWriteRequest b() {
      return (ThumbnailWriteRequest)this.a.b();
   }
}
