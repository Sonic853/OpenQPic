package com.alensw.image;

import android.annotation.SuppressLint;
import com.alensw.io.OpenFileHandle;
import com.alensw.jni.JniUtils;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ExifData extends JniUtils {
   @SuppressLint("SimpleDateFormat")
   public static final SimpleDateFormat a = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
   @SuppressLint("SimpleDateFormat")
   public static final SimpleDateFormat b = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
   private int c = 0;

   static {
      b.setTimeZone(TimeZone.getTimeZone("UTC"));
   }

   public ExifData() {
   }

   private ExifData(int var1) {
      this.c = var1;
   }

   public static int a(OpenFileHandle var0, int var1, boolean var2) {
        try {
            ExifData srcType0195A = a(var0, false);
            if (srcType0195A == null) {
                return -1;
            }
            if (!var2) {
                var1 += srcType0195A.f();
            }
            int i2 = (var1 + 360) % 360;
            boolean zA = srcType0195A.a(i2);
            srcType0195A.a();
            if (!zA) {
                i2 = -1;
            }
            return i2;
        } catch (Throwable th) {
            return -1;
        }
    }

   public static long a(String var0) {
        try {
            long jLastModified = new File(var0).lastModified();
            ExifData srcType0195A = a(var0, true);
            if (srcType0195A == null) {
                return jLastModified;
            }
            long jA = srcType0195A.a(jLastModified);
            srcType0195A.a();
            return jA;
        } catch (Throwable th) {
            return 0L;
        }
    }

   public static ExifData a(OpenFileHandle var0, boolean var1) {
      int var2 = exifOpenFD(var0.c(), var1);
      ExifData var3;
      if (var2 != 0) {
         var3 = new ExifData(var2);
      } else {
         var3 = null;
      }

      return var3;
   }

   public static ExifData a(String var0, boolean var1) {
      OpenFileHandle fd = null;
      try {
         fd = OpenFileHandle.a(new File(var0), var1);
         return a(fd, var1);
      } catch (Throwable failure) {
         return null;
      } finally {
         if (fd != null) fd.h();
      }
   }

   public static Date a(String var0, SimpleDateFormat var1) {
        try {
            return var1.parse(var0, new ParsePosition(0));
        } catch (Throwable th) {
            return null;
        }
    }

   public long a(long var1) {
      long var5 = this.i();
      long var3 = this.h();
      if (var5 != 0L && Math.abs(var5 - var1) <= 10000L) {
         var1 = var5;
      } else if (var3 != 0L && Math.abs(var3 - var1) <= 10000L) {
         var1 = var3;
      } else if (var5 != 0L) {
         var1 = var5;
      } else if (var3 != 0L) {
         var1 = var3;
      }

      return var1;
   }

   public Integer a(int var1, boolean var2) {
      try {
         Object var3 = exifGetValue(this.c, var1, var2);
         if (var3 instanceof Integer) {
            return (Integer)var3;
         }
      } catch (Throwable var4) {
      }

      return null;
   }

   public Date a(int var1, SimpleDateFormat var2) {
      String var3 = this.c(var1, false);
      Date var4;
      if (var3 != null) {
         var4 = a(var3, var2);
      } else {
         var4 = null;
      }

      return var4;
   }

   public void a() {
      if (this.c != 0) {
         exifClose(this.c);
         this.c = 0;
      }
   }

   public void a(PictureMetadata var1, long var2) {
      boolean var5 = true;
      var1.a = exifGetInfo(this.c, 1);
      var1.b = exifGetInfo(this.c, 2);
      var1.c = exifGetInfo(this.c, 3);
      boolean var4;
      if (exifGetInfo(this.c, 10) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var1.h = var4;
      var1.d = exifGetInfo(this.c, 4);
      if (exifGetInfo(this.c, 11) != 0) {
         var4 = var5;
      } else {
         var4 = false;
      }

      var1.i = var4;
      var1.j = this.a(var2);
      var1.k = this.o();
      var1.l = this.c(272, false);
      var1.m = this.c(270, false);
      var1.n = this.j();
      var1.o = "image/jpeg";
   }

   public boolean a(int var1) {
      return exifSetDegrees(this.c, var1);
   }

   public boolean a(OpenFileHandle var1) {
      boolean var2 = false;
      ExifData var3 = a(var1, false);
      if (var3 != null) {
         var2 = exifSaveTo(this.c, var3.c);
         var3.a();
      }

      return var2;
   }

   public Double b(int var1, boolean var2) {
      try {
         Object var3 = exifGetValue(this.c, var1, var2);
         if (var3 instanceof Double) {
            return (Double)var3;
         }
      } catch (Throwable var4) {
      }

      return null;
   }

   public final boolean b() {
      boolean var1;
      if (this.c != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean b(OpenFileHandle var1, boolean var2) {
      if (this.c != 0) {
         this.a();
      }

      this.c = exifOpenFD(var1.c(), var2);
      if (this.c != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public String c(int var1, boolean var2) {
        String strTrim;
        try {
            Object objExifGetValue = exifGetValue(this.c, var1, var2);
            if (objExifGetValue == null) {
                strTrim = null;
            } else {
                strTrim = objExifGetValue instanceof byte[] ? new String((byte[]) objExifGetValue).trim() : objExifGetValue.toString().trim();
            }
            return strTrim;
        } catch (Throwable th) {
            return null;
        }
    }

   public final boolean c() {
      boolean var1;
      if (exifGetInfo(this.c, 11) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final int d() {
      return exifGetInfo(this.c, 3);
   }

   public final long e() {
      int var1 = exifGetInfo(this.c, 1);
      long var2 = exifGetInfo(this.c, 2);
      return var1 | var2 << 32;
   }

   public final int f() {
      return exifGetInfo(this.c, 4);
   }

   @Override
   protected void finalize() {
      if (this.c != 0) {
         this.a();
      }
   }

   public byte[] g() {
      String var1 = this.c(305, false);
      byte[] var2;
      if (var1 != null && var1.startsWith("GIMP")) {
         var2 = null;
      } else {
         var2 = exifGetThumbnail(this.c);
      }

      return var2;
   }

   protected long h() {
      Date var4 = this.a(36867, a);
      Date var3 = var4;
      if (var4 == null) {
         var3 = this.a(306, a);
      }

      var4 = var3;
      if (var3 == null) {
         var4 = this.a(36868, a);
      }

      long var1;
      if (var4 != null) {
         var1 = var4.getTime();
      } else {
         var1 = 0L;
      }

      return var1;
   }

   protected long i() {
      long var3 = 0L;
      String var5 = this.c(29, true);
      long var1;
      if (var5 == null) {
         var1 = var3;
      } else {
         double[] var6 = exifGet3RealValue(this.c, 7, true);
         var1 = var3;
         if (var6 != null) {
            var1 = var3;
            if (!(var6[0] < 0.0)) {
               var1 = var3;
               if (!(var6[0] >= 24.0)) {
                  var1 = var3;
                  if (!(var6[1] < 0.0)) {
                     var1 = var3;
                     if (!(var6[1] >= 24.0)) {
                        var1 = var3;
                        if (!(var6[2] < 0.0)) {
                           var1 = var3;
                           if (!(var6[2] >= 24.0)) {
                              if (var6[0] == 0.0 && var6[1] == 0.0 && var6[2] == 0.0 && "0000:00:00".equals(var5)) {
                                 return var3;
                              }

                              String var8 = String.format("%d:%d:%d", (int)var6[0], (int)var6[1], (int)var6[2]);
                              Date var7 = a(var5 + " " + var8, b);
                              if (var7 != null) {
                                 var1 = var7.getTime();
                              } else {
                                 var1 = 0L;
                              }

                              if (var1 <= 0L) {
                                 var1 = 0L;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   public String j() {
      int var1 = 0;
      String[] var3 = new String[]{this.l(), this.m(), this.k(), this.n()};
      StringBuilder var4 = new StringBuilder(128);

      for (int var2 = var3.length; var1 < var2; var1++) {
         String var5 = var3[var1];
         if (var5 != null) {
            var4.append(var5);
            var4.append(' ');
         }
      }

      String var6;
      if (var4.length() > 0) {
         var6 = var4.toString();
      } else {
         var6 = null;
      }

      return var6;
   }

   @SuppressLint("DefaultLocale")
   public String k() {
      Double var4 = this.b(33434, false);
      Double var3 = var4;
      if (var4 == null) {
         var3 = this.b(37377, false);
         if (var3 == null) {
            Object var8 = null;
            return (String)var8;
         }

         var3 = 1.0 / Math.exp(var3 * Math.log(2.0));
      }

      double var1 = var3;
      String var6;
      if (var1 <= 0.5) {
         var6 = String.format("1/%ds", (int)(1.0 / var1 + 0.5));
      } else {
         String var7;
         if (var1 < 0.01) {
            var7 = "%6.4fs";
         } else {
            var7 = "%5.3fs";
         }

         var6 = String.format(var7, var1);
      }

      return var6;
   }

   @SuppressLint("DefaultLocale")
   public String l() {
      Double var4 = this.b(33437, false);
      Double var3 = var4;
      if (var4 == null) {
         var4 = this.b(37378, false);
         var3 = var4;
         if (var4 == null) {
            var3 = this.b(37381, false);
         }

         if (var3 == null) {
            Object var8 = null;
            return (String)var8;
         }

         var3 = Math.exp(var3 * Math.log(2.0) * 0.5);
      }

      double var1 = var3;
      String var6;
      if (var1 == (int)var1) {
         var6 = String.format("F/%d", (int)var1);
      } else {
         var6 = String.format("F/%3.1f", var1);
      }

      return var6;
   }

   public String m() {
      Double var1 = this.b(37386, false);
      String var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = Integer.toString((int)(var1 + 0.5)) + "mm";
      }

      return var2;
   }

   public String n() {
      Integer var2 = this.a(34855, false);
      String var3;
      if (var2 != null && var2 != 0) {
         Integer var4 = var2;
         if (var2 < 32) {
            var4 = var2 * 200;
         }

         var3 = "ISO-" + var4.toString();
      } else {
         Double var1 = this.b(41493, false);
         if (var1 != null && var1.intValue() != 0) {
            var3 = "ISO-" + var1.toString();
         } else {
            var3 = null;
         }
      }

      return var3;
   }

   public float[] o() {
      double[] var9 = exifGet3RealValue(this.c, 2, true);
      double[] var7 = exifGet3RealValue(this.c, 4, true);
      float[] var13;
      if (var9 != null && var7 != null) {
         String var8 = this.c(1, true);
         String var10 = this.c(3, true);
         double var1 = var9[0] + var9[1] / 60.0 + var9[2] / 3600.0;
         double var5 = var7[0];
         double var3 = var7[1] / 60.0;
         var5 = var7[2] / 3600.0 + (var5 + var3);
         if (var1 == 0.0 && var5 == 0.0 && var8 == null && var10 == null) {
            var13 = null;
         } else {
            if (var8 != null && var8.length() > 0 && var8.charAt(0) == 'S') {
               var1 = -var1;
            }

            var3 = var5;
            if (var10 != null) {
               var3 = var5;
               if (var10.length() > 0) {
                  var3 = var5;
                  if (var10.charAt(0) == 'W') {
                     var3 = -var5;
                  }
               }
            }

            var13 = new float[]{(float)var1, (float)var3};
         }
      } else {
         var13 = null;
      }

      return var13;
   }
}
