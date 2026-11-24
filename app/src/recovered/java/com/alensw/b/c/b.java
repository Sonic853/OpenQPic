package com.alensw.b.c;

import android.content.Context;
import android.os.Environment;
import android.os.Build.VERSION;
import java.io.File;
import java.util.Locale;

public class b {
   private b() {}

   public static final File a = Environment.getExternalStorageDirectory();
   public static final String b = a.getPath();

   public static File a() {
      String var0;
      if (VERSION.SDK_INT >= 8) {
         var0 = Environment.DIRECTORY_DOWNLOADS;
      } else {
         var0 = "Download";
      }

      return new File(a, var0);
   }

   public static File a(Context var0) {
      File var1 = a(a, "Android", "data", var0.getPackageName());
      if (!var1.exists()) {
         var1.mkdirs();
      }

      return var1;
   }

   public static File a(Context var0, String var1) {
      return new File(a(var0), var1);
   }

   public static File a(File var0, String... var1) {
      for (String var4 : var1) {
         if (var0 == null) {
            var0 = new File(var4);
         } else if (var4 != null) {
            var0 = new File(var0, var4);
         }
      }

      return var0;
   }

   public static void a(File var0) {
      var0.listFiles(new com.alensw.b.c.c());
      var0.delete();
   }

   public static File b(Context var0) {
      File var1 = new File(a(var0), "cache");
      if (!var1.exists()) {
         var1.mkdirs();
      }

      return var1;
   }

   public static File b(Context var0, String var1) {
      return new File(b(var0), var1);
   }

   public static File c(Context var0) {
      File var1 = new File(b(var0), ".temp");
      if (var1.exists()) {
         if (var1.lastModified() + 10800000L >= System.currentTimeMillis()) {
            return var1;
         }

         a(var1);
      }

      var1.mkdirs();
      return var1;
   }

   public static File c(Context var0, String var1) {
      int var2 = var1.lastIndexOf(File.separatorChar);
      String var3 = var1;
      if (var2 >= 0) {
         var3 = var1;
         if (var2 < var1.length() - 1) {
            var3 = var1.substring(var2 + 1);
         }
      }

      var2 = var3.lastIndexOf(46);
      var1 = var3;
      if (var2 != -1) {
         var1 = var3.substring(0, var2) + var3.substring(var2).toLowerCase(Locale.ENGLISH);
      }

      return new File(c(var0), var1);
   }
}
