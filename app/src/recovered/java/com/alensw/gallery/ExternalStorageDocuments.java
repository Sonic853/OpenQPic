package com.alensw.gallery;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import java.io.File;

public class ExternalStorageDocuments {
   public static String a;
   private static ContentResolver b;
   private static StorageVolumes c;

   protected static int a(Uri var0, String var1, String[] var2) {
      int var3;
      try {
         var3 = b.delete(var0, var1, var2);
      } catch (Throwable var4) {
         Log.e("ExternalStorage", "remove: ", var4);
         var3 = -1;
      }

      return var3;
   }

   protected static Uri a(Uri var0, ContentValues var1) {
      try {
         var0 = b.insert(var0, var1);
      } catch (Throwable var2) {
         Log.e("ExternalStorage", "insert: ", var2);
         var0 = null;
      }

      return var0;
   }

   protected static Uri a(File var0, boolean var1) {
        try {
            Uri parent = b(var0.getParent());
            String mime = var1 ? "vnd.android.document/directory" : "";
            return DocumentsContract.createDocument(b, parent, mime, var0.getName());
        } catch (Throwable th) {
            Log.e("ExternalStorage", "create file: ", th);
            return null;
        }
    }

   public static Uri a(String var0) {
      Uri var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = b(var0);
      } else if (VERSION.SDK_INT >= 11) {
         var1 = c(var0);
      } else {
         var1 = null;
      }

      return var1;
   }

   public static ParcelFileDescriptor a(File var0, String var1) {
      Uri var3;
      if (var0.exists()) {
         var3 = a(var0.getPath());
      } else {
         var3 = b(var0);
      }

      if (var3 != null) {
         try {
            return b.openFileDescriptor(var3, var1);
         } catch (Throwable var2) {
            Log.e("ExternalStorage", "open file:", var2);
         }
      }

      return null;
   }

   public static void a(Context var0, StorageVolumes var1) {
      b = var0.getContentResolver();
      c = var1;
   }

   public static boolean a(File var0) {
      boolean var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = d(var0);
      } else if (VERSION.SDK_INT >= 11) {
         var1 = g(var0);
      } else {
         var1 = false;
      }

      return var1;
   }

   public static Uri b(File var0) {
      Uri var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = e(var0);
      } else if (VERSION.SDK_INT >= 11) {
         var1 = h(var0);
      } else {
         var1 = null;
      }

      return var1;
   }

   protected static Uri b(String var0) {
        String[] strArrE;
        try {
            String[] strArrE2 = c.e(var0);
            if (strArrE2 == null) {
                if (var0.startsWith("/mnt/")) {
                    var0 = "/storage/" + var0.substring(5);
                } else if (!var0.startsWith("/storage/")) {
                    var0 = "/storage" + var0;
                }
                strArrE = c.e(var0);
            } else {
                strArrE = strArrE2;
            }
            if (strArrE == null || strArrE.length < 2) {
                throw new RuntimeException("invalid path: " + var0);
            }
            String str2 = strArrE[0] != null ? strArrE[0] : a;
            if (str2 == null) {
                throw new RuntimeException("invalid uuid: " + var0);
            }
            Uri uriBuildDocumentUriUsingTree = DocumentsContract.buildDocumentUriUsingTree(DocumentsContract.buildTreeDocumentUri("com.android.externalstorage.documents", str2 + ':'), str2 + ':' + strArrE[1]);
            Log.d("ExternalStorage", "get uri: " + uriBuildDocumentUriUsingTree);
            return uriBuildDocumentUriUsingTree;
        } catch (Throwable th) {
            Log.e("ExternalStorage", "get uri: ", th);
            return null;
        }
    }

   public static boolean b(File var0, String var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = c(var0, var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   protected static Uri c(String path) {
      Uri files = Files.getContentUri("external");
      Cursor cursor = null;
      try {
         cursor = b.query(files, new String[]{"_id"}, "_data=?", new String[]{path}, null);
         return cursor != null && cursor.moveToFirst() ? ContentUris.withAppendedId(files, cursor.getInt(0)) : null;
      } catch (Throwable error) {
         Log.e("ExternalStorage", "get uri: " + error);
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   public static boolean c(File var0) {
      boolean var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = f(var0);
      } else if (VERSION.SDK_INT >= 11) {
         var1 = i(var0);
      } else {
         var1 = false;
      }

      return var1;
   }

   protected static boolean c(File var0, String var1) {
        try {
            Uri document = b(var0.getPath());
            return DocumentsContract.renameDocument(b, document, var1) != null;
        } catch (Throwable th) {
            Log.e("ExternalStorage", "rename file: ", th);
            return false;
        }
    }

   protected static boolean d(File var0) {
      boolean var1 = true;
      if (a(var0, true) == null) {
         var1 = false;
      }

      return var1;
   }

   protected static Uri e(File var0) {
      return a(var0, false);
   }

   protected static boolean f(File var0) {
      boolean var1 = false;

      boolean var2;
      try {
         if (var0.isDirectory()) {
            return var1;
         }

         Uri var4 = b(var0.getPath());
         var2 = DocumentsContract.deleteDocument(b, var4);
      } catch (Throwable var3) {
         Log.e("ExternalStorage", "delete file: ", var3);
         return var1;
      }

      return var2;
   }

   protected static boolean g(File var0) {
      Uri var2 = Files.getContentUri("external");
      ContentValues var1 = new ContentValues();
      var1.put("_data", var0.getPath());
      a(var2, var1);
      var1.put("_data", new File(var0, "!qpicfake.jpg").getPath());
      Uri var3 = a(Media.EXTERNAL_CONTENT_URI, var1);
      if (var3 != null) {
         a(var3, null, null);
      }

      return var0.exists();
   }

   protected static Uri h(File var0) {
      String var1 = var0.getPath();
      Uri var3 = c(var1);
      if (var3 == null) {
         Uri var2 = Files.getContentUri("external");
         ContentValues var4 = new ContentValues();
         var4.put("_data", var1);
         var3 = a(var2, var4);
      }

      return var3;
   }

   protected static boolean i(File var0) {
      boolean var1 = true;
      Uri var3 = Files.getContentUri("external");
      String var4 = var0.getPath();
      String[] var5 = new String[]{var4};
      a(var3, "_data=?", var5);
      if (var0.exists()) {
         ContentValues var2 = new ContentValues();
         var2.put("_data", var4);
         a(Media.EXTERNAL_CONTENT_URI, var2);
         a(var3, "_data=?", var5);
         if (var0.exists()) {
            var1 = false;
         }
      }

      return var1;
   }
}
