package com.b.a;

import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import java.util.ArrayList;

class f {
   String a;
   String b;
   boolean c;
   float d;
   float e;
   float f;
   float g;
   float h;
   float i;
   float j;
   ArrayList k = new ArrayList();
   ArrayList l = new ArrayList();
   Matrix m = null;
   Shader n = null;

   f(com.b.a.e unused) {
      this();
   }

   private f() {
   }

   public Shader a() {
      byte var2 = 0;
      Shader var3;
      if (this.n != null) {
         var3 = this.n;
      } else {
         int[] var6 = new int[this.l.size()];

         for (int var1 = 0; var1 < var6.length; var1++) {
            var6[var1] = (Integer)this.l.get(var1);
         }

         float[] var4 = new float[this.k.size()];

         for (int var5 = var2; var5 < var4.length; var5++) {
            var4[var5] = (Float)this.k.get(var5);
         }

         if (this.c) {
            this.n = new LinearGradient(this.d, this.e, this.f, this.g, var6, var4, TileMode.CLAMP);
         } else {
            this.n = new RadialGradient(this.h, this.i, this.j, var6, var4, TileMode.CLAMP);
         }

         if (this.m != null) {
            this.n.setLocalMatrix(this.m);
         }

         var3 = this.n;
      }

      return var3;
   }

   public com.b.a.f a(com.b.a.f var1) {
      com.b.a.f var2 = new com.b.a.f();
      var2.a = var1.a;
      var2.b = this.a;
      var2.c = var1.c;
      var2.d = var1.d;
      var2.f = var1.f;
      var2.e = var1.e;
      var2.g = var1.g;
      var2.h = var1.h;
      var2.i = var1.i;
      var2.j = var1.j;
      var2.k = this.k;
      var2.l = this.l;
      var2.m = this.m;
      if (var1.m != null) {
         if (this.m == null) {
            var2.m = var1.m;
         } else {
            Matrix var3 = new Matrix(this.m);
            var3.preConcat(var1.m);
            var2.m = var3;
         }
      }

      return var2;
   }
}
