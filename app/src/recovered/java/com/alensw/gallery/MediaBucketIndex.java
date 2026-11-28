package com.alensw.gallery;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MediaBucketIndex {
   public static final Integer a = -1;
   private static final ConcurrentHashMap<String, Integer> g = new ConcurrentHashMap<>();
   private boolean b;
   private int c;
   private String d;
   private final ContentResolver e;
   private final HashMap<String, Integer> f = new HashMap<>();

   public MediaBucketIndex(ContentResolver var1) {
      this.e = var1;
   }

   public static void a(ContentResolver resolver) {
      String[] columns = {"bucket_id", "_data"};
      StorageVolumes storage = com.alensw.PicFolder.QuickApp.p;
      long start = System.currentTimeMillis();
      for (Uri uri : GalleryMediaStore.e) {
         Cursor cursor = null;
         try {
            cursor = com.alensw.PicFolder.GalleryStorage.buckets(resolver, uri, columns);
            if (cursor != null && cursor.moveToFirst()) {
               do {
                  int bucket = cursor.getInt(0);
                  String path = cursor.getString(1);
                  if (path != null && storage.b(path) && !GalleryUtils.f(path)) {
                     g.put(GalleryUtils.d(path), bucket);
                  }
               } while (cursor.moveToNext());
            }
         } catch (Throwable ignored) {
            // One unavailable media table does not invalidate the other buckets.
         } finally {
            if (cursor != null) cursor.close();
         }
      }
      Log.d("SysMediaStore", "query: " + g.size() + " albums, used=" + (System.currentTimeMillis() - start));
   }

   public static ArrayList<String> b() {
      ArrayList<String> var0 = new ArrayList<>(256);

      Enumeration<String> var1;
      try {
         var1 = g.keys();
      } catch (Throwable var3) {
         return var0;
      }

      while (true) {
         try {
            if (!var1.hasMoreElements()) {
               break;
            }

            var0.add(var1.nextElement());
         } catch (Throwable var2) {
            break;
         }
      }

      return var0;
   }

   public static boolean d(String var0) {
      boolean var1;
      if (var0 != null) {
         var1 = g.containsKey(var0);
      } else {
         var1 = false;
      }

      return var1;
   }

   @SuppressLint("DefaultLocale")
   public static Integer e(String var0) {
      Integer var2;
      if (var0 == null) {
         var2 = null;
      } else {
         Integer var3 = (Integer)g.get(var0);
         var2 = var3;
         if (var3 == null) {
            int var1 = var0.length();
            String var4 = var0;
            if (var1 > 1) {
               var4 = var0;
               if (var0.charAt(var1 - 1) == File.separatorChar) {
                  var4 = var0.substring(0, var1 - 1);
               }
            }

            var2 = var4.toLowerCase().hashCode();
         }
      }

      return var2;
   }

   @SuppressLint("DefaultLocale")
   private void f(String directory) {
      this.d = directory;
      this.c = this.d.length();
      this.b = GalleryUtils.h(this.d) || GalleryUtils.f(this.d);
      this.f.clear();
      if (android.os.Build.VERSION.SDK_INT >= 11) return;
      Integer bucket = e(this.d);
      boolean byBucket = bucket != null;
      String selection = byBucket ? "bucket_id=?" : "LOWER(_data) LIKE ?";
      String[] args = {byBucket ? bucket.toString() : this.d.toLowerCase() + '%'};
      HashMap<String, Integer> ids = this.f;
      int length = this.c;
      for (Uri uri : GalleryMediaStore.e) {
         Cursor cursor = null;
         try {
            cursor = Media.query(this.e, uri, GalleryMediaStore.j, selection, args, null);
            if (cursor != null && cursor.moveToFirst()) {
               do {
                  int id = cursor.getInt(0);
                  String path = cursor.getString(1);
                  if (path.length() > length && (byBucket || path.indexOf(File.separatorChar, length) == -1)) {
                     ids.put(path.substring(length), id);
                  }
               } while (cursor.moveToNext());
            }
         } catch (Throwable ignored) {
            // Keep the IDs already obtained from this table.
         } finally {
            if (cursor != null) cursor.close();
         }
      }
   }

   public synchronized Integer a(String path) {
      if (path == null || this.d == null || !path.startsWith(this.d)) return null;
      Integer id = this.f.get(path.substring(this.c));
      return id == null && this.b ? a : id;
   }

   public synchronized void a() {
      this.f.clear();
      this.d = null;
      this.c = 0;
      this.b = false;
   }

   public synchronized void a(String path, int id) {
      if (this.d != null && path.startsWith(this.d)) this.f.put(path.substring(this.c), id);
   }

   public synchronized void b(String directory) {
      String normalized = GalleryUtils.e(directory);
      if (this.d == null || !normalized.equalsIgnoreCase(this.d)) this.f(normalized);
   }

   public synchronized Integer c(String path) {
      return this.d != null && path.startsWith(this.d) ? this.f.remove(path.substring(this.c)) : null;
   }
}
