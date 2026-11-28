package com.alensw.gallery;

import com.alensw.PicFolder.QuickApp;
import java.util.HashMap;
import java.util.concurrent.Callable;

class QueryFolderMediaTask implements Callable {
   final boolean a;
   final String b;
   final FolderScanner c;

   QueryFolderMediaTask(FolderScanner var1, boolean var2, String var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   public HashMap a() {
      String[] var1;
      if (this.a) {
         var1 = GalleryMediaStore.m;
      } else {
         var1 = GalleryMediaStore.l;
      }

      return QuickApp.s.a(this.b, var1);
   }

   @Override
   public Object call() {
      return this.a();
   }
}
