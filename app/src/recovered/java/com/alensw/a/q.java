package com.alensw.a;

import android.content.Context;
import android.preference.PreferenceManager;
import com.alensw.bean.CommonFolder;
import java.io.File;
import java.util.Map.Entry;

public class q {
   private static com.alensw.a.q c;
   public int a;
   private final com.alensw.b.c.d b = new com.alensw.a.r(this, 16);

   public q() {
      this.a = 0;
   }

   public static synchronized com.alensw.a.q a(Context context) {
      if (c == null) {
         c = new com.alensw.a.q();
         c.b(context);
      }
      return c;
   }

   private void b(Context var1) {
      this.a = PreferenceManager.getDefaultSharedPreferences(var1).getInt("default_sort", 0);
      File var2 = com.alensw.b.l.b.b(var1, "folder_modes");
      this.b.a(var2);
      File var4 = com.alensw.b.c.b.a(var1, "cfolder_modes");
      com.alensw.b.c.d var5 = new com.alensw.b.c.d(256);
      if (var4.exists() && var5.a(var4)) {
         for (Object value : var5.f()) {
            Entry var6 = (Entry)value;
            this.b.a((String)var6.getKey(), var6.getValue());
         }

         if (this.b.e()) {
            this.b.h();
         }

         com.alensw.b.l.b.c(var4);
      }
   }

   public int a(com.alensw.a.e var1) {
      Integer var3 = (Integer)this.b.b(var1.getPath());
      int var2;
      if (var3 != null) {
         var2 = var3;
      } else {
         var2 = var1.a(this.a);
      }

      return var2;
   }

   public int a(CommonFolder var1) {
      int var2 = 2;
      Integer var5 = (Integer)this.b.b(var1.i().toString());
      if (var5 != null) {
         var2 = var5;
      } else {
         int var4 = var1.c();
         byte var3;
         if ((var4 & 16) != 0) {
            var3 = 0;
         } else {
            var3 = 2;
         }

         if (this.a == 0) {
            if ((var4 & 32) != 0) {
               var2 = 1;
            }
         } else if (this.a == 5) {
            var2 = 3;
         } else if (this.a == 6) {
            var2 = 1;
         } else {
            var2 = this.a;
         }

         var2 = CommonFolder.a(var3, var2);
      }

      return var2;
   }

   public void b(com.alensw.a.e var1) {
      String var2 = var1.getPath();
      this.b.a(var2, var1.d);
      this.b.h();
   }

   public void b(CommonFolder var1) {
      String var2 = var1.i().toString();
      this.b.a(var2, var1.l);
      this.b.h();
   }
}
