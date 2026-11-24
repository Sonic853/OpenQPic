package com.alensw.b.c;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class a extends com.alensw.b.c.g {
   public a(int var1) {
      super(var1);
   }

   protected Boolean a(DataInputStream var1) {
      try {
         return var1.readBoolean();
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   @Override
   protected String a() {
      return "Boolean";
   }

   protected void a(DataOutputStream var1, Boolean var2) {
      try {
         var1.writeBoolean(var2);
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   @Override
   protected void a(DataOutputStream var1, Object var2) {
      this.a(var1, (Boolean)var2);
   }

   @Override
   protected Object b(DataInputStream var1) {
      return this.a(var1);
   }
}
