package com.alensw.PicFolder;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class WallpaperActivity extends com.alensw.ui.activity.ao {
   private ListView a;
   private boolean b;
   private int c;
   private int d;
   private int e;
   private int f;
   private Uri g;
   private final Paint h = new Paint(1);
   private final RectF i = new RectF();

   static Paint a(WallpaperActivity var0) {
      return var0.h;
   }

   static RectF b(WallpaperActivity var0) {
      return var0.i;
   }

   protected Drawable a(Point var1) {
      float var2 = this.getResources().getDisplayMetrics().density;
      int var3 = (int)(4.0F * var2);
      return new com.alensw.PicFolder.bg(this, (int)(var2 * 16.0F), var1, var2 * 16.0F, var3, (int)(220.0F * var2));
   }

   protected void a(Intent var1) {
      ArrayList var3 = new ArrayList(4);
      var3.add(new Point(this.e, this.f));
      if (this.c > 0 && this.d > 0) {
         var3.add(new Point(this.c, this.d));
      } else {
         int var2 = Math.max(this.e, this.f);
         var3.add(new Point(var2, var2));
      }

      com.alensw.PicFolder.be var4 = new com.alensw.PicFolder.be(this, var3);
      this.a.setAdapter((ListAdapter)var4);
      this.a.setOnItemClickListener(new com.alensw.PicFolder.bf(this, var3, var1));
   }

   protected void a(Intent var1, int var2, int var3) {
      try {
         WallpaperManager var4 = WallpaperManager.getInstance(this);
         if (var2 != var4.getDesiredMinimumWidth() || var3 != var4.getDesiredMinimumHeight()) {
            var4.suggestDesiredDimensions(var2, var3);
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      var1.putExtra("aspectX", var2);
      var1.putExtra("aspectY", var3);
      var1.putExtra("outputX", var2);
      var1.putExtra("outputY", var3);
      com.alensw.ui.activity.a.a(this, var1);
      this.finish();
   }

   @Override
   protected void onActivityResult(int var1, int var2, Intent var3) {
      if (var2 != -1) {
         this.setResult(var2);
         this.finish();
      } else if (var1 == 1) {
         this.g = var3.getData();
      }
   }

   @Override
   public void onCreate(Bundle var1) {
      int var2 = 1;
      super.onCreate(var1);
      this.setContentView(com.alensw.PicFolder.R.layout.list_view);
      this.g(true);
      this.a = (ListView)this.findViewById(com.alensw.PicFolder.R.id.list);
      boolean var3;
      if (com.alensw.b.l.b.b(this) != 2) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.b = var3;
      if (!this.b) {
         var2 = 0;
      }

      this.setRequestedOrientation(var2);
      Point var4 = com.alensw.b.l.b.a(this);
      if (this.b && var4.x > var4.y) {
         var2 = var4.x;
         var4.x = var4.y;
         var4.y = var2;
      }

      this.e = var4.x;
      this.f = var4.y;
      SharedPreferences var7 = PreferenceManager.getDefaultSharedPreferences(this);
      this.c = var7.getInt("wallpaper_width", 0);
      this.d = var7.getInt("wallpaper_height", 0);
      if (this.c <= 0 || this.d <= 0) {
         WallpaperManager var5 = WallpaperManager.getInstance(this);
         this.c = var5.getDesiredMinimumWidth();
         this.d = var5.getDesiredMinimumHeight();
         Editor var8 = var7.edit();
         var8.putInt("wallpaper_width", this.c);
         var8.putInt("wallpaper_height", this.d);
         var8.commit();
      }

      this.g = this.getIntent().getData();
      if (this.g == null && var1 != null) {
         this.g = (Uri)var1.getParcelable("uri");
      }
   }

   @Override
   public void onResume() {
      super.onResume();
      Intent var1 = new Intent();
      if (this.g == null) {
         var1.setAction("android.intent.action.GET_CONTENT");
         var1.setClass(this, GalleryActivity.class);
         var1.setType("image/*");
         com.alensw.ui.activity.a.a(this, var1, 1);
      } else {
         var1.setAction("android.intent.action.SET_WALLPAPER");
         var1.setData(this.g);
         var1.setClass(this, CropActivity.class);
         var1.putExtras(this.getIntent());
         var1.putExtra("crop", "true");
         var1.putExtra("noFaceDetection", true);
         var1.putExtra("scale", true);
         var1.putExtra("setWallpaper", true);
         var1.putExtra("set-as-wallpaper", true);
         if (QuickApp.b && this.c > 0 && this.d > 0) {
            this.a(var1, this.c, this.d);
         } else if (this.e == this.c && this.f == this.d) {
            this.a(var1, this.c, this.d);
         } else {
            this.a(var1);
         }
      }
   }

   @Override
   protected void onSaveInstanceState(Bundle var1) {
      if (this.g != null) {
         var1.putParcelable("uri", this.g);
      }
   }
}
