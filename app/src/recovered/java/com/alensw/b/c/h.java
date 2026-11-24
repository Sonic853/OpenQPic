package com.alensw.b.c;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class h extends com.alensw.b.c.g {
   public h(int var1) {
      super(var1);
   }

   @Override
   protected String a() {
      return "String";
   }

   protected String a(DataInputStream var1) {
      try {
         return var1.readUTF();
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   @Override
   protected void a(DataOutputStream var1, Object var2) {
      this.a(var1, (String)var2);
   }

   protected void a(DataOutputStream var1, String var2) {
      try {
         var1.writeUTF(var2);
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   @Override
   protected Object b(DataInputStream var1) {
      return this.a(var1);
   }
}
