package com.alensw.location;

import android.content.Context;
import android.text.TextUtils;
import java.util.Locale;

public class PlaceAddress {
   public String a;
   public String b;
   public String c;
   public String d;
   public String e;
   public String f;

   public String a() {
      StringBuilder var3 = new StringBuilder(128);
      if (!TextUtils.isEmpty(this.d)) {
         var3.append(this.d).append(',');
      }

      if (!TextUtils.isEmpty(this.c) && !this.c.equals(this.b)) {
         var3.append(this.c).append(',');
      }

      if (!TextUtils.isEmpty(this.b)) {
         var3.append(this.b);
      }

      int var2 = var3.length();
      int var1 = var2;
      if (var2 > 0) {
         var1 = var2;
         if (var3.charAt(var2 - 1) == ',') {
            var1 = var2 - 1;
            var3.deleteCharAt(var1);
         }
      }

      if (var1 == 0 && !TextUtils.isEmpty(this.a)) {
         var3.append(this.a);
      }

      return var3.toString();
   }

   public String a(Context var1) {
      StringBuilder var4 = new StringBuilder(256);
      boolean var3 = Locale.CHINA.equals(var1.getResources().getConfiguration().locale);
      if (!TextUtils.isEmpty(this.a) && (!var3 || !"中国".equals(this.a))) {
         var4.append(this.a).append(',');
      }

      if (!TextUtils.isEmpty(this.b)) {
         var4.append(this.b).append(',');
      }

      if (!TextUtils.isEmpty(this.c) && !this.c.equals(this.b)) {
         var4.append(this.c).append(',');
      }

      if (!TextUtils.isEmpty(this.d)) {
         var4.append(this.d).append(',');
      }

      if (!TextUtils.isEmpty(this.e)) {
         var4.append(this.e).append(',');
      }

      if (!TextUtils.isEmpty(this.f)) {
         var4.append(this.f);
      }

      int var2 = var4.length();
      if (var2 > 0 && var4.charAt(var2 - 1) == ',') {
         var4.deleteCharAt(var2 - 1);
      }

      return var4.toString();
   }

   @Override
   public String toString() {
      return this.a();
   }
}
