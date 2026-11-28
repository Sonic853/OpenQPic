package com.alensw.image;

import android.util.Log;
import com.alensw.cache.LruEntry;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ReferenceCountedEntry extends LruEntry {
   private final AtomicInteger a = new AtomicInteger(1);

   protected void a() {
   }

   public Object l() {
      this.a.incrementAndGet();
      return this;
   }

   public int m() {
      int var1 = this.a.decrementAndGet();
      if (var1 == 0) {
         this.a();
      } else if (var1 < 0) {
         Log.e("RefObject", "error release: " + var1 + ", " + this);
      }

      return var1;
   }

   protected int n() {
      return this.a.get();
   }
}
