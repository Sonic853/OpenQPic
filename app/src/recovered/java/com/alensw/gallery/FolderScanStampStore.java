package com.alensw.gallery;

import com.alensw.io.PropertyFile;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class FolderScanStampStore extends PropertyFile {
   public FolderScanStampStore(int var1) {
      super(var1);
   }

   protected FolderScanStamp a(DataInputStream var1) {
      try {
         return new FolderScanStamp(var1.readInt(), -1);
      } catch (IOException error) {
         throw com.alensw.compat.LegacyPlatform.propagate(error);
      }
   }

   @Override
   protected String a() {
      return "Integer";
   }

   public void a(ScannedFolder var1) {
      FolderScanStamp var2 = new FolderScanStamp(var1.e, var1.i());
      super.a(var1.getPath(), var2);
   }

   protected void a(DataOutputStream var1, FolderScanStamp var2) {
      try {
         var1.writeInt(var2.a);
      } catch (IOException error) {
         throw com.alensw.compat.LegacyPlatform.propagate(error);
      }
   }

   @Override
   protected void a(DataOutputStream var1, Object var2) {
      this.a(var1, (FolderScanStamp)var2);
   }

   public boolean a(String var1, int var2) {
      FolderScanStamp var4 = (FolderScanStamp)super.b(var1);
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
