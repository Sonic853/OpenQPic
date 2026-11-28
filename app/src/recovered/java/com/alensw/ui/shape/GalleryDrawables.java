package com.alensw.ui.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build.VERSION;
import android.widget.ImageView;
import com.alensw.utils.GalleryUtils;
public class GalleryDrawables {
   public static final int[] a = new int[0];
   public static final int[] b = new int[]{16842910, 16842913};
   public static final int[] c = new int[]{-1, -3355444, -3355444, -1};

   public static Drawable a(int var0) {
      StateListDrawable var1 = new StateListDrawable();
      var1.addState(b, new ClipDrawable(new ColorDrawable(var0), 80, 2));
      var1.addState(a, new ColorDrawable(0));
      var1.setLevel(Math.round(416.66666F));
      return var1;
   }

   public static Drawable a(int var0, int var1) {
      float var2 = var1 * 3 / 4.0F;
      PaintDrawable var3 = new PaintDrawable(var0);
      var3.getPaint().setShadowLayer(var2, var2 / 4.0F, var2 / 4.0F, -16777216);
      var3.setCornerRadius(var1 / 2.0F);
      var3.setPadding(0, 0, 0, 0);
      return new InsetDrawable(var3, var1);
   }

   public static Drawable a(Context var0) {
      return new CircularClippedDrawable(GalleryUtils.e(var0), 0);
   }

   public static ShapeDrawable a(Shape var0, int var1, int var2, int var3) {
      ShapeDrawable var4 = new ShapeDrawable(var0);
      var4.getPaint().setColor(var3);
      var4.setIntrinsicWidth(var1);
      var4.setIntrinsicHeight(var2);
      return var4;
   }

   public static void a(ImageView var0) {
      if (VERSION.SDK_INT <= 21) {
         var0.setBackgroundDrawable(a(var0.getContext()));
      }
   }

   public static Shader b(int var0) {
      BitmapShader var1 = new BitmapShader(Bitmap.createBitmap(c, 2, 2, Config.RGB_565), TileMode.REPEAT, TileMode.REPEAT);
      if (var0 != 1) {
         Matrix var2 = new Matrix();
         var2.setScale(var0, var0);
         var1.setLocalMatrix(var2);
      }

      return var1;
   }
}
