package com.alensw.b.a;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import java.util.ArrayList;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;

public class f {
   private static final String[] f = new String[]{"document_id", "_size", "_data"};
   protected boolean a;
   protected boolean b;
   protected SQLiteDatabase c;
   protected File d;
   protected long e = 536870912L;

   public static boolean a(long var0, long var2, int var4) {
      var0 = Math.abs(var0 - var2);
      boolean var5;
      if (var0 <= 86400L && var0 % 3600L <= Math.abs(var4)) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public static boolean a(File var0) {
      boolean deleted = false;
      try {
         if (VERSION.SDK_INT >= 16) return SQLiteDatabase.deleteDatabase(var0);
         String path = var0.getPath();
         deleted = var0.delete();
         deleted |= new File(path + "-shm").delete();
         deleted |= new File(path + "-wal").delete();
         deleted |= new File(path + "-journal").delete();
      } catch (Throwable e) {
         Log.e("FileCache", "delete: ", e);
      }
      return deleted;
   }

   public static void b(SQLiteDatabase var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Throwable var1) {
            Log.e("FileCache", "close: ", var1);
         }
      }
   }

   public static File c(Context var0, String var1) {
      return new File(com.alensw.b.c.b.b(var0), "." + var1);
   }

   public File a(String param1, long param2) {
      Cursor cursor = null;
      File file = null;
      try {
         String[] args = {param1};
         cursor = c.query("cache", new String[]{"_data", "last_modified"}, "document_id=?", args, null, null, null);
         if (cursor != null && cursor.moveToFirst()) {
            file = new File(cursor.getString(0));
            if (!file.exists() || cursor.getLong(1) != param2) {
               c.delete("cache", "document_id=?", args);
               file.delete();
               file = null;
            } else if (e > 0) {
               ContentValues values = new ContentValues();
               values.put("accessed_time", System.currentTimeMillis() / 1000L);
               c.update("cache", values, "document_id=?", args);
            }
         }
      } catch (Throwable failure) {
         Log.e("FileCache", "query cache: ", failure);
      } finally {
         if (cursor != null) cursor.close();
      }
      return file;
   }

   public void a(long var1) {
      this.e = var1;
   }

   @TargetApi(11)
   public void a(long param1, long param3) {
      Cursor cursor = null;
      try {
         cursor = c.query("cache", f, null, null, null, null, "accessed_time ASC");
         if (cursor == null || !cursor.moveToFirst()) return;
         ArrayList<String> paths = new ArrayList<>(Math.max(cursor.getCount(), 1));
         String[] args = new String[1];
         try {
            if (b) c.beginTransactionNonExclusive();
            else c.beginTransaction();
            try {
               int removed = 0;
               do {
                  String path = cursor.getString(2);
                  if (path != null) paths.add(path);
                  args[0] = cursor.getString(0);
                  param1 -= cursor.getLong(1);
                  c.delete("cache", "document_id=?", args);
                  removed++;
               } while (param1 > param3 && cursor.moveToNext());
               if (removed > 0) c.setTransactionSuccessful();
            } catch (Throwable failure) {
               Log.e("FileCache", "commit trim: ", failure);
            } finally {
               c.endTransaction();
            }
            for (String path : paths) new File(path).delete();
            Log.d("FileCache", "delete: " + paths.size() + " caches");
         } catch (Throwable failure) {
            Log.e("FileCache", "delete cache: ", failure);
         }
      } catch (Throwable failure) {
         Log.e("FileCache", "trim cache: ", failure);
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   protected void a(SQLiteDatabase var1) {
      var1.execSQL("CREATE TABLE cache (document_id TEXT UNIQUE,_data TEXT,_size INTEGER,accessed_time INTEGER,last_modified INTEGER);");
      var1.execSQL("CREATE INDEX cache_index_id ON cache(document_id);");
   }

   protected void a(SQLiteDatabase var1, int var2, int var3) {
      Log.w("FileCache", "Downgrading from version " + var2 + " -> " + var3);
      var1.execSQL("DROP TABLE IF EXISTS cache");
      this.a(var1);
   }

   public void a(String var1, long var2, File var4) {
      long var7 = var4.length();
      if (this.e > 0L) {
         long var5 = this.g() + var7;
         if (var5 > this.e) {
            this.a(var5, this.e);
         }
      }

      ContentValues var9 = new ContentValues(5);
      var9.put("document_id", var1);
      var9.put("_data", var4.getPath());
      var9.put("_size", var7);
      var9.put("accessed_time", System.currentTimeMillis() / 1000L);
      var9.put("last_modified", var2);

      try {
         this.c.replace("cache", null, var9);
      } catch (Throwable var10) {
         Log.e("FileCache", "update cache: ", var10);
      }
   }

   public void a(String var1, String[] var2) {
      try {
         int var3 = this.c.delete("cache", var1, var2);
         StringBuilder var5 = new StringBuilder();
         Log.d("FileCache", var5.append("delete cache: ").append(var3).toString());
      } catch (Throwable var4) {
         Log.e("FileCache", "delete cache: ", var4);
      }
   }

   public boolean a(Context param1, File param2, String param3, int param4) {
      if (param3 != null) {
         d = c(param1, param3);
         d.mkdirs();
      }
      SQLiteDatabase db = null;
      try {
         int flags = VERSION.SDK_INT >= 16 ? 0x30000000 : 0x10000000;
         db = SQLiteDatabase.openDatabase(param2.getPath(), null, flags);
         if (VERSION.SDK_INT >= 16) b = db.isWriteAheadLoggingEnabled();
         int oldVersion = db.getVersion();
         if (oldVersion != param4) {
            db.beginTransaction();
            try {
               if (oldVersion == 0) a(db);
               else if (oldVersion < param4) b(db, oldVersion, param4);
               else a(db, oldVersion, param4);
               db.setVersion(param4);
               db.setTransactionSuccessful();
            } finally {
               db.endTransaction();
            }
         }
         c = db;
         a = db.isOpen();
         return true;
      } catch (SQLiteDatabaseCorruptException failure) {
         Log.e("FileCache", "open corrupt db: ", failure);
         b(db);
         a(param2);
         return false;
      } catch (Throwable failure) {
         Log.e("FileCache", "open db: ", failure);
         b(db);
         return false;
      }
   }

   protected void b(SQLiteDatabase var1, int var2, int var3) {
      Log.w("FileCache", "Upgrading from version " + var2 + " -> " + var3);
      var1.execSQL("DROP TABLE IF EXISTS cache");
      this.a(var1);
   }

   public void c() {
      SQLiteDatabase var1 = this.c;
      this.c = null;
      this.a = false;
      b(var1);
   }

   public void c(String var1) {
      try {
         this.c.delete("cache", "document_id=?", new String[]{var1});
      } catch (Throwable var2) {
         Log.e("FileCache", "delete cache: ", var2);
      }
   }

   public boolean d() {
      return this.a;
   }

   public void e() {
      this.a(Long.MAX_VALUE, 0L);
   }

   public File f() {
      long var1 = System.nanoTime() / 1000L;
      return new File(this.d, Long.toHexString(var1));
   }

   public long g() {
      Cursor cursor = null;
      try {
         cursor = c.rawQuery("SELECT SUM(_size) FROM cache", null);
         if (cursor != null && cursor.moveToFirst()) return cursor.getLong(0);
      } catch (Throwable failure) {
         Log.e("FileCache", "query total size: ", failure);
      } finally {
         if (cursor != null) cursor.close();
      }
      return 0L;
   }
}
