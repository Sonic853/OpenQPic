package com.alensw.svg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import org.xml.sax.Attributes;

class SvgText {
   final SvgContentHandler a;
   private Paint b;
   private Paint c;
   private float d;
   private float e;
   private String f;
   private boolean g;
   private int h;

   public SvgText(SvgContentHandler var1, Attributes var2) {
      this.a = var1;
      this.b = null;
      this.c = null;
      this.h = 0;
      this.d = SvgContentHandler.a(var1, "x", var2, 0.0F);
      this.e = SvgContentHandler.a(var1, "y", var2, 0.0F);
      this.f = null;
      this.g = true;
      SvgAttributes var3 = new SvgAttributes(var2, null);
      if (SvgContentHandler.a(var1, var3, var1.t)) {
         this.c = new Paint(var1.h);
         SvgContentHandler.a(var1, var2, this.c);
      }

      if (SvgContentHandler.a(var1, var3)) {
         this.b = new Paint(var1.d);
         SvgContentHandler.a(var1, var2, this.b);
      }

      String var4 = SvgParser.a("alignment-baseline", var2);
      if ("middle".equals(var4)) {
         this.h = 1;
      } else if ("top".equals(var4)) {
         this.h = 2;
      }
   }

   public void a(Canvas var1) {
      if (this.c != null) {
         var1.drawText(this.f, this.d, this.e, this.c);
      }

      if (this.b != null) {
         var1.drawText(this.f, this.d, this.e, this.b);
      }
   }

   public void a(char[] var1, int var2, int var3) {
      if (this.a()) {
         if (this.f == null) {
            this.f = new String(var1, var2, var3);
         } else {
            this.f = this.f + new String(var1, var2, var3);
         }

         if (this.h > 0) {
            Paint var7;
            if (this.b == null) {
               var7 = this.c;
            } else {
               var7 = this.b;
            }

            Rect var6 = new Rect();
            var7.getTextBounds(this.f, 0, this.f.length(), var6);
            float var5 = this.e;
            float var4;
            if (this.h == 1) {
               var4 = -var6.centerY();
            } else {
               var4 = var6.height();
            }

            this.e = var4 + var5;
         }
      }
   }

   public boolean a() {
      return this.g;
   }

   public void b() {
      this.g = false;
   }
}
