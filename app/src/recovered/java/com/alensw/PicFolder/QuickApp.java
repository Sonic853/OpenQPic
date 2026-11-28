package com.alensw.PicFolder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;
import com.alensw.cache.ConcurrentLruCache;
import com.alensw.concurrent.AsyncExecutor;
import com.alensw.gallery.ExternalStorageDocuments;
import com.alensw.gallery.FolderScanner;
import com.alensw.gallery.GalleryFolders;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.PictureLoader;
import com.alensw.gallery.StorageVolumes;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.gallery.ThumbnailLoader;
import com.alensw.image.BitmapUtils;
import com.alensw.image.TiledPicture;
import com.alensw.sort.NameSortOrder;
import com.alensw.utils.GalleryUtils;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickApp extends Application {
   private static AtomicBoolean A = new AtomicBoolean();
   private static QuickApp B;
   private static final String[] F = new String[]{null, "com.samsung.app.playreadyui", "com.htc", "com.meizu.mstore"};
   public static final int[] a = new int[]{2560, 2048, 1920, 1800, 1600, 1280, 1024, 960, 800, 480, 320};
   public static boolean b;
   public static boolean c;
   public static boolean d;
   public static boolean e;
   public static int f;
   public static int g;
   public static int h;
   public static int i;
   public static int j;
   public static int k;
   public static String l;
   public static DateFormat m;
   public static DateFormat n;
   public static GalleryFolders o;
   public static StorageVolumes p;
   public static FolderScanner q;
   public static ThumbnailDatabase r;
   public static GalleryMediaStore s;
   public static ThumbnailLoader t;
   public static PictureLoader u;
   public static ConcurrentLruCache v;
   public static ConcurrentLruCache w;
   public static boolean x;
   public static String y;
   private static AtomicBoolean z = new AtomicBoolean();
   private final AtomicInteger C = new AtomicInteger();
   private final AtomicInteger D = new AtomicInteger();
   private final ArrayList E = new ArrayList();

   public static QuickApp a() {
      return B;
   }

   public static String a(long var0) {
      return m.format(var0);
   }

   static AtomicInteger a(QuickApp var0) {
      return var0.C;
   }

   public static void a(Context var0) {
      a(var0, new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(Environment.getExternalStorageDirectory())));
   }

   public static void a(Context var0, Intent var1) {
      try {
         var0.getApplicationContext().sendBroadcast(var1);
      } catch (Throwable var2) {
      }
   }

   public static boolean a(boolean var0) {
      if (var0) {
         x = false;
      }

      return A.getAndSet(var0);
   }

   public static String b(long var0) {
      return m.format(var0) + ' ' + n.format(var0);
   }

   static AtomicInteger b(QuickApp var0) {
      return var0.D;
   }

   public static boolean b(boolean var0) {
      if (var0) {
         x = false;
      }

      return z.getAndSet(var0);
   }

   private int f() {
      int var1 = 1;

      while (true) {
         if (var1 >= F.length) {
            var1 = 0;
            break;
         }

         if (GalleryUtils.a(this, F[var1])) {
            break;
         }

         var1++;
      }

      return var1;
   }

   @TargetApi(14)
   private boolean g() {
      boolean var7 = false;
      Resources var8 = this.getResources();
      Configuration var10 = var8.getConfiguration();
      DisplayMetrics var9 = var8.getDisplayMetrics();
      BitmapUtils.a(this);
      boolean var6;
      if ((var10.screenLayout & 15) >= 3) {
         var6 = true;
      } else {
         var6 = false;
      }

      b = var6;
      e = this.getPackageManager().hasSystemFeature("android.hardware.type.television");
      h = (int)(Math.min(var9.widthPixels, var9.heightPixels) / var9.density);
      if (VERSION.SDK_INT >= 13 && h < var10.smallestScreenWidthDp) {
         h = var10.smallestScreenWidthDp;
      }

      g = Math.round(h * var9.density);
      int var2 = Math.max(var9.widthPixels, var9.heightPixels);
      int[] var16 = a;
      int var5 = var16.length;
      int var3 = 0;
      int var1 = var2;

      int var4;
      while (true) {
         var4 = var2;
         if (var3 >= var5) {
            break;
         }

         var4 = var16[var3];
         if (var2 == var4) {
            var4 = var2;
            break;
         }

         if (var2 > var4) {
            var4 = var1;
            break;
         }

         var3++;
         var1 = var4;
      }

      f = var4;
      var6 = var7;
      if (VERSION.SDK_INT >= 14) {
         var6 = var7;
         if (!ViewConfiguration.get(this).hasPermanentMenuKey()) {
            var6 = true;
         }
      }

      d = var6;
      c = GalleryUtils.a(this, "com.google.android.gms");
      i = Math.max(var8.getInteger(17694721), 400);
      String var14;
      if (j == 2) {
         var14 = "100MEDIA";
      } else {
         var14 = "Camera";
      }

      l = var14;
      m = DateFormat.getDateInstance();
      n = android.text.format.DateFormat.getTimeFormat(this);
      o = new GalleryFolders();
      p = new StorageVolumes(this);
      q = new FolderScanner(this);
      r = new ThumbnailDatabase(this);
      t = new ThumbnailLoader(this);
      u = new PictureLoader(this);
      s = new GalleryMediaStore(this);
      ExternalStorageDocuments.a(this, p);
      TiledPicture.a(this, g);
      ContentResolver var15 = this.getContentResolver();
      u.a(new LoadMediaBucketIndex(this, var15));
      var1 = r.c;
      v = new AlbumCoverCache(this, var1 / 2);
      w = new MediaThumbnailCache(this, var1);
      return true;
   }

   @TargetApi(9)
   private void h() {
        boolean z2;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ExternalStorageDocuments.a = defaultSharedPreferences.getString("external_sd_uuid", null);
        android.content.SharedPreferences.Editor sharedPreferences$EditorEdit = defaultSharedPreferences.edit();
        String string = defaultSharedPreferences.getString("bkgnd_theme", null);
        if (string != null) {
            sharedPreferences$EditorEdit.remove("bkgnd_theme");
            if ("4".equals(string)) {
                sharedPreferences$EditorEdit.putInt("main_theme", 1);
                sharedPreferences$EditorEdit.putBoolean("auto_black_bkgnd", true);
            } else {
                sharedPreferences$EditorEdit.putInt("main_theme", GalleryUtils.a(string, 0));
            }
            z2 = true;
        } else {
            z2 = false;
        }
        if (j == 3 && g >= 720 && !defaultSharedPreferences.contains("split_bar")) {
            sharedPreferences$EditorEdit.putBoolean("split_bar", true);
            z2 = true;
        }
        q.a((defaultSharedPreferences.getBoolean("include_video", true) ? 2 : 0) | 1, defaultSharedPreferences);
        NameSortOrder.a(defaultSharedPreferences.getBoolean("sort_logical", true));
        if (android.os.Build.VERSION.SDK_INT >= 19 && d && !defaultSharedPreferences.contains("auto_hide_navigate_bar")) {
            sharedPreferences$EditorEdit.putBoolean("auto_hide_navigate_bar", true);
            z2 = true;
        }
        if (!defaultSharedPreferences.contains("show_controls") && defaultSharedPreferences.getBoolean("show_details", false)) {
            sharedPreferences$EditorEdit.putInt("show_controls", 3);
            sharedPreferences$EditorEdit.remove("show_details");
            z2 = true;
        }
        if (!defaultSharedPreferences.contains("protect_hidden")) {
            sharedPreferences$EditorEdit.putBoolean("protect_hidden", true);
            z2 = true;
        }
        if (z2) {
            GalleryUtils.a(sharedPreferences$EditorEdit);
        }
    }

   public void a(Activity var1) {
      if (VERSION.SDK_INT < 14) {
         this.C.incrementAndGet();
      }
   }

   public void a(Runnable runnable) {
        synchronized (this.E) {
            if (runnable != null) {
                if (!this.E.contains(runnable)) {
                    this.E.add(runnable);
                }
            }
        }
    }

   protected void b() {
      r.c();
      q.a();
   }

   public void b(Activity var1) {
      if (VERSION.SDK_INT < 14 && this.C.decrementAndGet() == 0) {
         this.d();
      }
   }

   public void b(Runnable runnable) {
        synchronized (this.E) {
            this.E.remove(runnable);
        }
    }

   protected void c() {
        synchronized (this.E) {
            java.util.Iterator it = this.E.iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
        }
        x = false;
        y = "";
        if (s.c() || !b(false)) {
            return;
        }
        a((Context) this);
    }

   public void c(Activity var1) {
      if (VERSION.SDK_INT < 14 && this.D.incrementAndGet() == 1) {
         this.b();
      }
   }

   protected void d() {
      if (q.b) {
         q.b = false;
         o.a(false);
      }

      r.d();
      q.b();
      u.a();
   }

   public void d(Activity var1) {
      if (VERSION.SDK_INT < 14 && this.D.decrementAndGet() == 0) {
         this.c();
      }
   }

   public int e() {
      int var1;
      try {
         var1 = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
         k = var1;
      } catch (Throwable var3) {
         var3.printStackTrace();
         var1 = -1;
      }

      return var1;
   }

   @Override
   public void onCreate() {
      super.onCreate();
      B = this;
      int var1 = this.e();

      try {
         j = this.f();
         this.g();
         this.h();
         StringBuilder var2 = new StringBuilder();
         Log.d("QuickApp", var2.append("v=").append(var1).append(", heap=").append(BitmapUtils.b).append(", core=").append(AsyncExecutor.c).toString());
      } catch (Throwable var4) {
         Log.e("QuickApp", "init app: ", var4);
         System.exit(0);
      }

      if (VERSION.SDK_INT >= 14) {
         label25: {
            Field var6;
            try {
               var6 = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            } catch (Exception var5) {
               break label25;
            }

            if (var6 != null) {
               try {
                  var6.setAccessible(true);
                  var6.setBoolean(ViewConfiguration.get(this), false);
               } catch (Exception var3) {
               }
            }
         }

         this.registerActivityLifecycleCallbacks(new GalleryActivityLifecycle(this));
      }
   }
}
