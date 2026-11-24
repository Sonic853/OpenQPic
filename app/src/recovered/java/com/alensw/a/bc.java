package com.alensw.a;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.os.Build;
import android.os.Environment;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import com.alensw.PicFolder.QuickApp;
public class bc {
   public static final Config a = Config.RGB_565;
   private static final String[] h = new String[]{"thumb", "modified"};
   private static final byte[] p = new byte[1];
   public boolean b;
   public final int c;
   public final int d;
   public final int e;
   public final int f;
   public final int g;
   private final Context i;
   private final int j;
   private final String k;
   private boolean l;
   private boolean m;
   private SQLiteDatabase n;
   private com.alensw.a.bg o;
   private final com.alensw.a.be q = new com.alensw.a.be(null);

   public bc(Context var1) {
      this.i = var1;
      this.b = PreferenceManager.getDefaultSharedPreferences(var1).getBoolean("crop_thumbnail", true);
      DisplayMetrics var12 = var1.getResources().getDisplayMetrics();
      float var2 = var12.density;
      float var3 = Math.max((float)Math.min(var12.widthPixels, var12.heightPixels), QuickApp.h * var2);
      int var10 = (int)(144.0F * var2);
      int var5 = Math.max((int)(var3 / var10), 3);
      int var9 = (int)(2.0F * var2) * (1 << Math.round(QuickApp.h / 400.0F));
      int var4 = var5;
      if (QuickApp.h == 400) {
         var4 = var5;
         if (var2 > 1.0F) {
            var4 = var5 + 1;
         }
      }

      int var7 = (int)((var3 - var9) / var4 - var9);
      int var6;
      if ((var7 & 1) == 1) {
         var6 = var7 - 1;
      } else {
         var6 = var7;
      }

      var5 = var7;
      if ((var7 & 1) == 1) {
         var5 = var7 - 1;
      }

      this.k = "thumb_" + Integer.toString(var6 * var5) + ".db";
      int var8 = var6;
      var7 = var9;
      var9 = var4;
      if (var2 >= 2.0F) {
         var5 = Math.max((int)(var3 / var10), 3);
         int var11 = Math.round(2.0F * var2 * (QuickApp.h / 320));
         var4 = var5;
         if (QuickApp.h == 400) {
            var4 = var5;
            if (var2 > 1.0F) {
               var4 = var5 + 1;
            }
         }

         var10 = (int)((var3 - var11) / var4 - var11);
         if ((var10 & 1) == 1) {
            var6 = var10 - 1;
         } else {
            var6 = var10;
         }

         var5 = var10;
         var8 = var6;
         var7 = var11;
         var9 = var4;
         if ((var10 & 1) == 1) {
            var5 = var10 - 1;
            var9 = var4;
            var7 = var11;
            var8 = var6;
         }
      }

      this.d = var8;
      this.e = var5;
      this.j = Math.max(this.d, 120);
      var4 = Math.round(3.0F * var9 / 4.0F);
      var4 = (int)((var3 - var7) / var4 - var7);
      if ((var4 & 1) == 1) {
         var5 = var4 - 1;
      } else {
         var5 = var4;
      }

      var6 = var4;
      if ((var4 & 1) == 1) {
         var6 = var4 - 1;
      }

      this.f = var5;
      this.g = var6;
      byte var16;
      if (a == Config.ARGB_8888) {
         var16 = 4;
      } else {
         var16 = 2;
      }

      var5 = com.alensw.b.h.b.b * 1024000 / 4;
      this.c = Math.min(Math.round((float)var12.widthPixels * var12.heightPixels / (this.d + var7) / (var7 + this.e)) * 10, var5 / (var16 * this.d * this.e));
   }

   static com.alensw.a.be a(com.alensw.a.bc var0) {
      return var0.q;
   }

   private void a(SQLiteDatabase var1) {
      var1.execSQL("CREATE TABLE thumbs (path TEXT PRIMARY KEY,thumb BLOB,modified INTEGER);");
   }

   private void a(SQLiteDatabase var1, int var2, int var3) {
      var1.execSQL("DROP TABLE IF EXISTS thumbs");
      this.a(var1);
   }

   @TargetApi(16)
   private boolean a(Context context, boolean readOnly) {
      String name = "thumb_" + Integer.toString(this.d * this.e) + ".db";
      File file = com.alensw.b.c.b.b(context, name);
      if (!file.exists() && !name.equals(this.k)) {
         File old = com.alensw.b.c.b.b(context, this.k);
         if (old.exists()) old.renameTo(file);
      }
      int flags = readOnly ? SQLiteDatabase.OPEN_READONLY : SQLiteDatabase.CREATE_IF_NECESSARY;
      SQLiteDatabase db = null;
      Throwable failure;
      try {
         if (Build.VERSION.SDK_INT >= 16) flags |= SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING;
         db = SQLiteDatabase.openDatabase(file.getPath(), null, flags);
         if (!readOnly && Build.VERSION.SDK_INT >= 16) this.m = db.isWriteAheadLoggingEnabled();
         int version = db.getVersion();
         if (version != 4) {
            db.beginTransaction();
            try {
               if (version == 0) {
                  this.a(db);
               } else if (version < 4) {
                  com.alensw.b.l.b.c(context, context.getString(com.alensw.PicFolder.R.string.cache_need_upgrade));
                  this.a(db, version, 4);
               }
               db.setVersion(4);
               db.setTransactionSuccessful();
            } finally {
               db.endTransaction();
            }
         }
         this.n = db;
         this.l = true;
         return true;
      } catch (SQLiteDatabaseCorruptException error) {
         com.alensw.b.a.f.b(db);
         com.alensw.b.a.f.a(file);
         failure = error;
      } catch (Throwable error) {
         com.alensw.b.a.f.b(db);
         failure = error;
      }
      if (!readOnly && "mounted".equals(Environment.getExternalStorageState())) {
         com.alensw.b.l.b.a(context, failure);
      }
      return false;
   }

   static boolean a(com.alensw.a.bc var0, boolean var1) {
      return var0.a(var1);
   }

   private synchronized boolean a(boolean readOnly) {
      if (this.l) {
         if (readOnly) return true;
         SQLiteDatabase db = this.n;
         if (db != null && !db.isReadOnly()) return true;
         this.a();
      }
      return this.a(this.i, readOnly);
   }

   static boolean b(com.alensw.a.bc var0) {
      return var0.m;
   }

   static SQLiteDatabase c(com.alensw.a.bc var0) {
      return var0.n;
   }

   static byte[] e() {
      return p;
   }

   public Bitmap a(com.alensw.b.c.f var1, com.alensw.b.h.a var2) {
      com.alensw.b.h.c var6 = com.alensw.b.h.c.a(var1, true);
      if (var6 != null) {
         Bitmap var5 = com.alensw.b.h.b.a(var6, 196608, var2);
         if (var5 != null) {
            var6.a();
            return var5;
         }

         long var3 = var6.e();
         var2.outWidth = (int)(var3 & 2147483647L);
         var2.outHeight = (int)(var3 >> 32 & 2147483647L);
         var2.b = var6.d();
         var2.c = var6.f();
         var2.d = var6.c();
         var6.a();
      }

      if ((var2.outWidth <= 0 || var2.outHeight <= 0) && !var2.mCancel) {
         com.alensw.b.h.b.b(var1, var2);
      }

      Bitmap var7;
      if (var2.outWidth > 0 && var2.outHeight > 0 && !var2.mCancel) {
         var7 = com.alensw.b.h.b.a(var1, this.b, this.j, var2);
      } else {
         var7 = null;
      }

      return var7;
   }

   public com.alensw.b.h.h a(Uri var1, long var2, com.alensw.b.h.a var4) {
      String var5;
      if (com.alensw.b.c.f.a(var1)) {
         var5 = var1.getPath();
      } else {
         var5 = var1.toString();
      }

      return this.a(var5, var2, var4);
   }

   public com.alensw.b.h.h a(com.alensw.b.c.f var1, char var2, int var3, int var4, long var5, com.alensw.b.h.a var7) {
      boolean var9 = true;
      Bitmap var10;
      if (var2 == 'I') {
         var10 = this.a(var1, var7);
      } else {
         Bitmap var11;
         if (var2 != 'D' && var2 != 0) {
            var11 = null;
         } else {
            var11 = this.a(var1, var7);
         }

         var10 = var11;
         if (var11 == null) {
            var10 = com.alensw.b.h.b.a(var1);
         }
      }

      boolean var8;
      Bitmap var17;
      if (var10 == null) {
         if (var7.mCancel) {
            return null;
         }

         var17 = com.alensw.b.h.b.a(1, 1, Config.ARGB_8888);
         if (var17 == null) {
            return null;
         }

         var17.eraseColor(-2139062144);
         var8 = true;
      } else {
         var8 = false;
         var17 = var10;
      }

      if (var3 <= 0 || var4 <= 0) {
         var9 = false;
      }

      Uri var15 = var1.g();
      String var16;
      if (var1.a()) {
         var16 = var15.getPath();
      } else {
         var16 = var15.toString();
      }

      com.alensw.b.h.h var14 = new com.alensw.b.h.h(var17, var7.b, var7.c);
      var14.b.e = com.alensw.b.h.j.a(var16);
      if (var9 && !var8 && (var14.b.d != 0 || var17.getWidth() != var3 || var17.getHeight() != var4 || var17.getConfig() != a)) {
         com.alensw.b.h.h var13 = var14.a(var3, var4, this.b, a);
         if (var13 == null) {
            Object var18 = null;
            return (com.alensw.b.h.h)var18;
         }

         var14.m();
         var14 = var13;
      }

      com.alensw.b.h.h var12 = var14;
      if (var14 != null) {
         var12 = var14;
         if (var5 != 0L) {
            if (var8 && var2 != 'V') {
               return var14;
            }

            this.a(var16, var5, var14);
            var12 = var14;
         }
      }

      return var12;
   }

   public com.alensw.b.h.h a(String path, long modified, com.alensw.b.h.a options) {
      byte[] bytes = this.q.a(path);
      if (bytes == p) return null;
      if (bytes == null && this.a(true) && !options.mCancel) {
         Cursor cursor = null;
         try {
            String[] args = {path};
            cursor = this.n.query("thumbs", h, "path=?", args, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
               long stored = cursor.getLong(1);
               if (stored == 0L || modified == 0L || com.alensw.b.a.f.a(stored, modified, 1)) {
                  bytes = cursor.getBlob(0);
               }
            }
         } catch (Throwable ignored) {
            // A missing or unreadable cache entry is a cache miss.
         } finally {
            if (cursor != null) cursor.close();
         }
      }
      return bytes == null || options.mCancel ? null : com.alensw.b.h.h.a(bytes, options);
   }

   public void a() {
      SQLiteDatabase var1 = this.n;
      this.n = null;
      this.l = false;
      com.alensw.b.a.f.b(var1);
   }

   public void a(String var1) {
      this.q.a(var1, 3, 0L, null);
   }

   public void a(String var1, long var2) {
      this.q.a(var1, 2, var2, null);
   }

   public void a(String var1, long var2, com.alensw.b.h.h var4) {
      byte[] var5 = var4.a(CompressFormat.JPEG, 75);
      if (var5 != null) {
         this.q.a(var1, 1, var2, var5);
      }
   }

   public void b() {
      this.q.a("*", 5, 0L, null);
   }

   public void b(String var1) {
      this.q.a(var1, 4, 0L, null);
   }

   public void c() {
      if (this.o == null) {
         this.o = new com.alensw.a.bg(this, null);
      }

      this.o.start();
   }

   public void d() {
      if (this.q.a() == 0 && this.o != null) {
         this.o.a(this.q);
         this.o = null;
      }
   }
}
