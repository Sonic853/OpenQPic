package com.alensw.ui.page;

import android.content.ContentResolver;
import android.content.Intent;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.ThumbnailDatabase;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

class CopyMoveDestinationCallback implements FolderIntentCallback {
   final CopyMoveCallback a;
   final boolean b;
   final int c;
   final HashSet d;
   final ContentResolver e;
   final MediaActionPage f;

   CopyMoveDestinationCallback(MediaActionPage var1, CopyMoveCallback var2, boolean var3, int var4, HashSet var5, ContentResolver var6) {
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
      ThumbnailDatabase var3 = QuickApp.r;
      GalleryMediaStore var7 = QuickApp.s;
      HashSet var6 = new HashSet(this.c);
      ArrayList var5 = new ArrayList(this.c);
      ArrayList var4 = new ArrayList(this.c);
      StringBuilder var8 = new StringBuilder();
      MediaActionPage var9 = this.f;
      int var2;
      if (this.b) {
         var2 = com.alensw.PicFolder.R.string.copy_to;
      } else {
         var2 = com.alensw.PicFolder.R.string.move_to;
      }

      String var11 = var8.append(var9.g(var2)).append(": ").append(var10.getName()).toString();
      new CopyMoveFilesDialog(this, this.f.C, var11, this.c, var10, var7, var3, var6, var5, var4).c();
   }
}
