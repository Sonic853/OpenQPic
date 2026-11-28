package com.alensw.ui.activity;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.alensw.svg.SvgPicture;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectThemeActivity extends GalleryChromeActivity {
   public static final int[] a = new int[]{0, 1, 2, 3};
   public static final int[] b = new int[]{
      -28928,
      -37632,
      -49920,
      -2818048,
      -3862174,
      -1035142,
      -2804484,
      -5635841,
      -8630785,
      -10354454,
      -14064897,
      -12285185,
      -16736539,
      -16734786,
      -16738393,
      -16738680,
      -16731493,
      -16729737,
      -16731061
   };
   private GridView c;
   private Drawable d;
   private int e;
   private int f = 144;
   private final ArrayList g = new ArrayList(64);
   private final HashMap h = new HashMap(4);
   private final OnGlobalLayoutListener i = new ThemeGridLayoutListener(this);
   private final BaseAdapter j = new ThemePreviewAdapter(this);

   static GridView a(SelectThemeActivity var0) {
      return var0.c;
   }

   static int b(SelectThemeActivity var0) {
      return var0.f;
   }

   static ArrayList c(SelectThemeActivity var0) {
      return var0.g;
   }

   static int d(SelectThemeActivity var0) {
      return var0.e;
   }

   static HashMap e(SelectThemeActivity var0) {
      return var0.h;
   }

   static Drawable f(SelectThemeActivity var0) {
      return var0.d;
   }

   @Override
   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.c = new GridView(this);
      this.c.setNumColumns(2);
      this.c.getViewTreeObserver().addOnGlobalLayoutListener(this.i);
      this.setContentView(this.c);
      this.g(true);
      Resources var14 = this.getResources();
      float var2 = var14.getDisplayMetrics().density;
      this.e = (int)(24.0F * var2);
      this.f = (int)(144.0F * var2);
      this.d = SvgPicture.a(var14, com.alensw.PicFolder.R.raw.ic_menu_done, this.Y);
      int var3 = (int)(var2 * 4.0F);
      this.c.setHorizontalSpacing(var3);
      this.c.setVerticalSpacing(var3);
      this.c.setPadding(var3, var3, var3, var3);
      this.c.setBackgroundColor(-2236963);
      GridView var11 = this.c;
      boolean var10;
      if (VERSION.SDK_INT >= 11) {
         var10 = true;
      } else {
         var10 = false;
      }

      var11.setDrawSelectorOnTop(var10);
      Theme var13 = var14.newTheme();
      TypedValue var24 = new TypedValue();

      for (int var7 : a) {
         int var5 = h(var7);
         var13.applyStyle(var5, true);
         var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionBar, var24, true);
         int var6 = var14.getColor(var24.resourceId);
         var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorBackground, var24, true);
         int var8 = var14.getColor(var24.resourceId);
         var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionIcon, var24, true);
         int var9 = var14.getColor(var24.resourceId);
         this.g.add(new ThemeOption(var7, var5, var6, var8, var9));
      }

      var13.applyStyle(com.alensw.PicFolder.R.style.Theme_DarkColor, true);
      var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorBackground, var24, true);
      int var21 = var14.getColor(var24.resourceId);
      var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionIcon, var24, true);
      int var19 = var14.getColor(var24.resourceId);
      var13.applyStyle(com.alensw.PicFolder.R.style.Theme_LightColor, true);
      var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorBackground, var24, true);
      int var20 = var14.getColor(var24.resourceId);
      var13.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionIcon, var24, true);
      int var22 = var14.getColor(var24.resourceId);

      for (int var23 : b) {
         if (var23 >>> 24 == 255) {
            this.g.add(new ThemeOption(-3, com.alensw.PicFolder.R.style.Theme_DarkColor, var23, var21, var19));
         }

         this.g.add(new ThemeOption(-2, com.alensw.PicFolder.R.style.Theme_LightColor, var23, var20, var22));
      }

      this.c.setAdapter((ListAdapter)this.j);
      this.c.setOnItemClickListener(new SelectThemeClick(this));
   }

   @Override
   public void onDestroy() {
      this.c.getViewTreeObserver().removeGlobalOnLayoutListener(this.i);
      super.onDestroy();
   }
}
