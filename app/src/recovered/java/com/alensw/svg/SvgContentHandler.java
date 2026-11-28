package com.alensw.svg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Region.Op;
import android.os.Build.VERSION;
import android.util.Base64;
import android.util.Log;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

class SvgContentHandler extends DefaultHandler {
   private static final Matrix A = new Matrix();
   HashMap a = new HashMap();
   Picture b;
   Canvas c;
   Paint d;
   boolean e = false;
   Stack f = new Stack();
   Stack g = new Stack();
   Paint h;
   boolean i = false;
   Stack j = new Stack();
   Stack k = new Stack();
   float l = 1.0F;
   Stack m = new Stack();
   RectF n = new RectF();
   RectF o = null;
   RectF p = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
   Integer q = null;
   float r = SvgParser.a;
   int s = 0;
   HashMap t;
   SvgGradient u;
   SvgText v;
   private boolean w = false;
   private int x = 0;
   private boolean y = false;
   private boolean z;

   SvgContentHandler(Picture picture, SvgConstructorMarker unused) {
      this(picture);
   }

   private SvgContentHandler(Picture var1) {
      this.t = new HashMap();
      this.u = null;
      this.v = null;
      this.z = false;
      this.b = var1;
      this.d = new Paint();
      this.d.setAntiAlias(true);
      this.d.setStyle(Style.STROKE);
      this.h = new Paint();
      this.h.setAntiAlias(true);
      this.h.setStyle(Style.FILL);
   }

   private static float a(String var0, float var1) {
      float var2;
      try {
         var2 = Float.parseFloat(var0);
      } catch (NumberFormatException var3) {
         return var1;
      }

      return var2;
   }

   private SvgGradient a(boolean var1, Attributes var2) {
      SvgGradient var4 = new SvgGradient(null);
      var4.a = SvgParser.a("id", var2);
      var4.c = var1;
      if (var1) {
         var4.d = this.a("x1", var2, Float.valueOf(0.0F));
         var4.f = this.a("x2", var2, Float.valueOf(0.0F));
         var4.e = this.a("y1", var2, Float.valueOf(0.0F));
         var4.g = this.a("y2", var2, Float.valueOf(0.0F));
      } else {
         var4.h = this.a("cx", var2, Float.valueOf(0.0F));
         var4.i = this.a("cy", var2, Float.valueOf(0.0F));
         var4.j = this.a("r", var2, Float.valueOf(0.0F));
      }

      String var3 = SvgParser.a("gradientTransform", var2);
      if (var3 != null) {
         var4.m = SvgParser.c(var3);
      }

      var3 = SvgParser.a("href", var2);
      if (var3 != null) {
         String var5 = var3;
         if (var3.startsWith("#")) {
            var5 = var3.substring(1);
         }

         var4.b = var5;
      }

      return var4;
   }

   static Float a(SvgContentHandler var0, String var1, Attributes var2, Float var3) {
      return var0.a(var1, var2, var3);
   }

   private Float a(String var1, Attributes var2) {
      return this.a(var1, var2, null);
   }

   private Float a(String var1, Attributes var2, float var3) {
      String var5 = SvgParser.a(var1, var2);
      Float var4;
      if (var5 == null) {
         var4 = null;
      } else if (var5.endsWith("px")) {
         var4 = Float.parseFloat(var5.substring(0, var5.length() - 2));
      } else if (var5.endsWith("pt")) {
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3 / 72.0F;
      } else if (var5.endsWith("pc")) {
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3 / 6.0F;
      } else if (var5.endsWith("cm")) {
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3 / 2.54F;
      } else if (var5.endsWith("mm")) {
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3 / 254.0F;
      } else if (var5.endsWith("in")) {
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3;
      } else if (var5.endsWith("em")) {
         var3 = this.h.getTextSize();
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3;
      } else if (var5.endsWith("ex")) {
         var3 = this.h.getTextSize();
         var4 = Float.valueOf(var5.substring(0, var5.length() - 2)) * var3 / 2.0F;
      } else if (var5.endsWith("%")) {
         Float var6 = Float.valueOf(var5.substring(0, var5.length() - 1));
         if (var1.indexOf("x") >= 0 || var1.equals("width")) {
            var3 = this.c.getWidth() / 100.0F;
         } else if (var1.indexOf("y") < 0 && !var1.equals("height")) {
            var3 = (this.c.getHeight() + this.c.getWidth()) / 2.0F;
         } else {
            var3 = this.c.getHeight() / 100.0F;
         }

         var4 = var3 * var6;
      } else {
         var4 = Float.valueOf(var5);
      }

      return var4;
   }

   private Float a(String var1, Attributes var2, Float var3) {
      Float var4 = this.a(var1, var2, this.r);
      if (var4 == null) {
         var4 = var3;
      }

      return var4;
   }

   private void a() {
      this.c.restore();
      this.s--;
   }

   private void a(float var1, float var2) {
      if (var1 < this.p.left) {
         this.p.left = var1;
      }

      if (var1 > this.p.right) {
         this.p.right = var1;
      }

      if (var2 < this.p.top) {
         this.p.top = var2;
      }

      if (var2 > this.p.bottom) {
         this.p.bottom = var2;
      }
   }

   private void a(float var1, float var2, float var3, float var4) {
      this.a(var1, var2);
      this.a(var1 + var3, var2 + var4);
   }

   private void a(Canvas var1, float var2, float var3, float var4, float var5, byte[] var6) {
      Bitmap var8 = BitmapFactory.decodeByteArray(var6, 0, var6.length);
      if (var8 != null) {
         var8.prepareToDraw();
         Paint var7 = new Paint(3);
         RectF var9 = new RectF(var2, var3, var2 + var4, var3 + var5);
         var1.clipRect(var9, Op.REPLACE);
         var1.drawBitmap(var8, null, var9, var7);
         var8.recycle();
      }
   }

   private void a(Path var1) {
      var1.computeBounds(this.n, false);
      this.a(this.n.left, this.n.top);
      this.a(this.n.right, this.n.bottom);
   }

   private void a(SvgAttributes var1, Integer var2, boolean var3, Paint var4) {
      Float var6 = var1.d("opacity");
      Float var5 = var6;
      if (var6 == null) {
         String var8;
         if (var3) {
            var8 = "fill-opacity";
         } else {
            var8 = "stroke-opacity";
         }

         var5 = var1.d(var8);
      }

      Float var7 = var5;
      if (var5 == null) {
         var7 = 1.0F;
      }

      var4.setColor(Math.round(var7 * 255.0F * this.l) << 24 | var2);
   }

   private void a(String var1, String var2) {
      byte var10 = 0;
      float var6 = 0.0F;
      if (var1 != null) {
         if (var1.equals("none")) {
            this.d.setPathEffect(null);
         } else {
            StringTokenizer var11 = new StringTokenizer(var1, " ,");
            int var8 = var11.countTokens();
            int var7 = var8;
            if ((var8 & 1) == 1) {
               var7 = var8 * 2;
            }

            float[] var13 = new float[var7];
            float var5 = 1.0F;
            float var3 = 0.0F;
            var7 = 0;

            while (true) {
               int var9 = var7;
               var8 = var10;
               float var4 = var3;
               if (!var11.hasMoreTokens()) {
                  while (var9 < var13.length) {
                     var3 = var13[var8];
                     var13[var9] = var3;
                     var4 += var3;
                     var9++;
                     var8++;
                  }

                  var3 = var6;
                  label27:
                  if (var2 != null) {
                     try {
                        var3 = Float.parseFloat(var2);
                     } catch (NumberFormatException var12) {
                        var3 = var6;
                        break label27;
                     }

                     var3 %= var4;
                  }

                  this.d.setPathEffect(new DashPathEffect(var13, var3));
                  break;
               }

               var5 = a(var11.nextToken(), var5);
               var13[var7] = var5;
               var3 += var5;
               var7++;
            }
         }
      }
   }

   private void a(Attributes var1) {
      String var2 = SvgParser.a("transform", var1);
      Matrix var3;
      if (var2 == null) {
         var3 = A;
      } else {
         var3 = SvgParser.c(var2);
      }

      this.s++;
      this.c.save();
      this.c.concat(var3);
   }

   private boolean a(SvgAttributes var1) {
      boolean var4 = true;
      boolean var3 = false;
      boolean var2;
      if ("none".equals(var1.b("display"))) {
         var2 = var3;
      } else {
         Float var5 = var1.d("stroke-width");
         if (var5 != null) {
            this.d.setStrokeWidth(var5);
         }

         var2 = var3;
         if (!(this.d.getStrokeWidth() <= 0.0F)) {
            String var6 = var1.b("stroke-linecap");
            if ("round".equals(var6)) {
               this.d.setStrokeCap(Cap.ROUND);
            } else if ("square".equals(var6)) {
               this.d.setStrokeCap(Cap.SQUARE);
            } else if ("butt".equals(var6)) {
               this.d.setStrokeCap(Cap.BUTT);
            }

            var6 = var1.b("stroke-linejoin");
            if ("miter".equals(var6)) {
               this.d.setStrokeJoin(Join.MITER);
            } else if ("round".equals(var6)) {
               this.d.setStrokeJoin(Join.ROUND);
            } else if ("bevel".equals(var6)) {
               this.d.setStrokeJoin(Join.BEVEL);
            }

            this.a(var1.b("stroke-dasharray"), var1.b("stroke-dashoffset"));
            var6 = var1.a("stroke");
            if (var6 != null) {
               if (var6.equalsIgnoreCase("none")) {
                  this.d.setColor(0);
                  var2 = var3;
               } else {
                  Integer var7 = var1.c("stroke");
                  if (var7 != null) {
                     this.a(var1, var7, false, this.d);
                     var2 = true;
                  } else {
                     Log.d("SVG", "Unrecognized stroke color, using none: " + var6);
                     this.d.setColor(0);
                     var2 = var3;
                  }
               }
            } else if (this.e) {
               if (this.d.getColor() != 0) {
                  var2 = var4;
               } else {
                  var2 = false;
               }
            } else if (var5 != null && this.q != null) {
               this.d.setColor(this.q);
               var2 = true;
            } else {
               this.d.setColor(0);
               var2 = var3;
            }
         }
      }

      return var2;
   }

   private boolean a(SvgAttributes var1, HashMap var2) {
      boolean var5 = false;
      boolean var4 = true;
      if ("none".equals(var1.b("display"))) {
         var4 = false;
      } else {
         String var6 = var1.b("fill");
         if (var6 != null) {
            if (var6.startsWith("url(#")) {
               var6 = var6.substring("url(#".length(), var6.length() - 1);
               SvgGradient var8 = (SvgGradient)var2.get(var6);
               if (var8 != null) {
                  this.h.setShader(var8.a());
               } else {
                  Log.w("SVG", "Didn't find shader, using black: " + var6);
                  this.h.setShader(null);
                  this.a(var1, -16777216, true, this.h);
               }
            } else if (var6.equalsIgnoreCase("none")) {
               this.h.setShader(null);
               this.h.setColor(0);
            } else {
               this.h.setShader(null);
               Integer var9 = var1.c("fill");
               if (var9 != null) {
                  this.a(var1, var9, true, this.h);
               } else {
                  Log.w("SVG", "Unrecognized fill color, using black: " + var6);
                  this.a(var1, -16777216, true, this.h);
               }
            }
         } else if (this.i) {
            var4 = var5;
            if (this.h.getColor() != 0) {
               var4 = true;
            }
         } else {
            this.h.setShader(null);
            Paint var7 = this.h;
            int var3;
            if (this.q != null) {
               var3 = this.q;
            } else {
               var3 = -16777216;
            }

            var7.setColor(var3);
         }
      }

      return var4;
   }

   static boolean a(SvgContentHandler var0, SvgAttributes var1) {
      return var0.a(var1);
   }

   static boolean a(SvgContentHandler var0, SvgAttributes var1, HashMap var2) {
      return var0.a(var1, var2);
   }

   static boolean a(SvgContentHandler var0, Attributes var1, Paint var2) {
      return var0.a(var1, var2);
   }

   private boolean a(Attributes var1, Paint var2) {
      boolean var3;
      if ("none".equals(var1.getValue("display"))) {
         var3 = false;
      } else {
         if (var1.getValue("font-size") != null) {
            var2.setTextSize(this.a("font-size", var1, Float.valueOf(10.0F)));
         }

         Typeface var4 = this.d(var1);
         if (var4 != null) {
            var2.setTypeface(var4);
         }

         if (this.c(var1) != null) {
            var2.setTextAlign(this.c(var1));
         }

         var3 = true;
      }

      return var3;
   }

   private String b(Attributes var1) {
      String var3 = "";

      for (int var2 = 0; var2 < var1.getLength(); var2++) {
         var3 = var3 + " " + var1.getLocalName(var2) + "='" + var1.getValue(var2) + "'";
      }

      return var3;
   }

   private Align c(Attributes var1) {
      String var2 = SvgParser.a("text-anchor", var1);
      Align var3;
      if (var2 == null) {
         var3 = null;
      } else if ("middle".equals(var2)) {
         var3 = Align.CENTER;
      } else if ("end".equals(var2)) {
         var3 = Align.RIGHT;
      } else {
         var3 = Align.LEFT;
      }

      return var3;
   }

   private Typeface d(Attributes var1) {
      String var4 = SvgParser.a("font-family", var1);
      String var5 = SvgParser.a("font-style", var1);
      String var6 = SvgParser.a("font-weight", var1);
      Typeface var7;
      if (var4 == null && var5 == null && var6 == null) {
         var7 = null;
      } else {
         byte var2 = 0;
         if ("italic".equals(var5)) {
            var2 = 2;
         }

         int var3 = var2;
         if ("bold".equals(var6)) {
            var3 = var2 | 1;
         }

         var7 = Typeface.create(var4, var3);
      }

      return var7;
   }

   public void a(float var1) {
      this.r = var1;
   }

   public void a(Integer var1) {
      this.q = var1;
   }

   @Override
   public void characters(char[] var1, int var2, int var3) {
      if (this.v != null) {
         this.v.a(var1, var2, var3);
      }
   }

   @Override
   public void endDocument() {
   }

   @Override
   public void endElement(String var1, String var2, String var3) {
      if (this.z) {
         if (var2.equals("defs")) {
            this.z = false;
         }
      } else if (var2.equals("svg")) {
         this.b.endRecording();
      } else if (!this.w && var2.equals("text")) {
         if (this.v != null) {
            this.v.a(this.c);
            this.v.b();
         }

         this.a();
      } else if (!var2.equals("linearGradient") && !var2.equals("radialGradient")) {
         if (var2.equals("g")) {
            if (this.y) {
               this.y = false;
            }

            if (this.w) {
               this.x--;
               if (this.x == 0) {
                  this.w = false;
               }
            }

            this.a();
            this.h = (Paint)this.j.pop();
            this.i = (Boolean)this.k.pop();
            this.d = (Paint)this.f.pop();
            this.e = (Boolean)this.g.pop();
            this.l = (Float)this.m.pop();
         }
      } else if (this.u.a != null) {
         if (this.u.b != null) {
            SvgGradient var4 = (SvgGradient)this.t.get(this.u.b);
            if (var4 != null) {
               this.u = var4.a(this.u);
            }
         }

         this.t.put(this.u.a, this.u);
      }
   }

   @Override
   public void startDocument() {
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) {
      if (!this.e) {
         this.d.setAlpha(255);
      }

      if (!this.i) {
         this.h.setAlpha(255);
      }

      if (this.y) {
         if (var2.equals("rect")) {
            float var5 = this.a("x", var4, Float.valueOf(0.0F));
            float var6 = this.a("y", var4, Float.valueOf(0.0F));
            Float var17 = this.a("width", var4);
            Float var35 = this.a("height", var4);
            this.o = new RectF(var5, var6, var17 + var5, var35 + var6);
         }
      } else if (!this.z) {
         if (var2.equals("svg")) {
            int var9;
            int var10;
            label308: {
               int var12 = (int)Math.ceil(this.a("width", var4).floatValue());
               int var11 = (int)Math.ceil(this.a("height", var4).floatValue());
               if (var12 != 0) {
                  var10 = var11;
                  var9 = var12;
                  if (var11 != 0) {
                     break label308;
                  }
               }

               RectF var18 = SvgParser.d(SvgParser.a("viewBox", var4));
               var10 = var11;
               var9 = var12;
               if (var18 != null) {
                  var10 = var11;
                  var9 = var12;
                  if (!var18.isEmpty()) {
                     var9 = Math.round(var18.width());
                     var10 = Math.round(var18.height());
                  }
               }
            }

            this.c = this.b.beginRecording(var9, var10);
         } else if (var2.equals("defs")) {
            this.z = true;
         } else if (var2.equals("linearGradient")) {
            this.u = this.a(true, var4);
         } else if (var2.equals("radialGradient")) {
            this.u = this.a(false, var4);
         } else if (var2.equals("stop")) {
            if (this.u != null) {
               float var55 = this.a("offset", var4);
               SvgStyle var36 = new SvgStyle(SvgParser.a("style", var4), null);
               var1 = var36.a("stop-color");
               int var63 = -16777216;
               if (var1 != null) {
                  if (var1.startsWith("#")) {
                     var63 = Integer.parseInt(var1.substring(1), 16);
                  } else {
                     var63 = Integer.parseInt(var1, 16);
                  }
               }

               var1 = var36.a("stop-opacity");
               if (var1 != null) {
                  var63 |= Math.round(Float.parseFloat(var1) * 255.0F) << 24;
               } else {
                  var63 |= -16777216;
               }

               this.u.k.add(var55);
               this.u.l.add(var63);
            }
         } else if (var2.equals("use")) {
            var3 = var4.getValue("xlink:href");
            String var15 = var4.getValue("transform");
            var1 = var4.getValue("x");
            String var14 = var4.getValue("y");
            StringBuilder var37 = new StringBuilder(4096);
            var37.append("<g");
            var37.append(" xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' version='1.1'");
            if (var15 != null || var1 != null || var14 != null) {
               var37.append(" transform='");
               if (var15 != null) {
                  var37.append(SvgParser.b(var15));
               }

               if (var1 != null || var14 != null) {
                  var37.append("translate(");
                  if (var1 != null) {
                     var1 = SvgParser.b(var1);
                  } else {
                     var1 = "0";
                  }

                  var37.append(var1);
                  var37.append(",");
                  if (var14 != null) {
                     var1 = SvgParser.b(var14);
                  } else {
                     var1 = "0";
                  }

                  var37.append(var1);
                  var37.append(")");
               }

               var37.append("'");
            }

            for (int var65 = 0; var65 < var4.getLength(); var65++) {
               var1 = var4.getQName(var65);
               if (!"x".equals(var1)
                  && !"y".equals(var1)
                  && !"width".equals(var1)
                  && !"height".equals(var1)
                  && !"xlink:href".equals(var1)
                  && !"transform".equals(var1)) {
                  var37.append(" ");
                  var37.append(var1);
                  var37.append("='");
                  var37.append(SvgParser.b(var4.getValue(var65)));
                  var37.append("'");
               }
            }

            var37.append(">");
            var37.append((String)this.a.get(var3.substring(1)));
            var37.append("</g>");
            InputSource var25 = new InputSource(new StringReader(var37.toString()));

            try {
               XMLReader var45 = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
               var45.setContentHandler(this);
               var45.parse(var25);
            } catch (Exception var16) {
               Log.d("SVG", var37.toString());
               var16.printStackTrace();
            }
         } else if (var2.equals("g")) {
            if ("bounds".equalsIgnoreCase(SvgParser.a("id", var4))) {
               this.y = true;
            }

            if (this.w) {
               this.x++;
            }

            if ("none".equals(SvgParser.a("display", var4)) && !this.w) {
               this.w = true;
               this.x = 1;
            }

            this.a(var4);
            SvgAttributes var26 = new SvgAttributes(var4, null);
            this.j.push(new Paint(this.h));
            this.f.push(new Paint(this.d));
            this.k.push(this.i);
            this.g.push(this.e);
            this.m.push(this.l);
            Float var38 = this.a("opacity", var4);
            if (var38 != null) {
               float var56 = this.l;
               this.l = var38 * var56;
            }

            this.a(var4, this.h);
            this.a(var4, this.d);
            this.a(var26, this.t);
            this.a(var26);
            boolean var13 = this.i;
            boolean var66;
            if (var26.b("fill") != null) {
               var66 = true;
            } else {
               var66 = false;
            }

            this.i = var66 | var13;
            var13 = this.e;
            if (var26.b("stroke") != null) {
               var66 = true;
            } else {
               var66 = false;
            }

            this.e = var66 | var13;
         } else if (!this.w && var2.equals("rect")) {
            float var60 = this.a("x", var4, Float.valueOf(0.0F));
            float var58 = this.a("y", var4, Float.valueOf(0.0F));
            Float var43 = this.a("width", var4);
            Float var34 = this.a("height", var4);
            float var61 = this.a("rx", var4, Float.valueOf(0.0F));
            float var62 = this.a("ry", var4, Float.valueOf(0.0F));
            this.a(var4);
            SvgAttributes var51 = new SvgAttributes(var4, null);
            if (this.a(var51, this.t)) {
               this.a(var60, var58, var43, var34.floatValue());
               if (var61 <= 0.0F && var62 <= 0.0F) {
                  this.c.drawRect(var60, var58, var43 + var60, var34 + var58, this.h);
               } else {
                  this.n.set(var60, var58, var43 + var60, var34 + var58);
                  this.c.drawRoundRect(this.n, var61, var62, this.h);
               }
            }

            if (this.a(var51)) {
               if (var61 <= 0.0F && var62 <= 0.0F) {
                  this.c.drawRect(var60, var58, var43 + var60, var34 + var58, this.d);
               } else {
                  this.n.set(var60, var58, var43 + var60, var34 + var58);
                  this.c.drawRoundRect(this.n, var61, var62, this.d);
               }
            }

            this.a();
         } else if (!this.w && var2.equals("image")) {
            var1 = SvgParser.a("href", var4);
            if (var1.startsWith("data") && var1.indexOf("base64") > 0 && VERSION.SDK_INT >= 8) {
               var1 = var1.substring(var1.indexOf(",") + 1);
               float var8 = this.a("x", var4, Float.valueOf(0.0F));
               float var7 = this.a("y", var4, Float.valueOf(0.0F));
               float var57 = this.a("width", var4, Float.valueOf(0.0F));
               float var59 = this.a("height", var4, Float.valueOf(0.0F));
               this.a(var4);
               this.a(var8, var7, var57, var59);
               this.a(this.c, var8, var7, var57, var59, Base64.decode(var1, 0));
               this.a();
            }
         } else if (!this.w && var2.equals("line")) {
            Float var31 = this.a("x1", var4);
            Float var71 = this.a("x2", var4);
            Float var42 = this.a("y1", var4);
            Float var50 = this.a("y2", var4);
            if (this.a(new SvgAttributes(var4, null))) {
               this.a(var4);
               this.a(var31, var42);
               this.a(var71, var50);
               this.c.drawLine(var31, var42, var71, var50, this.d);
               this.a();
            }
         } else if (!this.w && var2.equals("circle")) {
            Float var41 = this.a("cx", var4);
            Float var49 = this.a("cy", var4);
            Float var30 = this.a("r", var4);
            if (var41 != null && var49 != null && var30 != null) {
               this.a(var4);
               SvgAttributes var54 = new SvgAttributes(var4, null);
               if (this.a(var54, this.t)) {
                  this.a(var41 - var30, var49 - var30);
                  this.a(var41 + var30, var49 + var30);
                  this.c.drawCircle(var41, var49, var30, this.h);
               }

               if (this.a(var54)) {
                  this.c.drawCircle(var41, var49, var30, this.d);
               }

               this.a();
            }
         } else if (!this.w && var2.equals("ellipse")) {
            Float var40 = this.a("cx", var4);
            Float var70 = this.a("cy", var4);
            Float var29 = this.a("rx", var4);
            Float var48 = this.a("ry", var4);
            if (var40 != null && var70 != null && var29 != null && var48 != null) {
               this.a(var4);
               SvgAttributes var53 = new SvgAttributes(var4, null);
               this.n.set(var40 - var29, var70 - var48, var40 + var29, var70 + var48);
               if (this.a(var53, this.t)) {
                  this.a(var40 - var29, var70 - var48);
                  this.a(var40 + var29, var70 + var48);
                  this.c.drawOval(this.n, this.h);
               }

               if (this.a(var53)) {
                  this.c.drawOval(this.n, this.d);
               }

               this.a();
            }
         } else if (this.w || !var2.equals("polygon") && !var2.equals("polyline")) {
            if (!this.w && var2.equals("path")) {
               Path var28 = SvgParser.e(SvgParser.a("d", var4));
               this.a(var4);
               SvgAttributes var39 = new SvgAttributes(var4, null);
               if (this.a(var39, this.t)) {
                  this.a(var28);
                  this.c.drawPath(var28, this.h);
               }

               if (this.a(var39)) {
                  this.c.drawPath(var28, this.d);
               }

               this.a();
            } else if (!this.w && var2.equals("text")) {
               this.a(var4);
               this.v = new SvgText(this, var4);
            } else if (!this.w) {
               Log.d("SVG", String.format("Unrecognized tag: %s (%s)", var2, this.b(var4)));
            }
         } else {
            SvgNumberList var46 = SvgParser.b("points", var4);
            if (var46 != null) {
               Path var27 = new Path();
               ArrayList var47 = SvgNumberList.a(var46);
               if (var47.size() > 1) {
                  this.a(var4);
                  SvgAttributes var52 = new SvgAttributes(var4, null);
                  var27.moveTo((Float)var47.get(0), (Float)var47.get(1));

                  for (int var68 = 2; var68 < var47.size(); var68 += 2) {
                     var27.lineTo((Float)var47.get(var68), (Float)var47.get(var68 + 1));
                  }

                  if (var2.equals("polygon")) {
                     var27.close();
                  }

                  if (this.a(var52, this.t)) {
                     this.a(var27);
                     this.c.drawPath(var27, this.h);
                  }

                  if (this.a(var52)) {
                     this.c.drawPath(var27, this.d);
                  }

                  this.a();
               }
            }
         }
      }
   }
}
