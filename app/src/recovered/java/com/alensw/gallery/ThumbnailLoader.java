package com.alensw.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Paint;
import com.alensw.bean.CommonFile;
import com.alensw.collection.ObjectPool;
import com.alensw.concurrent.AsyncExecutor;
import com.alensw.ui.shape.PlaybackIconShape;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThumbnailLoader {
   private static final int a;
   private final AsyncExecutor b;
   private final ConcurrentHashMap c;
   private final ConcurrentLinkedQueue d;
   private final Paint e;
   private final ContentResolver f;
   private final ObjectPool g;

   static {
      byte var0 = 1;
      if (AsyncExecutor.c > 1) {
         var0 = 2;
      }

      a = var0;
   }

   public ThumbnailLoader(Context var1) {
      this.b = new ThumbnailExecutor(this, a, a + 2, 256, 5);
      this.c = new ConcurrentHashMap(256);
      this.d = new ConcurrentLinkedQueue();
      this.e = new Paint(5);
      this.g = new ObjectPool(4);
      this.b.prestartCoreThread();
      this.e.setColor(-855638017);
      this.f = var1.getContentResolver();
   }

   static ConcurrentHashMap a(ThumbnailLoader var0) {
      return var0.c;
   }

   static Paint b(ThumbnailLoader var0) {
      return var0.e;
   }

   static ContentResolver c(ThumbnailLoader var0) {
      return var0.f;
   }

   static ConcurrentLinkedQueue d(ThumbnailLoader var0) {
      return var0.d;
   }

   protected PlaybackIconShape a() {
        PlaybackIconShape srcType0785;
        synchronized (this.g) {
            srcType0785 = (PlaybackIconShape) this.g.a();
        }
        return srcType0785 == null ? new PlaybackIconShape(0) : srcType0785;
    }

   public void a(ScannedMediaFile var1, boolean var2, ThumbnailCallback var3) {
      if (var1 != null && !this.c.containsKey(var1)) {
         LoadThumbnailTask var4 = new LoadThumbnailTask(this, var1, var2, var3);
         this.c.put(var4.b(), var4);
         this.b.execute(var4);
      }
   }

   public void a(CommonFile var1, boolean var2, ThumbnailCallback var3) {
      if (var1 != null && !this.c.containsKey(var1)) {
         LoadThumbnailTask var4 = new LoadThumbnailTask(this, var1, var2, var3);
         this.c.put(var4.b(), var4);
         this.b.execute(var4);
      }
   }

   protected void a(PlaybackIconShape param1) {
        synchronized (this.g) {
            this.g.b(param1);
        }
    }

   public void a(boolean var1) {
      this.b.a();
      if (var1) {
         while (true) {
            LoadThumbnailTask var2 = (LoadThumbnailTask)this.d.poll();
            if (var2 == null) {
               break;
            }

            this.c.remove(var2.b());
            var2.c();
         }
      }
   }
}
