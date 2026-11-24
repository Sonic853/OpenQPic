package com.b.a;

import java.util.HashMap;

class k {
   HashMap a = new HashMap();

   k(String style, com.b.a.e unused) {
      this(style);
   }

   private k(String var1) {
      String[] var5 = var1.split(";");
      int var3 = var5.length;

      for (int var2 = 0; var2 < var3; var2++) {
         String[] var4 = var5[var2].split(":");
         if (var4.length == 2) {
            this.a.put(var4[0], var4[1]);
         }
      }
   }

   public String a(String var1) {
      return (String)this.a.get(var1);
   }
}
