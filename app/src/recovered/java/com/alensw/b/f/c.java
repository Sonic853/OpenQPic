package com.alensw.b.f;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;
import java.lang.reflect.Method;
import java.util.HashMap;

public class c {
   public static final String[][] a = new String[][]{
      {"jpg", "jpeg", "avif", "png", "apng", "gif", "webp", "heic", "heif"},
      {"bmp"},
      {"3gp", "3gpp", "webm", "avi", "mp4", "mkv", "mov", "m4v", "mpeg"},
      {"asf", "divx", "flv", "k3g", "mpg", "m2ts", "mts", "rm", "rmvb", "skm", "ts", "wmv", "3g2"}
   };
   private static final String[][] b = new String[][]{
      {"asf", "x-ms-asf"},
      {"bmp", "x-ms-bmp"},
      {"jpg", "jpeg"},
      {"mkv", "x-matroska"},
      {"mov", "mp4"},
      {"wbmp", "vnd.wap.wbmp"},
      {"wmv", "x-ms-wmv"}
   };
   private static final HashMap c = new HashMap(64);
   private static final HashMap d = new HashMap(16);
   private static boolean e;
   private static Object f;
   private static Method g;

   static {
      for (int var1 = 0; var1 < a.length; var1++) {
         for (String var5 : a[var1]) {
            HashMap var6 = c;
            char var0;
            if (var1 < 2) {
               var0 = 'I';
            } else {
               var0 = 'V';
            }

            var6.put(var5, var0);
         }
      }

      for (String[] var9 : b) {
         d.put(var9[0], var9[1]);
      }
   }

   public static char a(String var0) {
      var0 = com.alensw.b.l.b.a(var0, true);
      Character var3 = (Character)c.get(var0);
      char var1;
      if (var3 != null) {
         var1 = var3;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public static String a(String var0, char var1) {
      String var4 = com.alensw.b.l.b.a(var0, true);
      if ("apng".equals(var4)) return "image/apng";
      if ("webm".equals(var4)) return "video/webm";
      if ("weba".equals(var4)) return "audio/webm";
      char var2 = var1;
      if (var1 == 0) {
         Character var5 = (Character)c.get(var4);
         var2 = var1;
         if (var5 != null) {
            var2 = var5;
         }
      }

      var0 = (String)d.get(var4);
      if (var0 == null || var2 == 0) {
         String var3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var4);
         var0 = var3;
         if (var3 == null) {
            if (var2 == 'V') {
               var0 = "video/" + var4;
            } else {
               var0 = "image/" + var4;
            }
         }
      } else if (var2 == 'V') {
         var0 = "video/" + var0;
      } else {
         var0 = "image/" + var0;
      }

      return var0;
   }

   public static String a(String var0, String var1) {
      String var2 = var0;
      if (var0 != null) {
         if (var1 == null) {
            var2 = var0;
         } else {
            var1 = var1.substring(var1.indexOf(47) + 1);
            var2 = var0;
            if (!"*".equals(var1)) {
               var2 = var0 + "." + var1;
            }
         }
      }

      return var2;
   }

   public static String b(String var0) {
      return a(var0, '\u0000');
   }

   /** Legacy providers sometimes report the more general Matroska MIME for WebM. */
   public static String webmMimeType(String name, String mime) {
      String type = Intent.normalizeMimeType(mime);
      if ("audio/webm".equals(type) || "video/webm".equals(type)) return type;
      String extension = com.alensw.b.l.b.a(name, true);
      boolean audioFile = "weba".equals(extension);
      if (!audioFile && !"webm".equals(extension)) return mime;
      if ("audio/x-matroska".equals(type) || "audio/*".equals(type)) {
         return "audio/webm";
      }
      if (!audioFile && ("video/x-matroska".equals(type) || "video/*".equals(type))) {
         return "video/webm";
      }
      return genericMimeType(type) ? (audioFile ? "audio/webm" : "video/webm") : mime;
   }

   public static String webmMimeType(ContentResolver resolver, Uri uri, String mime) {
      String type = Intent.normalizeMimeType(mime);
      if ("content".equals(uri.getScheme()) && (genericMimeType(type) || "video/x-matroska".equals(type)
              || "audio/x-matroska".equals(type) || "video/*".equals(type) || "audio/*".equals(type))) {
         try (Cursor cursor = resolver.query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst() && !cursor.isNull(0)) {
               return webmMimeType(cursor.getString(0), mime);
            }
         } catch (RuntimeException unavailable) {
            // The URI and supplied MIME still work if the provider has no display name.
         }
      }
      return webmMimeType(uri.getLastPathSegment(), mime);
   }

   private static boolean genericMimeType(String type) {
      return type == null || type.isEmpty() || "*/*".equals(type)
              || "application/octet-stream".equals(type) || "binary/octet-stream".equals(type);
   }

   public static boolean c(String var0) {
      boolean var3 = true;
      int var1 = var0.length();
      if (var1 >= 10 && var0.regionMatches(true, var1 - 4, ".jpg", 0, 4)) {
         if (var0.regionMatches(true, 0, "AlbumArt_{", 0, 10)) {
            return var3;
         }

         if (var0.regionMatches(true, 0, "AlbumArt.", 0, 9)) {
            return var3;
         }

         if (var1 == 17 && var0.regionMatches(true, 0, "AlbumArtSmall", 0, 13)) {
            return var3;
         }

         if (var1 == 10 && var0.regionMatches(true, 0, "Folder", 0, 6)) {
            return var3;
         }
      }

      return false;
   }

   public static boolean d(String param0) {
        boolean z;
        synchronized (com.alensw.b.f.c.class) {
            if (!e && g == null) {
                try {
                    Class<?> cls = Class.forName("android.media.MediaFile");
                    f = cls.newInstance();
                    g = cls.getMethod("getFileType", String.class);
                } catch (Throwable th) {
                    e = true;
                }
            }
        }
        try {
            z = g.invoke(f, new StringBuilder().append("0.").append(param0).toString()) != null;
        } catch (Throwable th2) {
            z = false;
        }
        if (z || com.alensw.PicFolder.QuickApp.j != 2) {
            return z;
        }
        return "asf".equals(param0) || "divx".equals(param0) || "wmv".equals(param0);
    }
}
