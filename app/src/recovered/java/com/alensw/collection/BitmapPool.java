package com.alensw.collection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.alensw.image.BitmapUtils;

public class BitmapPool extends ObjectPool {
   private final int a;
   private final int b;
   private final Config c;

   public BitmapPool(int var1, int var2, int var3, Config var4) {
      super(var1);
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   public Bitmap a(int param1, int param2, Config param3) {
        Bitmap bitmap = null;
        if (param1 == this.a && param2 == this.b && param3 == this.c) {
            synchronized (this) {
                bitmap = (Bitmap) super.a();
            }
        }
        return bitmap == null ? BitmapUtils.a(this.a, this.b, this.c) : bitmap;
    }

   protected void a(Bitmap var1) {
      var1.recycle();
   }

   @Override
   protected void a(Object var1) {
      this.a((Bitmap)var1);
   }

   public void b(Bitmap param1) {
        if (param1 == null || param1.isRecycled()) {
            return;
        }
        if (param1.getWidth() != this.a || param1.getHeight() != this.b) {
            param1.recycle();
        } else {
            synchronized (this) {
                super.b((Object) param1);
            }
        }
    }

   @Override
   public void b(Object var1) {
      this.b((Bitmap)var1);
   }
}
