package com.alensw.bean;

class n {
   long a;
   String b;

   public n(long var1, String var3) {
      this.a = var1;
      if (var3 == null) {
         var3 = "";
      }

      this.b = var3;
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        com.alensw.bean.n srcType0245 = (com.alensw.bean.n) var1;
        return this.a == srcType0245.a && this.b.equals(srcType0245.b);
    }

   @Override
   public int hashCode() {
      return (int)this.a + this.b.hashCode();
   }
}
