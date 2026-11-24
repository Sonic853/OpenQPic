package com.alensw.ui.b;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.view.animation.AnimationUtils;
public class a extends InsetDrawable {
   private int a;
   private long b;
   private Bitmap c;
   private final Paint d;

   public a(Drawable var1, boolean var2) {
      super(var1, 0);
      byte var3 = 0;
      this.d = new Paint(6);
      if (var2) {
         var3 = 2;
      }

      this.a = var3;
   }

   public void a() {
      this.a = 1;
      this.b = AnimationUtils.currentAnimationTimeMillis();
      this.invalidateSelf();
   }

   @Override
   public void draw(Canvas var1) {
      float var2 = (float)Math.min(300L, AnimationUtils.currentAnimationTimeMillis() - this.b);
      if (var2 >= 300.0F) {
         if (this.a == 1) {
            this.a = 2;
         } else if (this.a == 3) {
            this.a = 0;
            this.setColorFilter(null);
         }
      }

      if (this.a == 0) {
         super.draw(var1);
      } else {
         float var3 = var2 / 300.0F;
         var2 = var3;
         if (this.a == 3) {
            var2 = 1.0F - var3;
         }

         Rect var6 = this.getBounds();
         float var4 = var6.width();
         float var5 = var6.height();
         var3 = var6.left;
         var5 = var5 * 0.3125F + var6.top;
         com.alensw.compat.LegacyPlatform.save(var1, 3);
         var1.rotate(var2 * -30.0F, var4 * 0.28125F + var3, var5);
         if (this.c != null) {
            var1.drawBitmap(this.c, var6.left, var6.top, this.d);
         } else {
            var1.clipRect((float)var6.left, (float)var6.top, (float)var6.right, var5);
            super.draw(var1);
         }

         var1.restore();
         com.alensw.compat.LegacyPlatform.save(var1, 2);
         var1.clipRect((float)var6.left, var5, (float)var6.right, (float)var6.bottom);
         super.draw(var1);
         var1.restore();
      }

      if (this.a == 1 || this.a == 3) {
         this.invalidateSelf();
      }
   }

   @Override
   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      if (this.c != null) {
         this.c.recycle();
      }

      this.c = com.alensw.b.h.b.a(var1.width(), Math.round(var1.height() * 0.3125F), Config.ARGB_8888);
      if (this.c != null) {
         super.draw(new Canvas(this.c));
      }
   }
}
