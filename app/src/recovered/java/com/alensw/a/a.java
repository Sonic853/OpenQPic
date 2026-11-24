package com.alensw.a;

class a {
   int a;
   int b;

   public a(int var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public boolean equals(Object var1) {
        if (!(var1 instanceof com.alensw.a.a)) {
            return false;
        }
        com.alensw.a.a srcType0073 = (com.alensw.a.a) var1;
        return this.a == srcType0073.a && this.b == srcType0073.b;
    }
}
