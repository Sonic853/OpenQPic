package com.alensw.bean;

class MomentGroupKey {
   long a;
   String b;

   public MomentGroupKey(long var1, String var3) {
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
        MomentGroupKey srcType0245 = (MomentGroupKey) var1;
        return this.a == srcType0245.a && this.b.equals(srcType0245.b);
    }

   @Override
   public int hashCode() {
      return (int)this.a + this.b.hashCode();
   }
}
