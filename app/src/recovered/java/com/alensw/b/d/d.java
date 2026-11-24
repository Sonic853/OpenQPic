package com.alensw.b.d;

import android.content.Context;
import java.io.RandomAccessFile;

public class d extends com.alensw.b.a.g {
   @Override
   protected Object a(RandomAccessFile var1) {
      return this.b(var1);
   }

   @Override
   protected void a(RandomAccessFile var1, Object var2) {
      this.a(var1, (String)var2);
   }

   protected void a(RandomAccessFile var1, String var2) {
      try {
         var1.writeUTF(var2);
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   public boolean a(Context var1) {
      return super.a(com.alensw.b.c.b.a(var1), "geocode");
   }

   protected String b(RandomAccessFile var1) {
      try {
         return var1.readUTF();
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }
}
