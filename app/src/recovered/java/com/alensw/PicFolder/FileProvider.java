package com.alensw.PicFolder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class FileProvider extends ContentProvider {
   public static final String[] a = new String[]{"_data", "_display_name", "title", "mime_type", "_size", "datetaken", "latitude", "longitude", "orientation"};
   private static final Uri b = Uri.parse("content://com.alensw.PicFolder.FileProvider");

   public static Uri a(String var0) {
      Uri var1;
      if (var0 != null && var0.length() != 0) {
         var1 = com.alensw.b.j.a.c("com.alensw.PicFolder.FileProvider", var0);
      } else {
         var1 = Uri.EMPTY;
      }

      return var1;
   }

   public static String a(Uri uri) {
        try {
            return com.alensw.b.j.a.b(uri);
        } catch (Throwable th) {
            return null;
        }
    }

   @Override
   public int delete(Uri var1, String var2, String[] var3) {
      return 0;
   }

   @Override
   public String getType(Uri var1) {
      String var2 = var1.getPath();
      String var3;
      if (var2 == null) {
         var3 = "*/*";
      } else {
         var3 = com.alensw.b.f.c.b(var2);
      }

      return var3;
   }

   @Override
   public Uri insert(Uri var1, ContentValues var2) {
      return null;
   }

   @Override
   public boolean onCreate() {
      return true;
   }

   @Override
   public ParcelFileDescriptor openFile(Uri uri, String str) {
        try {
            return com.alensw.b.c.f.b(new File(a(uri)), true);
        } catch (Throwable th) {
            return null;
        }
    }

   @Override
   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      String[] var16 = var2;
      if (var2 == null) {
         var16 = a;
      }

      MatrixCursor var15 = new MatrixCursor(var16);
      var5 = a(var1);
      if (var5 != null) {
         Object[] var12 = new Object[var16.length];
         char var6 = com.alensw.b.f.c.a(var5);
         long var10 = new File(var5).lastModified();
         com.alensw.b.h.c var17 = com.alensw.b.h.c.a(var5, true);
         long var8;
         if (var17 != null) {
            var8 = var17.e();
         } else {
            var8 = 0L;
         }

         float[] var14;
         if (var17 != null) {
            var14 = var17.o();
         } else {
            var14 = null;
         }

         for (int var7 = 0; var7 < var16.length; var7++) {
            String var13 = var16[var7];
            if ("_data".equals(var13)) {
               var12[var7] = var5;
            } else if ("_display_name".equals(var13)) {
               var12[var7] = com.alensw.b.l.b.b(var5);
            } else if ("title".equals(var13)) {
               var12[var7] = com.alensw.b.l.b.c(var5);
            } else if ("mime_type".equals(var13)) {
               var12[var7] = com.alensw.b.f.c.a(var5, var6);
            } else if ("_size".equals(var13)) {
               var12[var7] = new File(var5).length();
            } else if (var6 == 'I') {
               if ("datetaken".equals(var13) && var17 != null) {
                  var12[var7] = var17.a(var10);
               } else if ("latitude".equals(var13) && var14 != null) {
                  var12[var7] = (double)var14[0];
               } else if ("longitude".equals(var13) && var14 != null) {
                  var12[var7] = (double)var14[1];
               } else if ("orientation".equals(var13) && var17 != null) {
                  var12[var7] = var17.f();
               } else if ("width".equals(var13)) {
                  var12[var7] = (int)(2147483647L & var8);
               } else if ("height".equals(var13)) {
                  var12[var7] = (int)(var8 >> 32 & 2147483647L);
               }
            }
         }

         if (var17 != null) {
            var17.a();
         }

         var15.addRow(var12);
      }

      return var15;
   }

   @Override
   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      return 0;
   }
}
