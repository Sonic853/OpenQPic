package com.alensw.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import com.alensw.PicFolder.QuickApp;
import com.alensw.concurrent.AsyncExecutor;
import java.util.Locale;

public class Picture extends PictureBitmap {
   protected static AsyncExecutor c = new AsyncExecutor(1, 2, 4, 5);
   public int d;
   public String h = "image/bmp";
   public Uri i = Uri.EMPTY;
   public boolean animatedPreview;

   public Picture(int var1, int var2, int var3, Uri var4, String var5) {
      super(var1, var2, 0, 0);
      this.a(var3, var4, var5);
   }

   public Picture(Bitmap var1, int var2) {
      super(var1);
      this.a(var2, null, "image/bmp");
   }

   public Picture(Bitmap var1, int var2, Uri var3, BitmapDecodeOptions var4) {
      super(var1, var4.b, var4.c);
      this.a(var2, var3, var4.outMimeType);
   }

   public Picture(Uri var1, BitmapDecodeOptions var2) {
      super(var2.outWidth, var2.outHeight, var2.b, var2.c);
      byte var3;
      if (var2.c()) {
         var3 = -1;
      } else {
         var3 = 0;
      }

      this.a(var3, var1, var2.outMimeType);
   }

   public static float a(float[] var0) {
      float var1 = var0[0];
      float var2 = var0[3];
      return (float)Math.hypot(var1, var2);
   }

   public static String a(String var0, boolean var1) {
      int var2;
      if (var0 != null) {
         var2 = var0.lastIndexOf(46);
      } else {
         var2 = -1;
      }

      if (var2 == -1) {
         var0 = "";
      } else {
         String var3 = var0.substring(var2 + 1);
         var0 = var3;
         if (var1) {
            var0 = var3.toLowerCase(Locale.ENGLISH);
         }
      }

      return var0;
   }

   private void a(int var1, Uri var2, String var3) {
      this.d = var1;
      if (var3 == null) {
         var3 = "image/bmp";
      }

      this.h = var3;
      if (var2 == null) {
         var2 = Uri.EMPTY;
      }

      this.i = var2;
      this.b.e = a(this.i.toString());
   }

   public static boolean a(String var0) {
      return "jps".equals(a(var0, true));
   }

   public static float b(float[] var0) {
      return (float)Math.toDegrees(Math.atan2(var0[3], var0[4]));
   }

   public static boolean b(String var0) {
      boolean var2 = true;
      var0 = a(var0, true);
      boolean var1 = var2;
      if (!"jps".equals(var0)) {
         if ("mpo".equals(var0)) {
            var1 = var2;
         } else {
            var1 = false;
         }
      }

      return var1;
   }

   public void a(int var1, int var2, Matrix var3, RectF var4) {
   }

   public void a(Canvas var1, Matrix var2, Picture var3, Matrix var4, Paint var5, boolean var6) {
      if (this.j()) {
         var1.drawBitmap(this.d(), var2, var5);
      } else if (var3 != null && var3.j()) {
         var1.drawBitmap(var3.d(), var4, var5);
      }
   }

   public void a(boolean var1) {
   }

   public final boolean a(Uri var1) {
      boolean var2;
      if (this.i != var1 && !this.i.equals(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean a(View var1, Paint var2) {
      return true;
   }

   static int gifPlaybackRepeatCount(int encodedRepeatCount) {
      // Both GIF decoders use -1 for infinite repeats and 0 for a single play.
      return PreferenceManager.getDefaultSharedPreferences(QuickApp.a())
            .getBoolean("viewer_ignore_gif_loop_limit", true) ? -1 : encodedRepeatCount;
   }

   public long b() {
      return 0L;
   }

   public Picture c() {
      return (Picture)super.l();
   }

   @Override
   public Object l() {
      return this.c();
   }

   @Override
   public String toString() {
      return super.toString() + ", type=" + this.d + ", duration=" + this.b() + ", uri=" + this.i;
   }
}
