package com.b.a;

import android.graphics.Color;
import android.util.Log;
import org.xml.sax.Attributes;

class h {
   com.b.a.k a = null;
   Attributes b;

   h(Attributes attributes, com.b.a.e unused) {
      this(attributes);
   }

   private h(Attributes var1) {
      this.b = var1;
      String var2 = com.b.a.d.a("style", var1);
      if (var2 != null) {
         this.a = new com.b.a.k(var2, null);
      }
   }

   private int a(int var1) {
      return (var1 & 3840) << 8 | (var1 & 3840) << 12 | (var1 & 240) << 4 | (var1 & 240) << 8 | (var1 & 15) << 4 | var1 & 15;
   }

   public String a(String var1) {
      String var2 = null;
      if (this.a != null) {
         var2 = this.a.a(var1);
      }

      String var3 = var2;
      if (var2 == null) {
         var3 = com.b.a.d.a(var1, this.b);
      }

      return var3;
   }

   public String b(String var1) {
      return this.a(var1);
   }

   public Integer c(String var1) {
      String value = a(var1);
      if (value == null) return null;
      if (value.startsWith("#") && (value.length() == 4 || value.length() == 7)) {
         try {
            int color = Integer.parseInt(value.substring(1), 16);
            return value.length() == 4 ? a(color) : color;
         } catch (NumberFormatException failure) {
            return null;
         }
      }
      try {
         return Color.parseColor(value);
      } catch (IllegalArgumentException failure) {
         Log.w("SVG", "unknown color: " + value);
         return null;
      }
   }

   public Float d(String var1) {
      String value = a(var1);
      if (value == null) return null;
      try {
         return Float.parseFloat(value);
      } catch (NumberFormatException failure) {
         return null;
      }
   }
}
