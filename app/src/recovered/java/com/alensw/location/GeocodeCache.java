package com.alensw.location;

import android.content.Context;
import com.alensw.cache.LongKeyDiskCache;
import com.alensw.io.StoragePaths;
import java.io.RandomAccessFile;

public class GeocodeCache extends LongKeyDiskCache {
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
      return super.a(StoragePaths.a(var1), "geocode");
   }

   protected String b(RandomAccessFile var1) {
      try {
         return var1.readUTF();
      } catch (java.io.IOException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }
}
