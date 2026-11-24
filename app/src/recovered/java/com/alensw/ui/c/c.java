package com.alensw.ui.c;

import android.content.ContentResolver;
import android.content.Intent;
import com.alensw.PicFolder.QuickApp;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

class c implements com.alensw.ui.c.m {
   final com.alensw.ui.c.n a;
   final boolean b;
   final int c;
   final HashSet d;
   final ContentResolver e;
   final com.alensw.ui.c.a f;

   c(com.alensw.ui.c.a var1, com.alensw.ui.c.n var2, boolean var3, int var4, HashSet var5, ContentResolver var6) {
      this.f = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
      this.e = var6;
   }

   @Override
   public void a(Intent var1) {
      this.a.a(this.b);
      File var10 = new File(var1.getData().getPath());
      com.alensw.a.bc var3 = QuickApp.r;
      com.alensw.a.ba var7 = QuickApp.s;
      HashSet var6 = new HashSet(this.c);
      ArrayList var5 = new ArrayList(this.c);
      ArrayList var4 = new ArrayList(this.c);
      StringBuilder var8 = new StringBuilder();
      com.alensw.ui.c.a var9 = this.f;
      int var2;
      if (this.b) {
         var2 = com.alensw.PicFolder.R.string.copy_to;
      } else {
         var2 = com.alensw.PicFolder.R.string.move_to;
      }

      String var11 = var8.append(var9.g(var2)).append(": ").append(var10.getName()).toString();
      new com.alensw.ui.c.d(this, this.f.C, var11, this.c, var10, var7, var3, var6, var5, var4).c();
   }
}
