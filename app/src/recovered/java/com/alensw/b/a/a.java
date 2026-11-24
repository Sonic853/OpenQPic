package com.alensw.b.a;

import android.util.Log;
import java.util.concurrent.ConcurrentHashMap;

public class a {
   private int a;
   private final ConcurrentHashMap b;
   private final com.alensw.b.a.d c;

   public a(int var1) {
      this.a = var1;
      int var2 = var1;
      if (var1 <= 0) {
         var2 = 256;
      }

      this.b = new ConcurrentHashMap(var2);
      this.c = new com.alensw.b.a.d(this, null);
   }

   public final int a() {
      return this.b.size();
   }

   public com.alensw.b.a.c a(Object param1, com.alensw.b.a.c param2) {
        com.alensw.b.a.c srcType0135B;
        boolean z;
        param2.e = param1;
        synchronized (this.c) {
            this.c.a(param2);
        }
        com.alensw.b.a.c srcType0136 = (com.alensw.b.a.c) this.b.put(param1, param2);
        if (srcType0136 != null) {
            synchronized (this.c) {
                this.c.b(srcType0136);
            }
            srcType0136.e = null;
            a(srcType0136);
            return srcType0136;
        }
        synchronized (this.c) {
            srcType0135B = this.c.b();
            z = srcType0135B != null && this.b.size() > this.a;
            if (z) {
                this.c.b(srcType0135B);
            }
        }
        if (z) {
            this.b.remove(srcType0135B.e);
            srcType0135B.e = null;
            a(srcType0135B);
        }
        return null;
    }

   public void a(int var1) {
      this.a = var1;
   }

   protected void a(com.alensw.b.a.c var1) {
   }

   public final boolean a(Object var1) {
      return this.b.containsKey(var1);
   }

   public com.alensw.b.a.c b() {
        com.alensw.b.a.c srcType0135B;
        synchronized (this.c) {
            srcType0135B = this.c.b();
            if (srcType0135B != null) {
                this.c.b(srcType0135B);
            }
        }
        if (srcType0135B == null) {
            return null;
        }
        this.b.remove(srcType0135B.e);
        srcType0135B.e = null;
        a(srcType0135B);
        return srcType0135B;
    }

   public final com.alensw.b.a.c b(Object param1) {
        com.alensw.b.a.c srcType0135 = (com.alensw.b.a.c) this.b.get(param1);
        if (srcType0135 == null) {
            return null;
        }
        synchronized (this.c) {
            if (this.c.b(srcType0135)) {
                this.c.a(srcType0135);
            } else {
                Log.i("ConcurrentLruHashMap", "entry was unlinked: " + srcType0135);
            }
        }
        return srcType0135;
    }

   public void b(int var1) {
      for (int var2 = this.b.size() - var1; var2 > 0; var2--) {
         if (this.b() == null) {
            Log.v("ConcurrentLruHashMap", "eldest is null!");
            break;
         }
      }
   }

   public com.alensw.b.a.c c(Object param1) {
        com.alensw.b.a.c srcType0135 = (com.alensw.b.a.c) this.b.remove(param1);
        if (srcType0135 == null) {
            return null;
        }
        synchronized (this.c) {
            this.c.b(srcType0135);
        }
        srcType0135.e = null;
        a(srcType0135);
        return srcType0135;
    }

   public void c() {
        this.b.clear();
        synchronized (this.c) {
            this.c.a();
        }
    }
}
