package com.alensw.a;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Paint;
import com.alensw.bean.CommonFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ah {
   private static final int a;
   private final com.alensw.b.k.a b;
   private final ConcurrentHashMap c;
   private final ConcurrentLinkedQueue d;
   private final Paint e;
   private final ContentResolver f;
   private final com.alensw.b.i.c g;

   static {
      byte var0 = 1;
      if (com.alensw.b.k.a.c > 1) {
         var0 = 2;
      }

      a = var0;
   }

   public ah(Context var1) {
      this.b = new com.alensw.a.ai(this, a, a + 2, 256, 5);
      this.c = new ConcurrentHashMap(256);
      this.d = new ConcurrentLinkedQueue();
      this.e = new Paint(5);
      this.g = new com.alensw.b.i.c(4);
      this.b.prestartCoreThread();
      this.e.setColor(-855638017);
      this.f = var1.getContentResolver();
   }

   static ConcurrentHashMap a(com.alensw.a.ah var0) {
      return var0.c;
   }

   static Paint b(com.alensw.a.ah var0) {
      return var0.e;
   }

   static ContentResolver c(com.alensw.a.ah var0) {
      return var0.f;
   }

   static ConcurrentLinkedQueue d(com.alensw.a.ah var0) {
      return var0.d;
   }

   protected com.alensw.ui.d.g a() {
        com.alensw.ui.d.g srcType0785;
        synchronized (this.g) {
            srcType0785 = (com.alensw.ui.d.g) this.g.a();
        }
        return srcType0785 == null ? new com.alensw.ui.d.g(0) : srcType0785;
    }

   public void a(com.alensw.a.d var1, boolean var2, com.alensw.a.aj var3) {
      if (var1 != null && !this.c.containsKey(var1)) {
         com.alensw.a.ak var4 = new com.alensw.a.ak(this, var1, var2, var3);
         this.c.put(var4.b(), var4);
         this.b.execute(var4);
      }
   }

   public void a(CommonFile var1, boolean var2, com.alensw.a.aj var3) {
      if (var1 != null && !this.c.containsKey(var1)) {
         com.alensw.a.ak var4 = new com.alensw.a.ak(this, var1, var2, var3);
         this.c.put(var4.b(), var4);
         this.b.execute(var4);
      }
   }

   protected void a(com.alensw.ui.d.g param1) {
        synchronized (this.g) {
            this.g.b(param1);
        }
    }

   public void a(boolean var1) {
      this.b.a();
      if (var1) {
         while (true) {
            com.alensw.a.ak var2 = (com.alensw.a.ak)this.d.poll();
            if (var2 == null) {
               break;
            }

            this.c.remove(var2.b());
            var2.c();
         }
      }
   }
}
