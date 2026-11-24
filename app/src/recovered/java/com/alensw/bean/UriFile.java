package com.alensw.bean;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.alensw.PicFolder.FileProvider;
import com.alensw.PicFolder.QuickApp;
import java.io.File;

public class UriFile extends CommonFile {
   public static final String[] k = new String[]{"_id", "_data", "_display_name", "mime_type", "_size", "date_modified", "datetaken"};

   protected UriFile(char var1, Uri var2, String var3, long var4, long var6, long var8) {
      super(var1, var2.getLastPathSegment(), var3, 0, var4, var6, var8, null, var2);
   }

   public static CommonFile a(char var0, Uri var1, String var2) {
      return a(var0, var1, var2, 0L, 0L, 0L);
   }

   public static CommonFile a(char type, Uri uri, String mime, long size, long modified, long taken) {
      String scheme = uri.getScheme();
      if ("file".equals(scheme)) {
         String path = uri.getPath();
         if (path != null) return a(path, type, mime, size, modified, taken);
      } else if ("content".equals(scheme)) {
         ContentResolver resolver = QuickApp.a().getContentResolver();
         String authority = uri.getAuthority();
         if ("media".equals(authority)) {
            Cursor cursor = null;
            try {
               cursor = resolver.query(uri, k, null, null, null);
               if (cursor != null && cursor.moveToFirst()) {
                  int id = cursor.getInt(0);
                  String path = cursor.getString(1);
                  String name = cursor.getString(2);
                  if (mime == null) mime = cursor.getString(3);
                  if (size == 0L) size = cursor.getLong(4);
                  if (modified == 0L) modified = cursor.getLong(5);
                  if (taken == 0L) {
                     taken = cursor.getLong(6) / 1000L;
                     if (taken == 0L) taken = modified;
                  }
                  if (type == 0) type = CommonFile.a(mime, name);
                  return new MediaStoreFile(type, id, name, mime, 0, size, modified, taken, path, uri);
               }
            } catch (Throwable ignored) {
               // The URI fallback retains each field successfully read before the failure.
            } finally {
               if (cursor != null) cursor.close();
            }
         } else if ("com.alensw.PicFolder.FileProvider".equals(authority)) {
            String path = FileProvider.a(uri);
            if (path != null) return a(path, type, mime, 0L, modified, taken);
         } else if (com.alensw.b.j.a.d(uri)) {
            Cursor cursor = null;
            try {
               cursor = resolver.query(uri, DocumentFolder.w, null, null, null);
               if (cursor != null && cursor.moveToFirst()) {
                  String documentId = cursor.getString(0);
                  String name = cursor.getString(1);
                  if (mime == null) mime = cursor.getString(2);
                  int flags = cursor.getInt(3);
                  if (size == 0L) size = cursor.getLong(4);
                  if (modified == 0L) modified = cursor.getLong(5) / 1000L;
                  if (taken == 0L) {
                     taken = cursor.getLong(6) / 1000L;
                     if (taken == 0L) taken = modified;
                  }
                  if (type == 0) type = CommonFile.a(mime, name);
                  return new DocumentFile(type, authority, documentId, name, mime, flags, size, modified, taken, null, uri);
               }
            } catch (Throwable ignored) {
               // Missing provider columns do not discard previously read URI metadata.
            } finally {
               if (cursor != null) cursor.close();
            }
         }
      }
      return new UriFile(type, uri, mime, size, modified, taken);
   }

   public static CommonFile a(Uri var0) {
      return a('\u0000', var0, null, 0L, 0L, 0L);
   }

   public static LocalFile a(String var0, char var1, String var2, long var3, long var5, long var7) {
      File var11 = new File(var0);
      if (var1 == 0) {
         var1 = com.alensw.b.f.c.a(var0);
      }

      if (var2 == null) {
         var0 = com.alensw.b.f.c.a(var0, var1);
      } else {
         var0 = var2;
      }

      long var9;
      if (var3 == 0L) {
         var9 = var11.length();
      } else {
         var9 = var3;
      }

      if (var5 == 0L) {
         var3 = var11.lastModified() / 1000L;
      } else {
         var3 = var5;
      }

      if (var7 == 0L) {
         var5 = var3;
      } else {
         var5 = var7;
      }

      return new LocalFile(var1, var11.getParent(), var11.getName(), var0, 0, var9, var3, var5);
   }
}
