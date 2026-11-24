package com.alensw.ui.c;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import com.alensw.PicFolder.GalleryActivity;
import com.alensw.PicFolder.QuickApp;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class a extends com.alensw.ui.c.cu implements Callback {
   public static final boolean a;
   private static String f;
   protected Handler b;
   protected ViewGroup c;
   protected OnClickListener d;
   protected com.alensw.ui.c.m e;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 14) {
         var0 = true;
      } else {
         var0 = false;
      }

      a = var0;
   }

   static String e(String var0) {
      f = var0;
      return var0;
   }

   @Override
   public long a(boolean var1, RectF var2) {
      if (!var1 && this.c != null && this.c.getVisibility() == 0) {
         this.c.setVisibility(4);
      }

      return super.a(var1, var2);
   }

   @TargetApi(8)
   @Override
   public void a() {
      boolean var4 = true;
      super.a();
      boolean var1;
      if (!(this instanceof com.alensw.ui.c.dp) && !(this instanceof com.alensw.ui.c.as)) {
         var1 = false;
      } else {
         var1 = true;
      }

      SharedPreferences var8 = PreferenceManager.getDefaultSharedPreferences(this.C);
      Window var6 = this.C.getWindow();
      int var2 = com.alensw.b.l.b.a(var8.getString("screen_orientation", "-1"), -1);
      if (var1) {
         if (var2 != -2) {
            this.C.a(var2);
         }
      } else {
         this.C.a(-1);
      }

      LayoutParams var7 = var6.getAttributes();
      if (var1 && var8.getBoolean("auto_max_brightness", false)) {
         var7.screenBrightness = 1.0F;
      } else {
         var7.screenBrightness = -1.0F;
      }

      var6.setAttributes(var7);
      boolean var5 = var8.getBoolean("hide_status_bar", false);
      boolean var3 = var5;
      if (!this.C.e(var8.getBoolean("auto_hide_navigate_bar", false))) {
         var3 = var5 | var1;
      }

      short var9;
      if (var3) {
         var9 = 1024;
      } else {
         var9 = 0;
      }

      var6.setFlags(var9, 1024);
      this.C.a(this.c);
      if (var1) {
         this.C.f((int)this.C.b(true) + 50);
      } else {
         com.alensw.ui.c.cx var11 = this.C;
         if (!var1) {
            var3 = var4;
         } else {
            var3 = false;
         }

         var11.a(var3);
      }
   }

   public void a(Intent var1) {
      var1.addFlags(1);
      this.C.setResult(-1, var1);
      this.C.finish();
   }

   public void a(Uri var1) {
      com.alensw.ui.c.m var2 = this.e;
      this.e = null;
      if (var2 != null) {
         Intent var3 = new Intent();
         var3.setDataAndType(var1, "vnd.android.document/directory");
         this.b.post(new com.alensw.ui.c.b(this, var2, var3));
         this.E();
      } else {
         this.a(var1, "vnd.android.document/directory");
      }
   }

   public void a(Uri var1, String var2) {
      Intent var3 = new Intent();
      var3.setDataAndType(var1, var2);
      this.a(var3);
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      this.b = new Handler(this);
      this.d = this.C.Z;
   }

   public final void a(Menu var1, int var2, int var3) {
      this.C.a(var1.findItem(var2), var3);
   }

   public final void a(Menu var1, int var2, boolean var3) {
      this.C.a(var1, var2, var3);
   }

   public final void a(MenuItem var1, int var2) {
      this.C.a(var1, var2);
   }

   public void a(com.alensw.a.e var1) {
      this.a(var1.getName(), Uri.fromFile(var1), var1.d(0));
   }

   public void a(com.alensw.a.e var1, int var2, com.alensw.ui.c.o var3) {
      if (var1 != null) {
         File var4 = var1.c(var2);
         if (var4 != null) {
            String var5 = com.alensw.b.l.b.c(var4.getName());
            com.alensw.ui.a.f.a(this.C, com.alensw.PicFolder.R.string.rename, var5, null, new com.alensw.ui.c.g(this, var5, var1, var2, var3));
         }
      }
   }

   public void a(com.alensw.a.e srcType0111, com.alensw.ui.a.s srcType0572) {
        ArrayList arrayList = new ArrayList();
        for (File parentFile = srcType0111; parentFile != null; parentFile = parentFile.getParentFile()) {
            String path = parentFile.getPath();
            if (path.lastIndexOf(File.separatorChar) <= 0) {
                break;
            }
            arrayList.add(path);
        }
        if (arrayList.isEmpty()) {
            arrayList.add(srcType0111.getPath());
        }
        com.alensw.ui.a.f.a(this.C, com.alensw.PicFolder.R.string.exclude, com.alensw.PicFolder.R.string.exclude_confirm, arrayList, srcType0572);
    }

   public void a(com.alensw.a.e var1, Runnable var2) {
      com.alensw.a.e var3 = new com.alensw.a.e(var1.getPath(), var1.e);
      var3.a(var1, false);
      this.c(true);
      QuickApp.u.a(new com.alensw.ui.c.h(this, var3, var1, var2));
   }

   public void a(com.alensw.ui.c.m var1) {
      this.e = var1;
   }

   public void a(File var1) {
      PreferenceManager.getDefaultSharedPreferences(this.C).edit().putString("dest_folder", var1.getPath()).commit();
      this.a(Uri.fromFile(var1));
   }

   @TargetApi(11)
   public void a(String var1, Uri var2, Uri var3) {
      com.alensw.b.h.h var7 = null;
      String var8 = com.alensw.a.n.a(this.C).a(var2);
      if (var8 != null) {
         var7 = QuickApp.r.a(var8, 0L, new com.alensw.b.h.a());
      }

      com.alensw.b.h.h var13;
      if (var7 != null && var7.j()) {
         var13 = var7;
      } else {
         var13 = QuickApp.r.a(var3, 0L, new com.alensw.b.h.a());
      }

      if (var13 != null && var13.j()) {
         Resources var16 = this.F();
         int var5;
         if (VERSION.SDK_INT >= 11) {
            var5 = ((ActivityManager)this.C.getSystemService("activity")).getLauncherLargeIconSize();
         } else {
            var5 = var16.getDimensionPixelSize(17104896);
         }

         Bitmap var15 = com.alensw.b.h.b.a(var5, var5, Config.ARGB_8888);
         if (var15 != null) {
            float var4 = var16.getDisplayMetrics().density;
            int var6 = (int)(4.0F * var4);
            RectF var10 = new RectF(0.0F, 0.0F, var5, var5);
            Paint var11 = new Paint(7);
            Matrix var17 = new Matrix();
            var13.a(var17, var5 - var6 * 2, var5 - var6 * 2, 2);
            var17.postTranslate(var6, var6);
            var11.setColor(-1);
            var11.setShadowLayer(var4, var4, var4, -12303292);
            var10.inset(2.0F, 2.0F);
            Canvas var9 = new Canvas(var15);
            var9.drawRect(var10, var11);
            var9.clipRect(var6, var6, var5 - var6, var5 - var6);
            var11.clearShadowLayer();
            var9.drawBitmap(var13.d(), var17, var11);
            var13.m();
            Intent var14 = new Intent();
            var14.setComponent(new ComponentName(this.C.getPackageName(), GalleryActivity.class.getName()));
            var14.setData(var2);
            var14.addFlags(69730304);
            Intent var12 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            var12.putExtra("android.intent.extra.shortcut.NAME", var1);
            var12.putExtra("android.intent.extra.shortcut.INTENT", var14);
            var12.putExtra("android.intent.extra.shortcut.ICON", var15);
            var12.putExtra("duplicate", false);
            this.C.sendBroadcast(var12);
         }
      }
   }

   public void a(HashSet var1, boolean var2, com.alensw.ui.c.n var3) {
      int var4 = var1.size();
      if (var4 != 0) {
         if (this.C.q()) {
            this.C.d(false);
         }

         com.alensw.ui.c.c var5 = new com.alensw.ui.c.c(this, var3, var2, var4, var1, this.C.getContentResolver());
         if (var2) {
            var4 = com.alensw.PicFolder.R.string.copy_to;
         } else {
            var4 = com.alensw.PicFolder.R.string.move_to;
         }

         Intent var6 = new Intent("android.intent.action.PICK");
         var6.setType("vnd.android.document/directory");
         var6.putExtra("activity_title", this.g(var4) + ":");
         ((com.alensw.ui.c.a)this.C.a(com.alensw.ui.c.bz.class, var6, null)).a(var5);
      }
   }

   public boolean a(int var1) {
      byte var3 = 2;
      SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(this.C);
      int var2 = QuickApp.q.c;
      if (var1 == 1) {
         var2 = 1;
      } else {
         var2 = var3;
         if (var1 != 2) {
            if (!var4.getBoolean("include_video", true)) {
               var3 = 0;
            }

            var2 = var3 | 1;
         }
      }

      boolean var5;
      if (QuickApp.q.c != var2) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         QuickApp.q.a(var2, var4);
      }

      if (com.alensw.b.b.b.a(var4.getBoolean("sort_logical", true)) | var5) {
         QuickApp.a(true);
      }

      return QuickApp.a(false);
   }

   public boolean a(com.alensw.a.e var1, boolean var2, Runnable var3) {
      boolean var4 = false;
      String var6 = var1.getPath();
      if (var2) {
         if (var1.g == 2) {
            Toast.makeText(this.C, com.alensw.PicFolder.R.string.can_not_hide_camera_folder, 1).show();
            var2 = var4;
         } else if (QuickApp.p.a(var6)) {
            Toast.makeText(this.C, "Can't hide the SD folder!", 1).show();
            var2 = var4;
         } else {
            com.alensw.a.ba var5 = QuickApp.s;
            new com.alensw.ui.c.j(this, this.C, this.g(com.alensw.PicFolder.R.string.running_wait), 0, var1, var5, var6, var3).c();
            var2 = true;
         }
      } else {
         if (com.alensw.b.l.b.a(var1, false) && !var1.a(true)) {
            QuickApp.s.a();
            new com.alensw.ui.c.l(this, this.C, this.g(com.alensw.PicFolder.R.string.running_wait), 0, var6).c();
         } else {
            this.a(var1.getPath());
         }

         var2 = true;
      }

      return var2;
   }

   public boolean a(String var1) {
      return this.C.a(var1);
   }

   public String b(String var1) {
      int var2 = var1.lastIndexOf(46);
      if (var2 >= 0) {
         if (var2 <= 32) {
            var1 = var1.substring(0, var2);
         } else {
            var1 = var1.substring(0, 32) + '…';
         }
      }

      return var1;
   }

   @Override
   public void b() {
      super.b();
      this.C.u();
   }

   public void c(String var1) {
      com.alensw.ui.a.f.a(this.C, 17039372, f, null, new com.alensw.ui.c.e(this, var1));
   }

   public void d(String var1) {
      if (var1 != null) {
         QuickApp.u.a(new com.alensw.ui.c.f(this, var1));
      }
   }

   public boolean d() {
      boolean var1;
      if (QuickApp.s.c()) {
         com.alensw.ui.a.f.a(this.C, 17039380, (CharSequence)this.g(com.alensw.PicFolder.R.string.can_not_operate_now));
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int[] e() {
      int var1 = (int)(this.F().getDisplayMetrics().density * (QuickApp.h / 400 * 8 + 72));
      return new int[]{Math.min(QuickApp.r.d, var1), Math.min(QuickApp.r.e, var1)};
   }

   @Override
   public boolean handleMessage(Message var1) {
      return false;
   }
}
