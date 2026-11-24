package com.alensw.a;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class b extends com.alensw.b.c.g {
   public b(int var1) {
      super(var1);
   }

   protected com.alensw.a.a a(DataInputStream var1) {
      try {
         return new com.alensw.a.a(var1.readInt(), -1);
      } catch (IOException error) {
         throw com.alensw.compat.LegacyPlatform.propagate(error);
      }
   }

   @Override
   protected String a() {
      return "Integer";
   }

   public void a(com.alensw.a.e var1) {
      com.alensw.a.a var2 = new com.alensw.a.a(var1.e, var1.i());
      super.a(var1.getPath(), var2);
   }

   protected void a(DataOutputStream var1, com.alensw.a.a var2) {
      try {
         var1.writeInt(var2.a);
      } catch (IOException error) {
         throw com.alensw.compat.LegacyPlatform.propagate(error);
      }
   }

   @Override
   protected void a(DataOutputStream var1, Object var2) {
      this.a(var1, (com.alensw.a.a)var2);
   }

   public boolean a(String var1, int var2) {
      com.alensw.a.a var4 = (com.alensw.a.a)super.b(var1);
      boolean var3;
      if (var4 != null && var4.a == var2 && var4.b == 0) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   @Override
   protected Object b(DataInputStream var1) {
      return this.a(var1);
   }
}
