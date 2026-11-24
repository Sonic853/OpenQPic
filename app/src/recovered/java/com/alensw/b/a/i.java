package com.alensw.b.a;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

class i extends LinkedHashMap {
   final com.alensw.b.a.h a;

   i(com.alensw.b.a.h var1, int var2, float var3, boolean var4) {
      super(var2, var3, var4);
      this.a = var1;
   }

   @Override
   protected boolean removeEldestEntry(Entry var1) {
      boolean var2;
      if (this.size() > com.alensw.b.a.h.a(this.a)) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         Object var3 = var1.getValue();
         if (var3 != null) {
            this.a.b(var3);
         }
      }

      return var2;
   }
}
