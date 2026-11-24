package com.alensw.a;

import java.util.Comparator;

final class f implements Comparator {
   public int a(com.alensw.a.d var1, com.alensw.a.d var2) {
      return com.alensw.b.b.b.a.a(var1.b, var2.b);
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((com.alensw.a.d)var1, (com.alensw.a.d)var2);
   }
}
