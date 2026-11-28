package com.alensw.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class IntegerPropertyFile extends PropertyFile {
   public IntegerPropertyFile(int var1) {
      super(var1);
   }

   protected Integer a(DataInputStream var1) {
      try {
         return var1.readInt();
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   @Override
   protected String a() {
      return "Integer";
   }

   protected void a(DataOutputStream var1, Integer var2) {
      try {
         var1.writeInt(var2);
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   @Override
   protected void a(DataOutputStream var1, Object var2) {
      this.a(var1, (Integer)var2);
   }

   @Override
   protected Object b(DataInputStream var1) {
      return this.a(var1);
   }
}
