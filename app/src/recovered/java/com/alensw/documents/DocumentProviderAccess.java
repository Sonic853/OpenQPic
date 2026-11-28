package com.alensw.documents;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Point;
import java.io.File;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import java.util.List;

public class DocumentProviderAccess {
   private DocumentProviderAccess() {}

   public static final String[] a = new String[]{"_display_name", "parent_id"};

   public static Uri a(ContentResolver var0, Uri var1, String var2, String var3) {
      Uri var6;
      if (VERSION.SDK_INT >= 11) {
         Bundle var4 = new Bundle();
         var4.putString("document_id", b(var1));
         var4.putString("mime_type", var2);
         var4.putString("_display_name", var3);
         Bundle var5 = var0.call(var1, "android:createDocument", null, var4);
         var6 = c(var1.getAuthority(), var5.getString("document_id"));
      } else {
         ContentValues var7 = new ContentValues(2);
         var7.put("mime_type", var2);
         var7.put("_display_name", var3);
         var6 = var0.insert(var1, var7);
      }

      return var6;
   }

   public static Uri a(Uri var0) {
      return var0.buildUpon().appendPath("children").build();
   }

   public static ParcelFileDescriptor a(ContentResolver var0, Uri var1, int var2) {
      try {
         if (VERSION.SDK_INT >= 11) {
            Bundle options = new Bundle();
            options.putParcelable("thumbnail_size", new Point(var2, var2));
            return var0.openTypedAssetFileDescriptor(var1, "image/*", options).getParcelFileDescriptor();
         }
         Uri uri = var1.buildUpon().appendQueryParameter("thumbnail_size", Integer.toString(var2)).build();
         return var0.openAssetFileDescriptor(uri, "r").getParcelFileDescriptor();
      } catch (java.io.FileNotFoundException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   public static boolean a(ContentResolver var0, Uri var1, Uri var2, Uri var3) {
      boolean var4 = true;
      if (VERSION.SDK_INT >= 11) {
         Bundle var5 = new Bundle();
         var5.putString("document_id", b(var1));
         var5.putString("src_uri", b(var2));
         var5.putString("dst_uri", b(var3));
         if (var0.call(var1, "android:moveDocument", null, var5) == null) {
            var4 = false;
         }
      } else {
         ContentValues var6 = new ContentValues(2);
         var6.put("src_uri", b(var2));
         var6.put("dst_uri", b(var3));
         if (var0.update(var1, var6, null, null) != 1) {
            var4 = false;
         }
      }

      return var4;
   }

   public static Uri b(String var0) {
      return new Builder().scheme("content").authority(var0).appendPath("root").build();
   }

   public static Uri b(String var0, String var1) {
      return new Builder().scheme("content").authority(var0).appendPath("document").appendPath(var1).appendPath("children").build();
   }

   public static String b(Uri var0) {
      List var1 = var0.getPathSegments();
      String var2;
      if (var1.size() >= 2 && "document".equals(var1.get(0))) {
         var2 = (String)var1.get(1);
      } else {
         if (var1.size() < 4 || !"tree".equals(var1.get(0)) || !"document".equals(var1.get(2))) {
            throw new IllegalArgumentException("Invalid URI: " + var0);
         }

         var2 = (String)var1.get(3);
      }

      return var2;
   }

   public static boolean b(ContentResolver var0, Uri var1) {
      boolean var2 = true;
      if (VERSION.SDK_INT >= 11) {
         Bundle var3 = new Bundle();
         var3.putString("document_id", b(var1));
         if (var0.call(var1, "android:deleteDocument", null, var3) == null) {
            var2 = false;
         }
      } else if (var0.delete(var1, null, null) != 1) {
         var2 = false;
      }

      return var2;
   }

   public static Uri c(String var0, String var1) {
      return new Builder().scheme("content").authority(var0).appendPath("document").appendPath(var1).build();
   }

   public static String c(ContentResolver param0, Uri param1) {
      String authority = param1.getAuthority();
      StringBuilder path = new StringBuilder(256);
      for (;;) {
         Cursor cursor = null;
         try {
            cursor = param0.query(param1, a, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) break;
            String name = cursor.getString(0);
            String parent = cursor.getString(1);
            if (name != null && name.length() > 0) {
               path.insert(0, File.separatorChar);
               path.insert(1, name);
            }
            if (parent == null) break;
            param1 = c(authority, parent);
         } catch (Throwable failure) {
            break; // Keep the portion already read from the provider.
         } finally {
            if (cursor != null) cursor.close();
         }
      }
      return path.length() > 0 ? path.toString() : File.separator;
   }

   public static String d(ContentResolver param0, Uri param1) {
      String[] columns = {"parent_id"};
      String authority = param1.getAuthority();
      String last = null;
      for (;;) {
         Cursor cursor = null;
         try {
            cursor = param0.query(param1, columns, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) break;
            String parent = cursor.getString(0);
            if (parent == null) break;
            last = parent;
            param1 = c(authority, parent);
         } catch (Throwable failure) {
            break; // The last known parent survives a failed query or URI construction.
         } finally {
            if (cursor != null) cursor.close();
         }
      }
      return last;
   }

   public static boolean d(Uri var0) {
      boolean var2 = false;
      List var3 = var0.getPathSegments();
      boolean var1 = var2;
      if (var3.size() >= 2) {
         var1 = var2;
         if ("document".equals(var3.get(0))) {
            var1 = true;
         }
      }

      return var1;
   }
}
