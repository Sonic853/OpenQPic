package com.alensw.ui.activity;

import java.io.File;

class bh implements com.alensw.ui.a.an {
   final PathListActivity a;

   bh(PathListActivity var1) {
      this.a = var1;
   }

   @Override
   public void a(File var1) {
      String var3 = var1.getPath();
      int var2 = com.alensw.b.l.b.a(this.a.a, var3, com.alensw.b.b.b.a);
      if (var2 < 0) {
         var2 = -var2;
         this.a.a.add(var2 - 1, var3);
         this.a.d = true;
         this.a.b.notifyDataSetChanged();
      }
   }
}
