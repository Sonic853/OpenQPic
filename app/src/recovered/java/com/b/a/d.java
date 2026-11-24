package com.b.a;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class d {
   private d() {}

   static float a = 72.0F;
   private static final SAXParserFactory b = SAXParserFactory.newInstance();

   private static Matrix a(String var0, Matrix var1) {
      float var3 = 0.0F;
      if (var0.startsWith("matrix(")) {
         com.b.a.g var5 = f(var0.substring("matrix(".length()));
         if (com.b.a.g.a(var5).size() == 6) {
            Matrix var6 = new Matrix();
            var6.setValues(
               new float[]{
                  (Float)com.b.a.g.a(var5).get(0),
                  (Float)com.b.a.g.a(var5).get(2),
                  (Float)com.b.a.g.a(var5).get(4),
                  (Float)com.b.a.g.a(var5).get(1),
                  (Float)com.b.a.g.a(var5).get(3),
                  (Float)com.b.a.g.a(var5).get(5),
                  0.0F,
                  0.0F,
                  1.0F
               }
            );
            var1.preConcat(var6);
         }
      } else if (var0.startsWith("translate(")) {
         com.b.a.g var7 = f(var0.substring("translate(".length()));
         if (com.b.a.g.a(var7).size() > 0) {
            var3 = (Float)com.b.a.g.a(var7).get(0);
            float var2;
            if (com.b.a.g.a(var7).size() > 1) {
               var2 = (Float)com.b.a.g.a(var7).get(1);
            } else {
               var2 = 0.0F;
            }

            var1.preTranslate(var3, var2);
         }
      } else if (var0.startsWith("scale(")) {
         com.b.a.g var8 = f(var0.substring("scale(".length()));
         if (com.b.a.g.a(var8).size() > 0) {
            var3 = (Float)com.b.a.g.a(var8).get(0);
            float var12;
            if (com.b.a.g.a(var8).size() > 1) {
               var12 = (Float)com.b.a.g.a(var8).get(1);
            } else {
               var12 = var3;
            }

            var1.preScale(var3, var12);
         }
      } else if (var0.startsWith("skewX(")) {
         com.b.a.g var9 = f(var0.substring("skewX(".length()));
         if (com.b.a.g.a(var9).size() > 0) {
            var1.preSkew((float)Math.tan(((Float)com.b.a.g.a(var9).get(0)).floatValue()), 0.0F);
         }
      } else if (var0.startsWith("skewY(")) {
         com.b.a.g var10 = f(var0.substring("skewY(".length()));
         if (com.b.a.g.a(var10).size() > 0) {
            var1.preSkew(0.0F, (float)Math.tan(((Float)com.b.a.g.a(var10).get(0)).floatValue()));
         }
      } else if (var0.startsWith("rotate(")) {
         com.b.a.g var11 = f(var0.substring("rotate(".length()));
         if (com.b.a.g.a(var11).size() > 0) {
            float var4 = (Float)com.b.a.g.a(var11).get(0);
            float var13;
            if (com.b.a.g.a(var11).size() > 2) {
               var13 = (Float)com.b.a.g.a(var11).get(1);
               var3 = (Float)com.b.a.g.a(var11).get(2);
            } else {
               var13 = 0.0F;
            }

            var1.preTranslate(var13, var3);
            var1.preRotate(var4);
            var1.preTranslate(-var13, -var3);
         }
      } else {
         Log.w("SVG", "Invalid transform (" + var0 + ")");
      }

      return var1;
   }

   public static com.b.a.b a(Resources var0, int var1) {
      return a(var0.openRawResource(var1), null, var0.getDisplayMetrics().densityDpi);
   }

   public static com.b.a.b a(Resources var0, int var1, int var2) {
      return a(var0.openRawResource(var1), var2, var0.getDisplayMetrics().densityDpi);
   }

   private static com.b.a.b a(InputStream var0, Integer var1, float var2) {
      try {
         XMLReader var5 = b.newSAXParser().getXMLReader();
         Picture var3 = new Picture();
         com.b.a.i var4 = new com.b.a.i(var3, null);
         var4.a(var1);
         var4.a(var2);
         var5.setContentHandler(var4);
         BufferedInputStream var6 = new BufferedInputStream(var0);
         InputSource var9 = new InputSource(var6);
         var5.parse(var9);
         com.b.a.b var8 = new com.b.a.b(var3, var4.o);
         if (!Float.isInfinite(var4.p.top)) {
            var8.a(var4.p);
         }

         return var8;
      } catch (Exception var7) {
         Log.w("SVG", "Parse error: " + var7);
         throw new com.b.a.c(var7);
      }
   }

   public static com.b.a.b a(String var0) {
      return a(new ByteArrayInputStream(var0.getBytes()), null, a);
   }

   static String a(String var0, Attributes var1) {
      return d(var0, var1);
   }

   private static void a(Path var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, boolean var15, boolean var16) {
      double var17 = (var1 - var5) / 2.0;
      double var19 = (var3 - var7) / 2.0;
      var13 = Math.toRadians(var13 % 360.0);
      double var25 = Math.cos(var13);
      double var27 = Math.sin(var13);
      double var21 = var25 * var17 + var27 * var19;
      double var23 = var17 * -var27 + var19 * var25;
      var9 = Math.abs(var9);
      var11 = Math.abs(var11);
      var17 = var9 * var9;
      var13 = var11 * var11;
      double var29 = var21 * var21;
      double var31 = var23 * var23;
      var19 = var29 / var17 + var31 / var13;
      if (var19 > 1.0) {
         var17 = var9 * Math.sqrt(var19);
         var13 = var11 * Math.sqrt(var19);
         var11 = var13;
         var9 = var17;
         var13 *= var13;
         var17 *= var17;
      }

      if (var15 == var16) {
         var19 = -1.0;
      } else {
         var19 = 1.0;
      }

      var17 = (var17 * var13 - var17 * var31 - var13 * var29) / (var13 * var29 + var17 * var31);
      var13 = var17;
      if (var17 < 0.0) {
         var13 = 0.0;
      }

      var13 = Math.sqrt(var13) * var19;
      var17 = var9 * var23 / var11 * var13;
      var19 = var13 * -(var11 * var21 / var9);
      var1 = (var1 + var5) / 2.0;
      var3 = (var3 + var7) / 2.0;
      var5 = var1 + (var25 * var17 - var27 * var19);
      var7 = var3 + (var25 * var19 + var27 * var17);
      var3 = (var21 - var17) / var9;
      var13 = (var23 - var19) / var11;
      var17 = (-var21 - var17) / var9;
      var19 = (-var23 - var19) / var11;
      var21 = Math.sqrt(var3 * var3 + var13 * var13);
      if (var13 < 0.0) {
         var1 = -1.0;
      } else {
         var1 = 1.0;
      }

      var21 = Math.toDegrees(var1 * Math.acos(var3 / var21));
      var23 = Math.sqrt((var3 * var3 + var13 * var13) * (var17 * var17 + var19 * var19));
      if (var3 * var19 - var17 * var13 < 0.0) {
         var1 = -1.0;
      } else {
         var1 = 1.0;
      }

      var3 = Math.toDegrees(var1 * Math.acos((var13 * var19 + var3 * var17) / var23));
      if (!var16 && var3 > 0.0) {
         var1 = var3 - 360.0;
      } else {
         var1 = var3;
         if (var16) {
            var1 = var3;
            if (var3 < 0.0) {
               var1 = var3 + 360.0;
            }
         }
      }

      var0.addArc(
         new RectF((float)(var5 - var9), (float)(var7 - var11), (float)(var5 + var9), (float)(var7 + var11)), (float)(var21 % 360.0), (float)(var1 % 360.0)
      );
   }

   static com.b.a.g b(String var0, Attributes var1) {
      return c(var0, var1);
   }

   public static String b(String var0) {
      return var0.replaceAll("\"", "&quot;").replaceAll("'", "&apos").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;");
   }

   static Matrix c(String var0) {
      return g(var0);
   }

   private static com.b.a.g c(String var0, Attributes var1) {
        int length = var1.getLength();
        for (int i = 0; i < length; i++) {
            if (var1.getLocalName(i).equals(var0)) {
                return f(var1.getValue(i));
            }
        }
        return null;
    }

   static RectF d(String var0) {
      return h(var0);
   }

   private static String d(String var0, Attributes var1) {
      int var3 = var1.getLength();
      int var2 = 0;

      while (true) {
         if (var2 >= var3) {
            var0 = null;
            break;
         }

         if (var1.getLocalName(var2).equals(var0)) {
            var0 = var1.getValue(var2);
            break;
         }

         var2++;
      }

      return var0;
   }

   static Path e(String var0) {
      return i(var0);
   }

   private static com.b.a.g f(String var0) {
      int var4 = var0.length();
      ArrayList var6 = new ArrayList();
      int var2 = 1;
      boolean var3 = false;

      int var1;
      for (var1 = 0; var2 < var4; var2++) {
         if (var3) {
            var3 = false;
         } else {
            char var5 = var0.charAt(var2);
            switch (var5) {
               case '\t':
               case '\n':
               case ' ':
               case ',':
               case '-':
                  String var7 = var0.substring(var1, var2);
                  if (var7.trim().length() > 0) {
                     var6.add(Float.parseFloat(var7));
                     if (var5 == '-') {
                        var1 = var2;
                     } else {
                        var1 = var2 + 1;
                        var3 = true;
                     }
                  } else {
                     var1++;
                  }
                  break;
               case ')':
               case 'A':
               case 'C':
               case 'H':
               case 'L':
               case 'M':
               case 'Q':
               case 'S':
               case 'T':
               case 'V':
               case 'Z':
               case 'a':
               case 'c':
               case 'h':
               case 'l':
               case 'm':
               case 'q':
               case 's':
               case 't':
               case 'v':
               case 'z':
                  var0 = var0.substring(var1, var2);
                  if (var0.trim().length() > 0) {
                     var6.add(Float.parseFloat(var0));
                  }

                  return new com.b.a.g(var6, var2);
            }
         }
      }

      String var11 = var0.substring(var1);
      if (var11.length() > 0) {
         try {
            var6.add(Float.parseFloat(var11));
         } catch (NumberFormatException var8) {
         }

         var1 = var0.length();
      }

      return new com.b.a.g(var6, var1);
   }

   private static Matrix g(String var0) {
      Matrix var2 = new Matrix();

      while (true) {
         a(var0, var2);
         int var1 = var0.indexOf(")");
         if (var1 <= 0 || var0.length() <= var1 + 1) {
            return var2;
         }

         var0 = var0.substring(var1 + 1).replaceFirst("[\\s,]*", "");
      }
   }

   private static RectF h(String var0) {
      com.b.a.g var1 = f(var0);
      RectF var2;
      if (com.b.a.g.a(var1).size() >= 4) {
         var2 = new RectF(
            (Float)com.b.a.g.a(var1).get(0), (Float)com.b.a.g.a(var1).get(1), (Float)com.b.a.g.a(var1).get(2), (Float)com.b.a.g.a(var1).get(3)
         );
      } else {
         var2 = null;
      }

      return var2;
   }

   private static Path i(String var0) {
        boolean z;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        int length = var0.length();
        com.b.a.a srcType0871 = new com.b.a.a(var0, 0);
        srcType0871.a();
        Path path = new Path();
        RectF rectF = new RectF();
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        char c = 'x';
        float f12 = 0.0f;
        float f13 = 0.0f;
        while (srcType0871.a < length) {
            char cCharAt = var0.charAt(srcType0871.a);
            if (!Character.isDigit(cCharAt) && cCharAt != '.' && cCharAt != '-') {
                srcType0871.c();
            } else if (c == 'M') {
                cCharAt = 'L';
            } else {
                cCharAt = c == 'm' ? 'l' : c;
            }
            path.computeBounds(rectF, true);
            switch (cCharAt) {
                case 'A':
                case 'a':
                    float fE = srcType0871.e();
                    float fE2 = srcType0871.e();
                    float fE3 = srcType0871.e();
                    int iE = (int) srcType0871.e();
                    int iE2 = (int) srcType0871.e();
                    float fE4 = srcType0871.e();
                    float fE5 = srcType0871.e();
                    if (cCharAt == 'a') {
                        fE4 += f12;
                        fE5 += f13;
                    }
                    a(path, f12, f13, fE4, fE5, fE, fE2, fE3, iE == 1, iE2 == 1);
                    z = false;
                    f = f8;
                    f2 = f9;
                    f4 = f10;
                    f3 = f11;
                    f6 = fE5;
                    f5 = fE4;
                    break;
                case 'C':
                case 'c':
                    float fE6 = srcType0871.e();
                    float fE7 = srcType0871.e();
                    float fE8 = srcType0871.e();
                    float fE9 = srcType0871.e();
                    float fE10 = srcType0871.e();
                    float fE11 = srcType0871.e();
                    if (cCharAt == 'c') {
                        fE8 += f12;
                        fE10 += f12;
                        fE7 += f13;
                        fE9 += f13;
                        fE11 += f13;
                        f7 = fE6 + f12;
                    } else {
                        f7 = fE6;
                    }
                    path.cubicTo(f7, fE7, fE8, fE9, fE10, fE11);
                    z = true;
                    f = f8;
                    f2 = f9;
                    float f14 = fE9;
                    f3 = fE8;
                    f4 = f14;
                    float f15 = fE11;
                    f5 = fE10;
                    f6 = f15;
                    break;
                case 'H':
                case 'h':
                    float fE12 = srcType0871.e();
                    if (cCharAt != 'h') {
                        path.lineTo(fE12, f13);
                        f = f8;
                        f2 = f9;
                        f4 = f10;
                        f3 = f11;
                        f5 = fE12;
                        f6 = f13;
                        z = false;
                    } else {
                        path.rLineTo(fE12, 0.0f);
                        float f16 = fE12 + f12;
                        f = f8;
                        f2 = f9;
                        f4 = f10;
                        f3 = f11;
                        f5 = f16;
                        f6 = f13;
                        z = false;
                    }
                    break;
                case 'L':
                case 'l':
                    float fE13 = srcType0871.e();
                    float fE14 = srcType0871.e();
                    if (cCharAt != 'l') {
                        path.lineTo(fE13, fE14);
                        z = false;
                        f = f8;
                        f2 = f9;
                        f4 = f10;
                        f3 = f11;
                        f5 = fE13;
                        f6 = fE14;
                    } else {
                        path.rLineTo(fE13, fE14);
                        float f17 = fE13 + f12;
                        float f18 = fE14 + f13;
                        z = false;
                        f = f8;
                        f2 = f9;
                        f4 = f10;
                        f3 = f11;
                        f5 = f17;
                        f6 = f18;
                    }
                    break;
                case 'M':
                case 'm':
                    float fE15 = srcType0871.e();
                    float fE16 = srcType0871.e();
                    if (cCharAt == 'm') {
                        path.rMoveTo(fE15, fE16);
                        float f19 = fE15 + f12;
                        f = f13 + fE16;
                        f2 = f19;
                    } else {
                        path.moveTo(fE15, fE16);
                        f = fE16;
                        f2 = fE15;
                    }
                    z = false;
                    f4 = f10;
                    f3 = f11;
                    f6 = f;
                    f5 = f2;
                    break;
                case 'Q':
                case 'q':
                    float fE17 = srcType0871.e();
                    float fE18 = srcType0871.e();
                    float fE19 = srcType0871.e();
                    float fE20 = srcType0871.e();
                    if (cCharAt == 'q') {
                        fE19 += f12;
                        fE20 += f13;
                        fE17 += f12;
                        fE18 += f13;
                    }
                    path.cubicTo(f12, f13, fE17, fE18, fE19, fE20);
                    z = true;
                    f = f8;
                    f2 = f9;
                    float f20 = fE18;
                    f3 = fE17;
                    f4 = f20;
                    float f21 = fE20;
                    f5 = fE19;
                    f6 = f21;
                    break;
                case 'S':
                case 's':
                    float fE21 = srcType0871.e();
                    float fE22 = srcType0871.e();
                    float fE23 = srcType0871.e();
                    float fE24 = srcType0871.e();
                    if (cCharAt == 's') {
                        fE21 += f12;
                        fE23 += f12;
                        fE22 += f13;
                        fE24 += f13;
                    }
                    path.cubicTo((f12 * 2.0f) - f11, (2.0f * f13) - f10, fE21, fE22, fE23, fE24);
                    z = true;
                    f = f8;
                    f2 = f9;
                    float f22 = fE22;
                    f3 = fE21;
                    f4 = f22;
                    float f23 = fE24;
                    f5 = fE23;
                    f6 = f23;
                    break;
                case 'T':
                case 't':
                    float fE25 = srcType0871.e();
                    float fE26 = srcType0871.e();
                    if (cCharAt == 't') {
                        fE25 += f12;
                        fE26 += f13;
                    }
                    float f24 = (2.0f * f12) - f11;
                    float f25 = (2.0f * f13) - f10;
                    path.cubicTo(f12, f13, f24, f25, fE25, fE26);
                    z = true;
                    f = f8;
                    f2 = f9;
                    f3 = f24;
                    f4 = f25;
                    float f26 = fE26;
                    f5 = fE25;
                    f6 = f26;
                    break;
                case 'V':
                case 'v':
                    float fE27 = srcType0871.e();
                    if (cCharAt != 'v') {
                        path.lineTo(f12, fE27);
                        z = false;
                        f2 = f9;
                        f4 = f10;
                        f3 = f11;
                        f6 = fE27;
                        f5 = f12;
                        f = f8;
                    } else {
                        path.rLineTo(0.0f, fE27);
                        float f27 = fE27 + f13;
                        z = false;
                        f2 = f9;
                        f4 = f10;
                        f3 = f11;
                        f6 = f27;
                        f5 = f12;
                        f = f8;
                    }
                    break;
                case 'Z':
                case 'z':
                    path.close();
                    z = false;
                    f = f8;
                    f2 = f9;
                    f4 = f10;
                    f3 = f11;
                    f6 = f8;
                    f5 = f9;
                    break;
                default:
                    Log.w("SVG", "Invalid path command: " + cCharAt);
                    srcType0871.c();
                    f2 = f9;
                    f4 = f10;
                    f3 = f11;
                    f6 = f13;
                    f5 = f12;
                    f = f8;
                    z = false;
                    break;
            }
            if (!z) {
                f4 = f6;
                f3 = f5;
            }
            srcType0871.a();
            f8 = f;
            f9 = f2;
            f10 = f4;
            f11 = f3;
            f13 = f6;
            c = cCharAt;
            f12 = f5;
        }
        return path;
    }
}
