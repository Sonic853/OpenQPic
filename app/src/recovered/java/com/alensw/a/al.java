package com.alensw.a;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.alensw.jni.JniUtils;
import java.lang.reflect.Method;

public class al extends JniUtils {
   private static Method b;
   private int a;

   static {
      new com.alensw.a.e(com.alensw.a.e.b, 0).a('\u0000', 0, 0L, null);

      for (Method var3 : com.alensw.a.e.class.getMethods()) {
         Class[] var2 = var3.getParameterTypes();
         if (var2.length == 4
            && var2[0] == char.class
            && var2[1] == int.class
            && var2[2] == long.class
            && var2[3] == String.class
            && var3.getReturnType() == void.class) {
            b = var3;
            break;
         }
      }
   }

   public al() {
      if (b == null) {
         throw new RuntimeException("no Folder.add!");
      } else {
         this.a = fsCreateScanner(b);
         if (this.a == 0) {
            throw new RuntimeException("create failed!");
         }
      }
   }

   public static String a(boolean var0, boolean var1, SharedPreferences var2) {
      byte var4 = 0;
      String[][] var7 = com.alensw.b.f.c.a;
      StringBuilder var6 = new StringBuilder(128);
      if (var0) {
         for (String var9 : var7[0]) {
            var6.append('/').append('I').append(var9);
         }

         String var13 = a(var7[1], 'I', var2);
         if (var13 != null && var13.length() > 0) {
            var6.append(var13);
         }
      }

      if (var1) {
         String[] var14 = var7[2];
         int var12 = var14.length;

         for (int var11 = var4; var11 < var12; var11++) {
            String var15 = var14[var11];
            var6.append('/').append('V').append(var15);
         }

         String var10 = a(var7[3], 'V', var2);
         if (var10 != null && var10.length() > 0) {
            var6.append(var10);
         }
      }

      return var6.toString();
   }

   private static String a(String[] var0, char var1, SharedPreferences var2) {
      StringBuilder var6 = new StringBuilder();
      String var5;
      if (var1 == 'I') {
         var5 = "extra_images_";
      } else {
         var5 = "extra_videos_";
      }

      String var7 = var6.append(var5).append(var0.length).toString();
      String var11 = var2.getString(var7, null);
      var5 = var11;
      if (var11 == null) {
         var6 = new StringBuilder(128);

         for (String var10 : var0) {
            if (com.alensw.b.f.c.d("0." + var10)) {
               var6.append('/').append(var1).append(var10);
            }
         }

         var5 = var6.toString();
         Editor var8 = var2.edit();
         var8.putString(var7, var5);
         com.alensw.b.l.b.a(var8);
      }

      return var5;
   }

   public int a(com.alensw.a.e var1, int var2) {
      return fsScanFolders(this.a, var1, var2);
   }

   public void a(String var1) {
      fsInitExtensions(this.a, var1);
   }

   public void a(boolean var1) {
      fsCancelScan(this.a, var1);
   }

   public int b(com.alensw.a.e var1, int var2) {
      return fsScanPictures(this.a, var1, var2);
   }

   @Override
   protected void finalize() {
      int var1 = this.a;
      this.a = 0;
      if (var1 != 0) {
         JniUtils.fsDestroyScanner(var1);
      }
   }
}
