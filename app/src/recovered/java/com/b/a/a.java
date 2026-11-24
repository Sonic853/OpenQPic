package com.b.a;

public class a {
   private static final double[] e = new double[128];
   public int a;
   private final CharSequence b;
   private final int c;
   private char d;

   static {
      for (int var0 = 0; var0 < e.length; var0++) {
         e[var0] = Math.pow(10.0, var0);
      }
   }

   public a(CharSequence var1, int var2) {
      this.b = var1;
      this.a = var2;
      this.c = var1.length();
      this.d = var1.charAt(var2);
   }

   public static float a(int var0, int var1) {
      float var4;
      if (var1 < -125 || var0 == 0) {
         var4 = 0.0F;
      } else if (var1 >= 128) {
         if (var0 > 0) {
            var4 = Float.POSITIVE_INFINITY;
         } else {
            var4 = Float.NEGATIVE_INFINITY;
         }
      } else if (var1 == 0) {
         var4 = var0;
      } else {
         int var5 = var0;
         if (var0 >= 67108864) {
            var5 = var0 + 1;
         }

         double var2;
         if (var1 > 0) {
            var2 = var5 * e[var1];
         } else {
            var2 = var5 / e[-var1];
         }

         var4 = (float)var2;
      }

      return var4;
   }

   private void a(char var1) {
      throw new RuntimeException("Unexpected char '" + var1 + "'.");
   }

   private char f() {
      if (this.a < this.c) {
         this.a++;
      }

      char var1;
      if (this.a == this.c) {
         var1 = 0;
      } else {
         var1 = this.b.charAt(this.a);
      }

      return var1;
   }

   public void a() {
      while (this.a < this.c && Character.isWhitespace(this.b.charAt(this.a))) {
         this.c();
      }
   }

   public void b() {
      while (this.a < this.c) {
         switch (this.b.charAt(this.a)) {
            case '\t':
            case '\n':
            case ' ':
            case ',':
               this.c();
               break;
            default:
               return;
         }
      }
   }

   public void c() {
      this.d = this.f();
   }

   public float d() {
      boolean negative = d == '-';
      if (negative || d == '+') d = f();

      int mantissa = 0, digits = 0, shift = 0;
      boolean integer = d >= '0' && d <= '9';
      if (!integer && d != '.') return Float.NaN;
      if (d == '0') {
         do { d = f(); } while (d == '0');
         if ((d < '1' || d > '9') && d != '.' && d != 'e' && d != 'E') return 0.0F;
      }
      while (d >= '0' && d <= '9') {
         if (digits < 9) {
            mantissa = mantissa * 10 + (d - '0');
            digits++;
         } else {
            shift++;
         }
         d = f();
      }
      if (d == '.') {
         d = f();
         if (digits == 0 && d == '0') {
            do {
               d = f();
               shift--;
            } while (d == '0');
            if (!integer && (d < '1' || d > '9')) return 0.0F;
         } else if (!integer && (d < '0' || d > '9')) {
            a(d);
            return 0.0F;
         }
         while (d >= '0' && d <= '9') {
            if (digits < 9) {
               digits++;
               mantissa = mantissa * 10 + (d - '0');
               shift--;
            }
            d = f();
         }
      }
      int exponent = 0;
      if (d == 'e' || d == 'E') {
         d = f();
         boolean negativeExponent = d == '-';
         if (negativeExponent || d == '+') d = f();
         if (d < '0' || d > '9') {
            a(d);
            return 0.0F;
         }
         while (d == '0') d = f();
         int exponentDigits = 0;
         while (d >= '0' && d <= '9') {
            if (exponentDigits < 3) {
               exponent = exponent * 10 + (d - '0');
               exponentDigits++;
            }
            d = f();
         }
         if (negativeExponent) exponent = -exponent;
      }
      return a(negative ? -mantissa : mantissa, exponent + shift);
   }

   public float e() {
      this.a();
      float var1 = this.d();
      this.b();
      return var1;
   }
}
