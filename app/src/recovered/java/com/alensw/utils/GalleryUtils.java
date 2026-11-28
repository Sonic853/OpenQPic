package com.alensw.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.view.Display;
import android.view.WindowManager;
import com.alensw.gallery.ExternalStorageDocuments;
import com.alensw.image.ExifData;
import com.alensw.io.OpenFileHandle;
import com.alensw.io.StoragePaths;
import com.alensw.jni.JniUtils;
import com.alensw.svg.SvgPicture;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class GalleryUtils {
   public static final char[] a;
   private static final Random b = new Random(System.nanoTime());
   private static final SimpleDateFormat c = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
   private static Field d;
   private static final Pattern e;
   private static Handler f;

   static {
      c.setTimeZone(TimeZone.getTimeZone("UTC"));

      try {
         d = ArrayList.class.getDeclaredField("array");
         d.setAccessible(true);
      } catch (Throwable var1) {
      }

      a = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      e = Pattern.compile("[\\\\/:*?\"<>|]");
   }

   public static int a(int param0) {
        int iNextInt;
        synchronized (b) {
            iNextInt = b.nextInt(param0);
        }
        return iNextInt;
    }

   public static int a(PackageInfo var0, byte[] var1, byte[] var2) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            for (android.content.pm.Signature signature : var0.signatures) {
                messageDigest.update(signature.toByteArray());
                byte[] bArrDigest = messageDigest.digest();
                if (Arrays.equals(bArrDigest, var1)) {
                    return 0;
                }
                if (Arrays.equals(bArrDigest, var2)) {
                    return 1;
                }
                messageDigest.reset();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return -1;
    }

   public static int a(String var0, int var1) {
      int var2;
      try {
         var2 = Integer.valueOf(var0);
      } catch (Throwable var3) {
         return var1;
      }

      return var2;
   }

   public static int a(String var0, int var1, boolean var2) {
        try {
            File file = new File(var0);
            long jLastModified = file.lastModified();
            OpenFileHandle srcType0150D = d(file);
            if (srcType0150D == null) {
                return -1;
            }
            int iA = ExifData.a(srcType0150D, var1, var2);
            srcType0150D.h();
            if (iA != -1) {
                file.setLastModified(jLastModified);
            }
            return iA;
        } catch (Throwable th) {
            return -1;
        }
    }

   public static int a(List var0, Object var1, Comparator var2) {
      if (VERSION.SDK_INT >= 9) {
         Object[] var4;
         try {
            var4 = (Object[])d.get(var0);
         } catch (Throwable var7) {
            return Collections.binarySearch(var0, var1, var2);
         }

         if (var4 == null) {
            try {
               NoSuchFieldException var8 = new NoSuchFieldException();
               throw var8;
            } catch (Throwable var5) {
            }
         } else {
            try {
               return Arrays.binarySearch(var4, 0, var0.size(), var1, var2);
            } catch (Throwable var6) {
            }
         }
      }

      return Collections.binarySearch(var0, var1, var2);
   }

   public static long a(File param0, char param1, long param2) {
      String path = param0.getPath();
      long date = param2 == 0L ? param0.lastModified() : param2;
      if (param1 == 'I') {
         ExifData exif = ExifData.a(path, true);
         if (exif != null) {
            date = exif.a(date);
            exif.a();
         }
         return date;
      }
      if (VERSION.SDK_INT < 10) return date;
      android.media.MediaMetadataRetriever retriever = new android.media.MediaMetadataRetriever();
      try {
         retriever.setDataSource(path);
         String value = retriever.extractMetadata(5);
         if (value != null) {
            long time = c.parse(value).getTime();
            if (time > 0L) date = time;
         }
      } catch (Throwable ignored) {
         // Keep the filesystem timestamp on absent or unreadable metadata.
      } finally {
         try { retriever.release(); } catch (Throwable ignored) {}
      }
      return date;
   }

   public static long a(String var0, long var1) {
      long var3;
      try {
         var3 = Long.valueOf(var0);
      } catch (Throwable var5) {
         return var1;
      }

      return var3;
   }

   public static Point a(Context var0) {
      Display var2 = ((WindowManager)var0.getSystemService("window")).getDefaultDisplay();
      Point var1 = new Point();

      try {
         Display.class.getMethod("getRealSize", Point.class).invoke(var2, var1);
      } catch (Throwable var3) {
         var1.set(var2.getWidth(), var2.getHeight());
      }

      return var1;
   }

   public static Drawable a(Resources var0, int var1, int var2) {
      return a(var0, var1, var2, -1);
   }

   public static Drawable a(Resources var0, int var1, int var2, int var3) {
        try {
            return SvgPicture.a(var0, var1, var2, var3);
        } catch (Throwable th) {
            try {
                return var0.getDrawable(var1);
            } catch (Throwable th2) {
                return null;
            }
        }
    }

   public static Drawable a(Resources var0, String var1) {
        try {
            return var0.getDrawable(var0.getIdentifier(var1, "drawable", "android"));
        } catch (Throwable th) {
            return null;
        }
    }

   public static File a(File param0, String param1) {
        if (param0.exists()) {
            File parentFile = param0.getParentFile();
            String name = param0.getName();
            int iLastIndexOf = name.lastIndexOf(46);
            if (iLastIndexOf > 0) {
                if (param1 == null) {
                    param1 = name.substring(iLastIndexOf);
                }
                name = name.substring(0, iLastIndexOf);
            }
            synchronized (GalleryUtils.class) {
                int i = 1;
                while (true) {
                    int i2 = i;
                    param0 = new File(parentFile, name + (i2 < 10 ? "~0" : "~") + Integer.toString(i2) + param1);
                    if (!param0.exists()) {
                        break;
                    }
                    i = i2 + 1;
                }
            }
        }
        return param0;
    }

   @SuppressLint("DefaultLocale")
   public static String a(long var0) {
      int var4 = (int)((500L + var0) / 1000L);
      int var3 = var4 % 60;
      int var2 = var4 / 60 % 60;
      var4 /= 3600;
      String var5;
      if (var4 > 0) {
         var5 = String.format("%d:%02d:%02d", var4, var2, var3);
      } else {
         var5 = String.format("%02d:%02d", var2, var3);
      }

      return var5;
   }

   public static String a(Resources var0, String var1, String var2) {
        try {
            return var0.getString(var0.getIdentifier(var1, "string", "android"));
        } catch (Throwable th) {
            return var2;
        }
    }

   public static String a(FileInputStream var0, String var1) {
        try {
            FileChannel channel = var0.getChannel();
            long size = channel.size();
            java.nio.MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0L, size);
            MessageDigest messageDigest = MessageDigest.getInstance(var1);
            byte[] bArr = new byte[8192];
            long j = size / 8192;
            int i = (int) (size - (j * 8192));
            for (long j2 = 0; j2 < j; j2++) {
                map.get(bArr);
                messageDigest.update(bArr);
            }
            if (i > 0) {
                map.get(bArr, 0, i);
                messageDigest.update(bArr, 0, i);
            }
            return a(messageDigest.digest());
        } catch (Throwable th) {
            return "";
        }
    }

   public static String a(String var0, char var1) {
      int var2 = 0;
      int var4 = var0.length();

      int var3;
      while (true) {
         var3 = var4;
         if (var2 >= var4) {
            break;
         }

         var3 = var4;
         if (var0.charAt(var2) != var1) {
            break;
         }

         var2++;
      }

      while (var2 < var3 && var0.charAt(var3 - 1) == var1) {
         var3--;
      }

      if (var2 < var3) {
         var0 = var0.substring(var2, var3);
      } else {
         var0 = "";
      }

      return var0;
   }

   public static String a(String var0, double var1, double var3) {
      return String.format(Locale.ENGLISH, var0, var1, var3);
   }

   public static String a(String var0, boolean var1) {
      int var2;
      if (var0 != null) {
         var2 = var0.lastIndexOf(46);
      } else {
         var2 = -1;
      }

      if (var2 == -1) {
         var0 = "";
      } else {
         String var3 = var0.substring(var2 + 1);
         var0 = var3;
         if (var1) {
            var0 = var3.toLowerCase(Locale.ENGLISH);
         }
      }

      return var0;
   }

   public static String a(byte[] var0) {
      char[] var3 = new char[var0.length * 2];

      for (int var1 = 0; var1 < var0.length; var1++) {
         int var2 = var0[var1] & 255;
         var3[var1 * 2] = a[var2 >>> 4];
         var3[var1 * 2 + 1] = a[var2 & 15];
      }

      return new String(var3);
   }

   public static void a(Context var0, Throwable var1) {
        Throwable cause = var1.getCause();
        if (cause != null) {
            var1.printStackTrace();
            var1 = cause;
        }
        var1.printStackTrace();
        String message = var1.getMessage();
        if (message == null || message.length() == 0) {
            message = var1.getClass().getName();
        }
        c(var0, message);
    }

   public static void a(Editor var0) {
      if (VERSION.SDK_INT >= 9) {
         var0.apply();
      } else {
         var0.commit();
      }
   }

   public static void a(Canvas var0, RectF var1, float var2, Paint var3) {
      var0.drawRect(var1.left, var1.top, var1.right, var1.top + var2, var3);
      var0.drawRect(var1.left, var1.bottom - var2, var1.right, var1.bottom, var3);
      var0.drawRect(var1.left, var1.top + var2, var1.left + var2, var1.bottom - var2, var3);
      var0.drawRect(var1.right - var2, var1.top + var2, var1.right, var1.bottom - var2, var3);
   }

   public static boolean a(Context var0, String var1) {
        try {
            return var0.getPackageManager().getPackageInfo(var1, 0) != null;
        } catch (Throwable th) {
            return false;
        }
    }

   public static boolean a(OpenFileHandle var0, File var1) {
      boolean var3 = false;

      label53: {
         OpenFileHandle var4;
         try {
            var4 = d(var1);
         } catch (Throwable var8) {
            break label53;
         }

         boolean var2;
         if (var0 != null && var4 != null) {
            try {
               var2 = JniUtils.fuCopyFD(var0.c(), var4.c());
            } catch (Throwable var7) {
               break label53;
            }
         } else {
            var2 = false;
         }

         if (var4 != null) {
            try {
               var4.h();
            } catch (Throwable var6) {
               break label53;
            }
         }

         if (!var2) {
            return var3;
         }

         try {
            var1.setLastModified(var0.f());
         } catch (Throwable var5) {
         }
      }

      boolean var9 = var3;
      if (var0.e() == var1.length()) {
         var9 = var3;
         if (var1.length() > 0L) {
            var9 = true;
         }
      }

      return var9;
   }

   public static boolean a(File var0) {
      boolean var1;
      try {
         var1 = var0.mkdir();
      } catch (Throwable var3) {
         return ExternalStorageDocuments.a(var0);
      }

      return var1 ? true : ExternalStorageDocuments.a(var0);
   }

   public static boolean a(File var0, File var1) {
        try {
            OpenFileHandle srcType0150A = OpenFileHandle.a(var0, true);
            boolean zA = a(srcType0150A, var1);
            if (srcType0150A == null) {
                return zA;
            }
            srcType0150A.h();
            return zA;
        } catch (Throwable th) {
            return false;
        }
    }

   public static boolean a(File var0, boolean var1) {
      File var2 = var0;
      if (var1) {
         var1 = b(new File(var0, ".nomedia"));
      } else {
         while (true) {
            if (var2 == null) {
               var1 = true;
               break;
            }

            var0 = new File(var2, ".nomedia");
            var2 = var2.getParentFile();
            if (!c(var0)) {
               var1 = false;
               break;
            }
         }
      }

      return var1;
   }

   public static boolean a(String var0) {
      boolean var5 = false;
      boolean var4;
      if (var0 == null) {
         var4 = var5;
      } else {
         int var2 = var0.length();
         var4 = var5;
         if (var2 != 0) {
            int var1 = 0;

            while (true) {
               if (var1 >= var2) {
                  var4 = true;
                  break;
               }

               char var3 = var0.charAt(var1);
               if ((var3 < '0' || var3 > '9') && (var3 < 'a' || var3 > 'f')) {
                  var4 = var5;
                  if (var3 < 'A') {
                     break;
                  }

                  var4 = var5;
                  if (var3 > 'F') {
                     break;
                  }
               }

               var1++;
            }
         }
      }

      return var4;
   }

   public static boolean a(String var0, String var1) {
      boolean var3 = false;
      boolean var2;
      if (var0 == var1) {
         var2 = true;
      } else {
         var2 = var3;
         if (var0 != null) {
            var2 = var3;
            if (var1 != null) {
               var2 = var3;
               if (var0.length() == var1.length()) {
                  var2 = var0.equals(var1);
               }
            }
         }
      }

      return var2;
   }

   public static boolean a(ArrayList var0, Comparator var1) {
      Object[] var3;
      try {
         var3 = (Object[])d.get(var0);
      } catch (Throwable var6) {
         return a((List)var0, var1);
      }

      boolean var2;
      if (var3 == null) {
         try {
            NoSuchFieldException var7 = new NoSuchFieldException();
            throw var7;
         } catch (Throwable var5) {
            var2 = a((List)var0, var1);
         }
      } else {
         try {
            Arrays.sort(var3, 0, var0.size(), var1);
         } catch (Throwable var4) {
            var2 = a((List)var0, var1);
            return var2;
         }

         var2 = true;
      }

      return var2;
   }

   public static boolean a(List var0, Comparator var1) {
      try {
         Collections.sort(var0, var1);
      } catch (Throwable var3) {
         return false;
      }

      return true;
   }

   public static int b(Context var0) {
      WindowManager var2 = (WindowManager)var0.getSystemService("window");
      Configuration var3 = var0.getResources().getConfiguration();
      int var1;
      if (VERSION.SDK_INT >= 8) {
         var1 = var2.getDefaultDisplay().getRotation();
      } else {
         var1 = 0;
      }

      byte var4;
      if ((var1 != 0 && var1 != 2 || var3.orientation != 2) && (var1 != 1 && var1 != 3 || var3.orientation != 1)) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      return var4;
   }

   public static File b(Context var0, String var1) {
      File var2 = var0.getFileStreamPath(var1);
      if (!var2.exists()) {
         File var3 = StoragePaths.a(var0, var1);
         if (var3.exists() && a(var3, var2)) {
            c(var3);
         }
      }

      return var2;
   }

   @SuppressLint("DefaultLocale")
   public static String b(int var0) {
      float var2 = var0;
      String var3 = "P";
      float var1 = var2;
      if (var2 > 900.0F) {
         var3 = "KP";
         var1 = var2 / 1000.0F;
      }

      var2 = var1;
      if (var1 > 900.0F) {
         var3 = "MP";
         var2 = var1 / 1000.0F;
      }

      String var4;
      if (var2 > 900.0F) {
         var2 /= 1000.0F;
         var4 = "GP";
      } else {
         var4 = var3;
      }

      if (var2 < 1.0F) {
         var3 = String.format("%.2f", var2);
      } else if (var2 < 10.0F) {
         var3 = String.format("%.1f", var2);
      } else {
         var3 = String.format("%.0f", var2);
      }

      return var3 + var4;
   }

   public static String b(String var0) {
      if (var0 != null) {
         var0 = var0.substring(var0.lastIndexOf(File.separatorChar) + 1);
      } else {
         var0 = "";
      }

      return var0;
   }

   public static String b(String var0, String var1) {
      int var2;
      if (var0 != null) {
         var2 = var0.length();
      } else {
         var2 = 0;
      }

      if (var2 > 0 && var0.charAt(var2 - 1) != File.separatorChar) {
         var0 = var0 + File.separatorChar + var1;
      } else if (var0 == null) {
         var0 = File.separator + var1;
      } else {
         var0 = var0 + var1;
      }

      return var0;
   }

   public static boolean b(File var0) {
      boolean var2 = true;
      boolean var1 = var2;

      label22: {
         try {
            if (var0.exists()) {
               return var1;
            }

            var1 = var0.createNewFile();
         } catch (Throwable var4) {
            break label22;
         }

         if (var1) {
            return var2;
         }
      }

      var1 = var2;
      if (ExternalStorageDocuments.b(var0) == null) {
         var1 = false;
      }

      return var1;
   }

   public static boolean b(File var0, String var1) {
      boolean var2;
      try {
         File var4 = var0.getParentFile();
         File var3 = new File(var4, var1);
         if (var3.exists()) {
            return false;
         }

         var2 = var0.renameTo(var3);
      } catch (Throwable var5) {
         return ExternalStorageDocuments.b(var0, var1);
      }

      return var2 ? true : ExternalStorageDocuments.b(var0, var1);
   }

   public static String c(Context param0) {
        String str;
        String string = android.provider.Settings.Secure.getString(param0.getContentResolver(), "android_id");
        if (string != null && !"9774d56d682e549c".equals(string)) {
            return "a:" + string;
        }
        synchronized (GalleryUtils.class) {
            android.content.SharedPreferences sharedPreferences = param0.getSharedPreferences("device_id.xml", 0);
            String string2 = sharedPreferences.getString("device_id", "");
            if (string2 == null || string2.length() == 0) {
                string2 = java.util.UUID.randomUUID().toString();
                a(sharedPreferences.edit().putString("device_id", string2));
            }
            str = "r:" + string2;
        }
        return str;
    }

   public static String c(String var0) {
      String var2 = b(var0);
      int var1 = var2.lastIndexOf(46);
      var0 = var2;
      if (var1 != -1) {
         var0 = var2.substring(0, var1);
      }

      return var0;
   }

   public static void c(Context var0, String var1) {
      ShowToast var4 = new ShowToast(var0, var1);
      if (var0 instanceof Activity) {
         ((Activity)var0).runOnUiThread(var4);
      } else {
         try {
            if (f == null) {
               Handler var3 = new Handler(Looper.getMainLooper());
               f = var3;
            }

            f.post(var4);
         } catch (Throwable var2) {
         }
      }
   }

   public static boolean c(File var0) {
      boolean var1;
      try {
         if (var0.delete()) {
            return true;
         }

         var1 = var0.exists();
      } catch (Throwable var3) {
         return ExternalStorageDocuments.c(var0);
      }

      return !var1 ? true : ExternalStorageDocuments.c(var0);
   }

   public static boolean c(String var0, String var1) {
      boolean var5 = false;
      boolean var4 = var5;
      if (var0 != null) {
         if (var1 == null) {
            var4 = var5;
         } else {
            int var2 = var0.length();
            int var3 = var1.length();
            var4 = var5;
            if (var2 <= var3) {
               var4 = var5;
               if (var1.startsWith(var0)) {
                  if (var2 != var3 && var1.charAt(var2) != File.separatorChar) {
                     return var5;
                  }

                  var4 = true;
               }
            }
         }
      }

      return var4;
   }

   public static int d(Context var0) {
      if (VERSION.SDK_INT >= 14) {
         try {
            TypedArray var3 = var0.obtainStyledAttributes(new int[]{16843534});
            int var1 = var3.getResourceId(0, com.alensw.PicFolder.R.drawable.item_background);
            var3.recycle();
            return var1;
         } catch (Throwable var2) {
         }
      }

      return com.alensw.PicFolder.R.drawable.item_background;
   }

   public static OpenFileHandle d(File var0) {
      label19: {
         OpenFileHandle var1;
         try {
            var1 = OpenFileHandle.a(var0, false);
         } catch (Throwable var2) {
            break label19;
         }

         if (var1 != null) {
            return var1;
         }
      }

      ParcelFileDescriptor var4 = ExternalStorageDocuments.a(var0, "rw");
      OpenFileHandle var3;
      if (var4 != null) {
         var3 = new OpenFileHandle(Uri.fromFile(var0), var4);
      } else {
         var3 = null;
      }

      return var3;
   }

   public static String d(String var0) {
      String var2 = var0;
      if (var0 != null) {
         int var1 = var0.lastIndexOf(File.separatorChar);
         var2 = var0;
         if (var1 >= 0) {
            var2 = var0.substring(0, var1);
         }
      }

      return var2;
   }

   public static Drawable e(Context var0) {
      Drawable var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = var0.getTheme().getDrawable(d(var0));
      } else {
         var1 = var0.getResources().getDrawable(d(var0));
      }

      return var1;
   }

   public static FileOutputStream e(File var0) {
      label19: {
         FileOutputStream var1;
         try {
            var1 = new FileOutputStream(var0);
         } catch (Throwable var2) {
            break label19;
         }

         if (var1 != null) {
            return var1;
         }
      }

      ParcelFileDescriptor var3 = ExternalStorageDocuments.a(var0, "rw");
      AutoCloseOutputStream var4;
      if (var3 != null) {
         var4 = new AutoCloseOutputStream(var3);
      } else {
         var4 = null;
      }

      return var4;
   }

   public static String e(String var0) {
      int var1;
      if (var0 != null) {
         var1 = var0.length();
      } else {
         var1 = 0;
      }

      if (var1 > 0 && var0.charAt(var1 - 1) != File.separatorChar) {
         var0 = var0 + File.separatorChar;
      }

      String var2 = var0;
      if (var0 == null) {
         var2 = File.separator;
      }

      return var2;
   }

   public static boolean f(String var0) {
      boolean var1;
      if (var0 != null && var0.contains("/.")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static String g(String var0) {
      if (var0 == null) {
         var0 = "";
      } else {
         var0 = e.matcher(var0).replaceAll("_");
      }

      return var0;
   }

   public static boolean h(String var0) {
      boolean var1;
      if (var0 != null) {
         var1 = JniUtils.fuHasNoMedia(var0);
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean i(String var0) {
      boolean var1;
      if (var0 != null && c(new File(var0))) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
