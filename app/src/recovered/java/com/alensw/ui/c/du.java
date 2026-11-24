package com.alensw.ui.c;

import android.widget.Toast;
import java.io.File;

class du implements Runnable {
   final boolean a;
   final com.alensw.ui.c.dp b;

   du(com.alensw.ui.c.dp var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (com.alensw.ui.c.dp.a(this.b).j() == 0) {
         String var3 = com.alensw.ui.c.dp.b(this.b).getPath();
         if (var3 == null) {
            Toast.makeText(this.b.C, com.alensw.PicFolder.R.string.no_picture_in_album, 1).show();
            this.b.E();
         }

         File var2 = new File(var3);
         char var1 = com.alensw.b.f.c.a(var3);
         com.alensw.a.e var4 = com.alensw.ui.c.dp.a(this.b);
         if (var1 == 0) {
            var1 = 'I';
         }

         var4.a(var1, (int)(var2.lastModified() / 1000L), var2.length(), com.alensw.b.l.b.c(var3));
      }

      if (this.b.H == 2) {
         if (this.a) {
            com.alensw.ui.c.dp.c(this.b);
         } else {
            com.alensw.ui.c.dp.d(this.b);
         }
      }
   }
}
